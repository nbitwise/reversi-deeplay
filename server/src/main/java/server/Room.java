package server;


import logic.*;
import io.deeplay.Game;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Room {

    public int roomId = 0;
    public String whitePlayer = "";
    public String blackPlayer = "";

    public int whitePlayerId;

    public int blackPlayerId;
    private UUID whitePlayerUUID;
    private UUID blackPlayerUUID;

    public Board board = new Board();

    public Game game = new Game();
    public int gameId;
    public Cell getCell(@NotNull final UUID uuid) {
        if(uuid.equals(whitePlayerUUID)){
            return Cell.WHITE;
        }
        return Cell.BLACK;
    }

    public UUID getOpponentUUID(@NotNull final UUID uuid) {
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
     * Меняет в комнате игроков цветами
     */
    public void changeColor() {
        final String whitePlayerSave = whitePlayer;
        final UUID whitePlayerUUIDSave = whitePlayerUUID;
        whitePlayer = blackPlayer;
        blackPlayer = whitePlayerSave;
        whitePlayerUUID = blackPlayerUUID;
        blackPlayerUUID = whitePlayerUUIDSave;
    }
    /**
     * Проверяет наличие конкретного игрока в комнате.
     *
     * @return возвращает true при наличии и false при отсутствии.
     */
    public boolean hasPlayer(UUID uuid) {
        return whitePlayerUUID.equals(uuid) || blackPlayerUUID.equals(uuid);
    }

    /**
     * Удаляет игрока из комнаты.
     */
    public void removePlayer(@NotNull final UUID uuid) {
        if (whitePlayerUUID.equals(uuid)) {
            whitePlayer = "";
            whitePlayerUUID = null;
        } else if (blackPlayerUUID.equals(uuid)) {
            blackPlayer = "";
            blackPlayerUUID = null;
        }
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


}