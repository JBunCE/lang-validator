module com.jbunce.pvlpus {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.fxmisc.richtext;
    requires org.fxmisc.flowless;
    requires org.fxmisc.undo;
    requires lombok;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires org.slf4j;

    opens com.jbunce.pvlpus to javafx.fxml;
    exports com.jbunce.pvlpus;
}