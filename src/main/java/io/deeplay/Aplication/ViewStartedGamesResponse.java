package io.deeplay.Aplication;

import java.util.List;

public class ViewStartedGamesResponse implements Response {
    String status;
    String message;
    List<String> roomIds;

    ViewStartedGamesResponse(String status, String message, List<String> roomIds) {
        this.status = status;
        this.message = message;
        this.roomIds = roomIds;
    }
}
