module io._7st._7thmodpackupdater {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires org.apache.commons.io;
    requires zip4j;
    requires com.google.gson;

    opens io._7st._7thmodpackupdater to javafx.fxml, com.google.gson;
    exports io._7st._7thmodpackupdater;
}