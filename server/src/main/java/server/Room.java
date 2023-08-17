package server;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Room {
    int id;
    String WhitePlayer = "";
    String BlackPlayer = "";
    private UUID whitePlayer;
    private UUID blackPlayer;

    public  boolean checkHavePlace() {
        if ((WhitePlayer.isEmpty() && BlackPlayer.isEmpty()) || (WhitePlayer.isEmpty() || BlackPlayer.isEmpty())) {
            return true;
        }
        return false;
    }
    public boolean hasPlayer(UUID uuid) {
        return WhitePlayer.equals(uuid) || BlackPlayer.equals(uuid);
    }

    public void removePlayer(UUID uuid) {
        if (WhitePlayer.equals(uuid)) {
            WhitePlayer = "";
        } else if (BlackPlayer.equals(uuid)) {
            BlackPlayer = "";
        }
    }

    public void addPlayer(UUID uuid) {
        if (WhitePlayer == null) {
            WhitePlayer = String.valueOf(uuid);
        } else if (BlackPlayer == null) {
            BlackPlayer = String.valueOf(uuid);
        }
    }

    public UUID getWhitePlayer() {
        return whitePlayer;
    }

    public UUID getBlackPlayer() {
        return blackPlayer;
    }

    public void setWhitePlayer(UUID player) {
        this.whitePlayer = player;
    }

    public void setBlackPlayer(UUID player) {
        this.blackPlayer = player;
    }

    public boolean hasNoPlayers() {
        return WhitePlayer == null && BlackPlayer == null;
    }
    public boolean isFull() {
        return whitePlayer != null && blackPlayer != null;
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


}