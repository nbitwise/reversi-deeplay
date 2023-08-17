module reversi.deeplay.server.main {
    requires com.google.gson;
    requires reversi.deeplay.gamelogic.main;
    exports serverresponses;
    exports serserrequest;
    requires com.example.client;
}