package gui;

import guiClient.clientGui;

import javax.swing.*;
import java.awt.*;


public class MainGameWindow extends JFrame {
    public static MusicPlayer musicPlayer;
    public MainGameWindow(clientGui clientGui) {
        super("Reversi Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        // Создаем панель с размещением слоев
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBackground(Color.BLACK); // Устанавливаем черный цвет фона
        setContentPane(layeredPane);

        // Создаем JLabel для отображения анимированного GIF и добавляем его на задний слой
        JLabel gifLabel = new JLabel(new ImageIcon("resources/retro.gif"));
        gifLabel.setBounds(0, 0, screenSize.width, screenSize.height);
        layeredPane.add(gifLabel, Integer.valueOf(0)); // Добавляем на задний слой

        // Добавьте другие компоненты (кнопки и т. д.) на передний слой
        JButton backButton = new JButton("Back");
        backButton.setBounds(100, 100, 100, 50);
        layeredPane.add(backButton, Integer.valueOf(1)); // Добавляем на передний слой

        // Добавьте здесь другие компоненты и настройте их расположение
        setVisible(true);
    }
    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainGameWindow.musicPlayer = new MusicPlayer("resources/retro.wav");
                MainGameWindow.musicPlayer.play();
                new MainStartWindow().setVisible(true);
            }
        });
    }*/
}
