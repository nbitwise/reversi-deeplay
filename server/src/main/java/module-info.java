module reversiDeeplay.server.main {
    requires com.google.gson;
    requires reversi.deeplay.gamelogic.main;
    requires reversi.deeplay.localGame.main;
    requires com.example.client;
    exports serverresponses;
    exports serverrequest;
    opens serverresponses to com.google.gson;
    opens serverrequest to com.google.gson;
}