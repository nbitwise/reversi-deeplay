package io.deeplay;

import logic.Board;
import logic.Cell;
import logic.Player;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main {
    public static void main(String[] args) {
        final String nonStableId = new SimpleDateFormat("MMddHHmmss").format(Calendar.getInstance().getTime());
        new Game().startGame(new Board(), new Player.BotPlayer(Cell.BLACK), new Player.BotPlayer(Cell.WHITE),
                Integer.parseInt(nonStableId), "fileForHuman", "systemFile");
    }
}