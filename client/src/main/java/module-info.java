module com.example.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.jline;
    requires reversi.deeplay.gamelogic.main;


    opens client to javafx.fxml;
    opens clientresponse to com.google.gson;
    exports client;
    exports clientrequest;
    exports clientresponse;

}