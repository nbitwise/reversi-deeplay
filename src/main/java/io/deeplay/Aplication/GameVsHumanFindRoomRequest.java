package io.deeplay.Aplication;

public class GameVsHumanFindRoomRequest implements Request {
    String command = "findRoom";
    String color;

    GameVsHumanFindRoomRequest(String color) {
        this.color = color;
    }
}
