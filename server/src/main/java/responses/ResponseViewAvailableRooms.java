package responses;

import java.util.ArrayList;

public class ResponseViewAvailableRooms extends Response {
    private ArrayList<Integer> listOfRooms = new ArrayList<>();

    public ResponseViewAvailableRooms(final String status,final String message,final ArrayList<Integer> listOfRooms) {
        super(status, message);
        this.listOfRooms = listOfRooms;
    }

    public ArrayList<Integer> getListOfRooms() {
        return listOfRooms;
    }
}
