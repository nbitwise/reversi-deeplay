module reversi.deeplay.gamelogic.main {
    requires org.apache.logging.log4j;
    exports logic;
    exports gamelogging;
    exports parsing;
    requires java.naming;
    requires reversiAI.database.main;
}