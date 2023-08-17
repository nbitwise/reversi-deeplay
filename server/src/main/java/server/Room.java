package server;

import java.util.concurrent.atomic.AtomicInteger;

public class Room {
    int id;

    static String WhitePlayer;
    static String BlackPlayer;

    public static boolean checkHavePlace() {
        if (WhitePlayer.isEmpty() && BlackPlayer.isEmpty()) {
            return true;
        }
        return false;
    }

    public Room() {
        this.id = roomCounter.incrementAndGet();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private static final AtomicInteger roomCounter = new AtomicInteger(0);


    // Предположим, у вас есть список комнат roomList


}