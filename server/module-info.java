module reversi.deeplay.server.main {
    requires com.google.gson;
    requires reversi.deeplay.gamelogic.main;
    requires reversi.deeplay.localGame.main;
    requires java.desktop;
    requires reversi.deeplay.database.main;
    requires java.naming;

    exports serverresponses;
    exports serverrequest;
}