module reversi.deeplay.gamelogic.main {
    requires org.apache.logging.log4j;
    exports logic;
    exports ui;
    exports gamelogging;
    exports parsing;
    opens logic to com.google.gson;
}