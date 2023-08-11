package io.deeplay.Aplication;

import java.util.List;

public class GameVsHumanFindRoomResponse implements Response {
    String status;
    List<AvailableRoomInfo> availableRooms;

    static class AvailableRoomInfo {
        String roomId;
        String timeControl;
    }

    GameVsHumanFindRoomResponse(String status, List<AvailableRoomInfo> availableRooms) {
        this.status = status;
        this.availableRooms = availableRooms;
    }
}
