module org.example.view {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.example;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires org.slf4j;
    requires java.sql;

    opens org.example.view to javafx.fxml;
    exports org.example.view;

}