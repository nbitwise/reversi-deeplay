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
    /**
     * проверяет наличие места в комнате.
     * @return возвращает true при наличии мест и false при отсутствии.
     */
    public  boolean checkHavePlace() {
        if ((WhitePlayer.isEmpty() && BlackPlayer.isEmpty()) || (WhitePlayer.isEmpty() || BlackPlayer.isEmpty())) {
            return true;
        }
        return false;
    }

    /**
     * проверяет наличие конкретного игрока в комнате.
     * @return возвращает true при наличии и false при отсутствии.
     */
    public boolean hasPlayer(UUID uuid) {
        return WhitePlayer.equals(uuid) || BlackPlayer.equals(uuid);
    }

    /**
     * удаляет игрока из комнаты
     */
    public void removePlayer(UUID uuid) {
        if (WhitePlayer.equals(uuid)) {
            WhitePlayer = "";
        } else if (BlackPlayer.equals(uuid)) {
            BlackPlayer = "";
        }
    }
    public void swapPlayers() {
        UUID temp = whitePlayerUUID;
        whitePlayerUUID = blackPlayerUUID;
        blackPlayerUUID = temp;

        String tempName = WhitePlayer;
        WhitePlayer = BlackPlayer;
        BlackPlayer = tempName;
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

    /**
     * проверяет, пустая ли комната.
     * @return возвращает true, если пустая, и false, если кто-то есть.
     */
    public boolean hasNoPlayers() {
        return WhitePlayer == null && BlackPlayer == null;
    }

    /**
     * проверяет, полная ли комната
     * @return возвращает true если полная, и false если никого нет или игрок только один.
     */
    public boolean isFull() {
        return whitePlayerUUID != null && blackPlayerUUID != null;
    }
    public Room() {
        this.id = roomCounter.incrementAndGet();
    }

    public int getId() {
        return id;
    }
    private static final AtomicInteger roomCounter = new AtomicInteger(0);


}