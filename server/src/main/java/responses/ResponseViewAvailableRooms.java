package responses;

import java.util.ArrayList;

public class ResponseViewAvailableRooms extends Response {
    ArrayList<Integer> listOfRooms = new ArrayList<>();

    public ResponseViewAvailableRooms(String status, String message, ArrayList<Integer> listOfRooms) {
        super(status, message);
        this.listOfRooms = listOfRooms;
    }
}
