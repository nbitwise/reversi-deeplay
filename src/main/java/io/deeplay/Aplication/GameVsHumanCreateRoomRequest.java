package io.deeplay.Aplication;

public class GameVsHumanCreateRoomRequest implements Request {
    String command = "createRoom";
    String color;
    String timeControl;

    GameVsHumanCreateRoomRequest(String color, String timeControl) {
        this.color = color;
        this.timeControl = timeControl;
    }
}
