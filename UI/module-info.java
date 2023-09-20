module reversi.deeplay.UI.main {
    requires reversi.deeplay.gamelogic.main;
    requires java.desktop;
    requires org.apache.pdfbox;
    exports gui;
    exports ui;
    exports Replayer;
    requires java.naming;
    requires reversiAI.database.main;
    requires com.example.client;
    requires com.google.gson;
    requires org.apache.logging.log4j;
}