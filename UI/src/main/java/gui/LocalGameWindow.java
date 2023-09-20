package gui;

import guiClient.clientGui;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import static gui.MainGameWindow.musicPlayer;

public class LocalGameWindow extends JFrame {
    static MusicPlayer newMusicPlayer;
    public LocalGameWindow(clientGui clientGui) {
        super("Local Game");
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
        JLabel gifLabel = new JLabel(new ImageIcon("resources/A3.gif"));
        gifLabel.setBounds(0, 0, screenSize.width, screenSize.height);
        layeredPane.add(gifLabel, Integer.valueOf(0)); // Добавляем на задний слой

        // Создаем JLabel для меню и добавляем его на передний слой
        JLabel menuLabel = new JLabel(new ImageIcon("resources/menu.png"));
        menuLabel.setBounds(10, -170, screenSize.width, screenSize.height); // Установите координаты и размер для центрирования
        layeredPane.add(menuLabel, Integer.valueOf(1)); // Добавляем на передний слой поверх кнопки

        // Добавьте здесь другие компоненты и настройте их расположение

        // Создайте изображение с текстом, которое вы хотите добавить
        ImageIcon PlayerVsPlayerIcon = new ImageIcon("resources/pvp.png");
        // Создайте JLabel с изображением
        JLabel PlayerVsPlayerLabel = new JLabel(PlayerVsPlayerIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int pvpX = (screenSize.width - PlayerVsPlayerIcon.getIconWidth()) / 2 + 10;
        int pvpY = 500; // Смещение на 100 пикселей ниже
        PlayerVsPlayerLabel.setBounds(pvpX, pvpY, PlayerVsPlayerIcon.getIconWidth(), PlayerVsPlayerIcon.getIconHeight());

        // Сделайте JLabel интерактивным (как кнопку)
        PlayerVsPlayerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();
                musicPlayer.stop();
                // Здесь вы можете добавить код для обработки события щелчка на изображении
                LocalGameVsHuman localGameVsHuman = new LocalGameVsHuman(clientGui);
                localGameVsHuman.setVisible(true);

                LocalGameWindow.newMusicPlayer = new MusicPlayer("resources/game.wav");
                LocalGameWindow.newMusicPlayer.play();
                // Закрыть текущее окно "Main Menu"

                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                // Здесь вы можете добавить код для подсветки кнопки при наведении
                PlayerVsPlayerLabel.setIcon(new ImageIcon("resources/pvpUP.png"));
                // Воспроизведите звук клика
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Здесь вы можете добавить код для возврата кнопки к обычному состоянию
                PlayerVsPlayerLabel.setIcon(PlayerVsPlayerIcon);
            }
        });
        layeredPane.add(PlayerVsPlayerLabel, Integer.valueOf(1)); // Добавляем на передний слой


        // Создайте изображение с текстом, которое вы хотите добавить
        ImageIcon PlayerVsBotIcon = new ImageIcon("resources/pve.png");
        // Создайте JLabel с изображением
        JLabel PlayerVsBotLabel = new JLabel(PlayerVsBotIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int pvbX = (screenSize.width - PlayerVsBotIcon.getIconWidth()) / 2 + 10;
        int pvbY = 590; // Смещение на 100 пикселей ниже
        PlayerVsBotLabel.setBounds(pvbX, pvbY, PlayerVsBotIcon.getIconWidth(), PlayerVsBotIcon.getIconHeight());

        // Сделайте JLabel интерактивным (как кнопку)
        PlayerVsBotLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();
                musicPlayer.stop();
                // Здесь вы можете добавить код для обработки события щелчка на изображении
                LocalGameVsBot localGameVsBot = new LocalGameVsBot(clientGui);
                localGameVsBot.setVisible(true);

                LocalGameWindow.newMusicPlayer = new MusicPlayer("resources/game.wav");
                LocalGameWindow.newMusicPlayer.play();
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                // Здесь вы можете добавить код для подсветки кнопки при наведении
                PlayerVsBotLabel.setIcon(new ImageIcon("resources/pveUP.png"));
                // Воспроизведите звук клика
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Здесь вы можете добавить код для возврата кнопки к обычному состоянию
                PlayerVsBotLabel.setIcon(PlayerVsBotIcon);
            }
        });
        layeredPane.add(PlayerVsBotLabel, Integer.valueOf(1)); // Добавляем на передний слой

        ;;;;;;;;;
        // Создайте изображение с текстом, которое вы хотите добавить
        ImageIcon WatchBotVsBotIcon = new ImageIcon("resources/bvb.png");
        // Создайте JLabel с изображением
        JLabel WatchBotVsBotLabel = new JLabel(WatchBotVsBotIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int wbvbX = (screenSize.width - WatchBotVsBotIcon.getIconWidth()) / 2 + 10;
        int wbvbY = 680; // Смещение на 100 пикселей ниже
        WatchBotVsBotLabel.setBounds(wbvbX, wbvbY, WatchBotVsBotIcon.getIconWidth(), WatchBotVsBotIcon.getIconHeight());

        // Сделайте JLabel интерактивным (как кнопку)
        WatchBotVsBotLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();
                musicPlayer.stop();
                // Здесь вы можете добавить код для обработки события щелчка на изображении
                LocalGameBotVsBot localGameBotVsBot = new LocalGameBotVsBot(clientGui);
                localGameBotVsBot.setVisible(true);

                LocalGameWindow.newMusicPlayer = new MusicPlayer("resources/game.wav");
                LocalGameWindow.newMusicPlayer.play();
                // Закрыть текущее окно "Main Menu"
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                // Здесь вы можете добавить код для подсветки кнопки при наведении
                WatchBotVsBotLabel.setIcon(new ImageIcon("resources/bvbUP.png"));
                // Воспроизведите звук клика
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Здесь вы можете добавить код для возврата кнопки к обычному состоянию
                WatchBotVsBotLabel.setIcon(WatchBotVsBotIcon);
            }
        });
        layeredPane.add(WatchBotVsBotLabel, Integer.valueOf(1)); // Добавляем на передний слой
        ;;;;;;;;;;;

        // Создайте изображение с текстом, которое вы хотите добавить
        ImageIcon BackIcon = new ImageIcon("resources/back.png");
        // Создайте JLabel с изображением
        JLabel BackLabel = new JLabel(BackIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int backX = (screenSize.width - BackIcon.getIconWidth()) / 2 + 10;
        int backY = 770; // Смещение на 100 пикселей ниже
        BackLabel.setBounds(backX, backY, BackIcon.getIconWidth(), BackIcon.getIconHeight());

        // Сделайте JLabel интерактивным (как кнопку)
        BackLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();
                // Здесь вы можете добавить код для обработки события щелчка на изображении
                // Закрыть текущее окно "Main Menu"
                MainMenuWindow gameWindow = new MainMenuWindow(clientGui);
                gameWindow.setVisible(true);
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                // Здесь вы можете добавить код для подсветки кнопки при наведении
                BackLabel.setIcon(new ImageIcon("resources/backUP.png"));
                // Воспроизведите звук клика
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Здесь вы можете добавить код для возврата кнопки к обычному состоянию
                BackLabel.setIcon(BackIcon);
            }
        });
        layeredPane.add(BackLabel, Integer.valueOf(1)); // Добавляем на передний слой



        setVisible(true);
    }

    public void playClickSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("resources/click.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void playTapSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("resources/tap.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
