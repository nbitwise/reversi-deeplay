package io.deeplay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        final String nonStableId = new SimpleDateFormat("MMddHHmmss").format(Calendar.getInstance().getTime());
        Player.BotPlayer b = new Player.BotPlayer(Cell.BLACK);
        Player.BotPlayer w = new Player.BotPlayer(Cell.WHITE);
        Player.BotPlayer b2 = new Player.BotPlayer(Cell.BLACK);
        Player.BotPlayer w2 = new Player.BotPlayer(Cell.WHITE);
        Player.HumanPlayer ilya = new Player.HumanPlayer(Cell.BLACK);

        for (int i = 0; i < 10; i++) {
            Game.startGame(new Board(), b, w,
                    Integer.parseInt(nonStableId), "fileForHuman", "systemFile");
        }
        LogAnalyzer.parseLog("fileForHuman", "Analysis");
    }
}