module reversi.deeplay.botFarm.main {
    requires com.google.gson;
    requires java.desktop;
    requires reversi.deeplay.gamelogic.main;

    exports botresponses;
    exports botrequests;

    opens botresponses to com.google.gson;
    opens botrequests to com.google.gson;

}