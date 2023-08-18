module com.example.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.jline;


    opens client to javafx.fxml;
    exports client;
    exports clientrequest;
    exports clientresponse;

}