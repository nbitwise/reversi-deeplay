module reversi.deeplay.server.main {
    requires com.google.gson;
    requires reversi.deeplay.gamelogic.main;
    requires reversi.deeplay.localGame.main;
    requires java.desktop;

    exports serverresponses;
    exports serverrequest;
}