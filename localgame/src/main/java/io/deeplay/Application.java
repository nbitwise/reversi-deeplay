package io.deeplay;


import gui.GUI;
import logic.Board;
import logic.Cell;
import logic.Player;

import javax.swing.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import static logic.Player.HumanPlayer.scanner;

public class Application {
    public static void main(String[] args) {
        System.out.println("Выберите режим интерфейса:");
        System.out.println("1. Консольный интерфейс");
        System.out.println("2. Графический интерфейс");
        int choice = scanner.nextInt();

        if (choice == 1) {
            startConsoleInterface();

        } else if (choice == 2) {
            SwingUtilities.invokeLater(() -> startGUIInterface());

        } else {
            System.out.println("Неверный выбор");
        }
    }

    private static void startConsoleInterface() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите режим игры:");
        System.out.println("1. Human vs Human");
        System.out.println("2. Human vs Bot");
        System.out.println("3. Bot vs Bot");
        int choice = scanner.nextInt();

        if (choice == 1) {
            startHumanVsHumanGame();
        } else if (choice == 2) {
            startHumanVsBotGame();
        } else if (choice == 3) {
            startBotVsBotGame();
        }else {
            System.out.println("Неверный выбор");
        }
    }

    private static void startHumanVsBotGame() {
        final String nonStableId = new SimpleDateFormat("MMddHHmmss").format(Calendar.getInstance().getTime());
        new Game().startGame(new Board(), new Player.HumanPlayer(Cell.BLACK), new Player.BotPlayer(Cell.WHITE),
                Integer.parseInt(nonStableId), "fileForHuman", "systemFile");
    }
    private static void startHumanVsHumanGame() {
        final String nonStableId = new SimpleDateFormat("MMddHHmmss").format(Calendar.getInstance().getTime());
        new Game().startGame(new Board(), new Player.HumanPlayer(Cell.BLACK), new Player.HumanPlayer(Cell.WHITE),
                Integer.parseInt(nonStableId), "fileForHuman", "systemFile");
    }
    private static void startBotVsBotGame() {
        final String nonStableId = new SimpleDateFormat("MMddHHmmss").format(Calendar.getInstance().getTime());
        new Game().startGame(new Board(), new Player.BotPlayer(Cell.BLACK), new Player.BotPlayer(Cell.WHITE),
                Integer.parseInt(nonStableId), "fileForHuman", "systemFile");
    }

    private static void startGUIInterface() {
        new GUI();
    }
}
