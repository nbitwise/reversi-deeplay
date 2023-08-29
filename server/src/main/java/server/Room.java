package server;

import io.deeplay.Game;
import logic.Board;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Room {

    int roomId;
    String whitePlayer = "";
    String blackPlayer = "";
    private UUID whitePlayerUUID;
    private UUID blackPlayerUUID;

    Board board = new Board();

    Game game = new Game();

    public int moveNumber = 1;

    public Room() {
        this.roomId = roomCounter.incrementAndGet();
    }

    public UUID getOpponentUUID(UUID uuid) {
        if (uuid == whitePlayerUUID) {
            return blackPlayerUUID;
        } else {
            return whitePlayerUUID;
        }
    }

    /**
     * Проверяет наличие места в комнате.
     *
     * @return возвращает true при наличии мест и false при отсутствии.
     */
    public boolean checkHavePlace() {
        if ((whitePlayer.isEmpty() && blackPlayer.isEmpty()) || (whitePlayer.isEmpty() || blackPlayer.isEmpty())) {
            return true;
        }
        return false;
    }

    /**
     * Проверяет наличие конкретного игрока в комнате.
     *
     * @return возвращает true при наличии и false при отсутствии.
     */
    public boolean hasPlayer(UUID uuid) {
        return whitePlayer.equals(uuid) || blackPlayer.equals(uuid);
    }

    /**
     * Удаляет игрока из комнаты.
     */
    public void removePlayer(UUID uuid) {
        if (whitePlayer.equals(uuid)) {
            whitePlayer = "";
        } else if (blackPlayer.equals(uuid)) {
            blackPlayer = "";
        }
    }
    public void swapPlayers() {
        UUID temp = whitePlayerUUID;
        whitePlayerUUID = blackPlayerUUID;
        blackPlayerUUID = temp;

        String tempName = whitePlayer;
        whitePlayer = blackPlayer;
        blackPlayer = tempName;
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
     * Проверяет, пустая ли комната.
     *
     * @return возвращает true, если пустая, и false, если кто-то есть.
     */
    public boolean hasNoPlayers() {
        return whitePlayer == null && blackPlayer == null;
    }

    /**
     * Проверяет, полная ли комната
     *
     * @return возвращает true если полная, и false если никого нет или игрок только один.
     */
    public boolean isFull() {
        return whitePlayerUUID != null && blackPlayerUUID != null;
    }

    public int getRoomId() {
        return roomId;
    }

    private static final AtomicInteger roomCounter = new AtomicInteger(0);


}