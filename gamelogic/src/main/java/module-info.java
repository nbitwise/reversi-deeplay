module reversi.deeplay.gamelogic.main {
    requires org.apache.logging.log4j;
    exports logic;
    exports ui;
    exports gamelogging;
    requires com.google.gson;
    exports parsing;
    opens gamelogging to com.google.gson;
    opens logic to com.google.gson;
}