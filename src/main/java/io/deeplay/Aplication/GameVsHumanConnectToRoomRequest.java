package io.deeplay.Aplication;

public class GameVsHumanConnectToRoomRequest implements Request {
    String command = "connectToRoom";
    String roomId;

    GameVsHumanConnectToRoomRequest(String roomId) {
        this.roomId = roomId;
    }
}
