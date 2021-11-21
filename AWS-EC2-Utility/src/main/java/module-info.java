module com.dassoop.awsec2utility {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.datatransfer;
    requires com.google.gson;


    opens com.dassoop.awsec2utility to javafx.fxml;
    exports com.dassoop.awsec2utility;
}