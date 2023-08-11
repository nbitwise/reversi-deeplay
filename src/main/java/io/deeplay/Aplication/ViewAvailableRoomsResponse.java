package io.deeplay.Aplication;

import java.util.List;

public class ViewAvailableRoomsResponse implements Response {
    String status;
    String message;
    List<String> roomIds;

    ViewAvailableRoomsResponse(String status, String message, List<String> roomIds) {
        this.status = status;
        this.message = message;
        this.roomIds = roomIds;
    }
}
