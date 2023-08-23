package server;

import io.deeplay.Game;
import logic.Board;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static server.Server.roomList;

public class Room {

    int id;
    String WhitePlayer = "";
    String BlackPlayer = "";
    private UUID whitePlayerUUID;
    private UUID blackPlayerUUID;

    Board board = new Board();

    Game game = new Game();

    public int moveNumber = 1;

    public UUID getOpponentUUID(UUID uuid){
        if(uuid == whitePlayerUUID){
            return blackPlayerUUID;
        }
        else {
            return whitePlayerUUID;
        }
    }

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
    private static boolean roomExists(int roomId) {
        for (Room room : roomList) {
            if (room.getId() == roomId) {
                return true;
            }
        }
        return false;
    }
    private static boolean roomExistsAndHasPlace(int roomId) {
        for (Room room : roomList) {
            if (room.getId() == roomId && room.checkHavePlace()) {
                return true;
            }
        }
        return false;
    }
    private static boolean roomExistsAndHasPlayer(int roomId, UUID uuid) {
        for (Room room : roomList) {
            if (room.getId() == roomId && room.hasPlayer(uuid)) {
                return true;
            }
        }
        return false;
    }
    public void startServerGame() throws IOException {
        game = new Game();
        board = new Board();
        moveNumber = 1;
    }
    public UUID getWhitePlayerUUID() {
        return whitePlayerUUID;
    }

    public UUID getBlackPlayerUUID() {
        return blackPlayerUUID;
    }

    public void setWhitePlayerUUID(UUID player) {
        this.whitePlayerUUID = player;
    }

    public void setBlackPlayerUUID(UUID player) {
        this.blackPlayerUUID = player;
    }

    public boolean hasNoPlayers() {
        return WhitePlayer == null && BlackPlayer == null;
    }
    public boolean isFull() {
        return whitePlayerUUID != null && blackPlayerUUID != null;
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