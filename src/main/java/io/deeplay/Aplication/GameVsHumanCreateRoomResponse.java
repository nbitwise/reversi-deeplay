package io.deeplay.Aplication;

public class GameVsHumanCreateRoomResponse implements Response {
    String status;
    String roomId;

    GameVsHumanCreateRoomResponse(String status, String roomId) {
        this.status = status;
        this.roomId = roomId;
    }
}
