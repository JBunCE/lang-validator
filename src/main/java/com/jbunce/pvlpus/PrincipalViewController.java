package com.jbunce.pvlpus;

import ch.qos.logback.classic.Logger;
import com.jbunce.pvlpus.appenders.TextFlowAppender;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import lombok.extern.slf4j.Slf4j;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PrincipalViewController implements Initializable {

    @FXML
    private VBox filesVbox;
    @FXML
    private CodeArea codeArea;
    @FXML
    private VirtualizedScrollPane<CodeArea> codeScrollPane;
    @FXML
    private TextFlow logArea;
    @FXML
    private Label codet;
    private String directoryPath = "";
    private final Set<String> errorLines = new HashSet<>();
    private final List<String> lineStyles = new ArrayList<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // LOGGER
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAndStopAllAppenders();

        TextFlowAppender appender = new TextFlowAppender(logArea);
        appender.start();
        logger.addAppender(appender);

        log.info("Iniciando programa");

        codeArea.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        codeScrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Platform.runLater(() -> {
            IntFunction<Node> lineNumberFactory = LineNumberFactory.get(codeArea);
            codeArea.setParagraphGraphicFactory(lineNumberFactory);
        });
    }

    @FXML
    public void openFolder() {
        if (Objects.equals(directoryPath, "")) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(null);
            if (selectedDirectory != null) {
                directoryPath = selectedDirectory.getAbsolutePath();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setContentText("No se ha seleccionado un directorio");
                alert.showAndWait();
            }
        }

        File dir = new File(directoryPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                Label label = getLabel(child);
                Platform.runLater(() -> filesVbox.getChildren().add(label));
            }
        }
    }

    @FXML
    public void onCodeAreaChange() {
        Platform.runLater(() -> executor.submit(this::validate));
    }

    @FXML
    public void onBuildClick() {
        log.info("Starting validation");
        validate();
        log.info("Validation finished");
    }

    private void validate () {
        String text = codeArea.getText();
        String[] lines = text.split("\n");
        int braceCount = 0;

        while (lineStyles.size() < lines.length) {
            lineStyles.add("success");
        }
        while (lineStyles.size() > lines.length) {
            lineStyles.remove(lineStyles.size() - 1);
        }

        Pattern pattern = Pattern.compile(Regexps.ALL);
        for (int i = 0; i < lines.length; i++) {
            Matcher matcher = pattern.matcher(lines[i]);

            for (char ch : lines[i].toCharArray()) {
                if (ch == '{') {
                    braceCount++;
                } else if (ch == '}') {
                    braceCount--;
                }
            }

            if (matcher.find()) {
                lineStyles.set(i, "success");
            } else {
                lineStyles.set(i, "error");
                String error = ": en la línea -> " + (i + 1) + ": " + lines[i] + "\n";
                errorLines.add(error);
            }
        }

        if (braceCount != 0) {
            String error = "Expected " + braceCount + " more '}'\n";
            errorLines.add(error);
        }

        Platform.runLater(this::applyStyles);
        Platform.runLater(() -> logArea.getChildren().clear());
        errorLines.forEach(logText -> Platform.runLater(() -> log.error(logText)));
        errorLines.clear();
    }

    private void applyStyles() {
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        System.out.println(lineStyles.size());
        for (int i = 0; i < lineStyles.size(); i++) {
            spansBuilder.add(Collections.singleton(lineStyles.get(i)), codeArea.getParagraph(i).length());
        }
        codeArea.setStyleSpans(0, spansBuilder.create());
    }

    private Label getLabel(File child) {
        Label label = new Label(child.getName());
        label.setTextFill(Paint.valueOf("#ffffff"));
        label.setStyle("-fx-max-width: infinity");
        label.setOnMouseClicked(event -> {
            try {
                String content = Files.readString(child.toPath());
                Platform.runLater(() -> {
                    codet.setText(child.getName());
                    codeArea.replaceText(content);
                    filesVbox.getChildren().forEach(labelChild -> {
                        Label labelElement = (Label) labelChild;
                        labelElement.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                    });
                    label.setBackground(new Background(new BackgroundFill(Color.valueOf("#1f1f1f"), CornerRadii.EMPTY, Insets.EMPTY)));
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return label;
    }
}