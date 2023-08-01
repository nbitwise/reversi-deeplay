package io.deeplay;

public class Main {
    public static void main(String[] args) {
        Game.startGame(new Board(), new Player.BotPlayer(Cell.BLACK), new Player.BotPlayer(Cell.WHITE));
    }
}