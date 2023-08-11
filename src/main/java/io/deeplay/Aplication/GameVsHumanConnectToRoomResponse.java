package io.deeplay.Aplication;

public class GameVsHumanConnectToRoomResponse implements Response {
    String status;
    String message;

    GameVsHumanConnectToRoomResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
