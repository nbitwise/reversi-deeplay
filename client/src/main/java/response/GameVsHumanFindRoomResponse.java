package response;

import java.util.List;

public class GameVsHumanFindRoomResponse implements Response {
    public String status;
    public List<AvailableRoomInfo> availableRooms;

    public static class AvailableRoomInfo {
        public String roomId;
        public String timeControl;

        public AvailableRoomInfo(String room, String rapid) {
        }
    }

    public GameVsHumanFindRoomResponse(String status, List<AvailableRoomInfo> availableRooms) {
        this.status = status;
        this.availableRooms = availableRooms;
    }
}
