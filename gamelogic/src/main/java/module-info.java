module reversi.deeplay.gamelogic.main {
    requires org.apache.logging.log4j;
    requires java.desktop;
    exports logic;
    exports ui;
    exports gamelogging;
    exports parsing;
    exports gui;
    opens logic to com.google.gson;
}