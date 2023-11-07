package com.jbunce.pvlpus.appenders;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lombok.Setter;

@Setter
public class TextFlowAppender extends AppenderBase<ILoggingEvent> {
    private final TextFlow textFlow;

    public TextFlowAppender(TextFlow textFlow) {
        this.textFlow = textFlow;
    }

    @Override
    protected void append(ch.qos.logback.classic.spi.ILoggingEvent event) {
        Platform.runLater(() -> {
            Text text = new Text(event.getLevel().levelStr + " " + event.getMessage());
            text.setFont(Font.font("Consolas", 15));
            switch (event.getLevel().levelStr) {
                case "ERROR":
                    text.setFill(Color.RED);
                    break;
                case "INFO":
                    text.setFill(Color.BLACK);
                    break;
            }
            textFlow.getChildren().add(text);
        });
    }
}
