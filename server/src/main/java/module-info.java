module reversi.deeplay.server.main {
    requires com.google.gson;
    requires reversi.deeplay.gamelogic.main;
    exports server;
    exports responses;
    //exports request;
    requires com.example.client;
}