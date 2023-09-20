package gui;


import guiClient.clientGui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.sound.sampled.*;


public class MainMenuWindow extends JFrame {
    public MainMenuWindow(clientGui clientGui){
        super("Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);


        // Создаем панель с размещением слоев
        JLayeredPane layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        // Создаем JLabel для отображения анимированного GIF и добавляем его на задний слой
        JLabel gifLabel = new JLabel(new ImageIcon("resources/A3.gif"));
        gifLabel.setBounds(0, 0, screenSize.width, screenSize.height);
        layeredPane.add(gifLabel, Integer.valueOf(0)); // Добавляем на задний слой

        // Создаем JLabel для меню и добавляем его на передний слой
        JLabel menuLabel = new JLabel(new ImageIcon("resources/menu.png"));
        menuLabel.setBounds(10, -170, screenSize.width, screenSize.height); // Установите координаты и размер для центрирования
        layeredPane.add(menuLabel, Integer.valueOf(1)); // Добавляем на передний слой поверх кнопки


        // Создайте изображение с текстом, которое вы хотите добавить
        ImageIcon MultiplayerIcon = new ImageIcon("resources/multiplayer.png");
        // Создайте JLabel с изображением
        JLabel MultiplayerLabel = new JLabel(MultiplayerIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int multiplayerX = (screenSize.width - MultiplayerIcon.getIconWidth()) / 2 + 10;
        int multiplayerY = 460; // Смещение на 100 пикселей ниже
        MultiplayerLabel.setBounds(multiplayerX, multiplayerY, MultiplayerIcon.getIconWidth(), MultiplayerIcon.getIconHeight());

        // Сделайте JLabel интерактивным (как кнопку)
        MultiplayerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();
                // Здесь вы можете добавить код для обработки события щелчка на изображении

                clientGui.regAndAuth = new RegAndAuth(clientGui);


                // Закрыть текущее окно "Main Menu"
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                // Здесь вы можете добавить код для подсветки кнопки при наведении
                MultiplayerLabel.setIcon(new ImageIcon("resources/multiplayerUP.png"));
                // Воспроизведите звук клика
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Здесь вы можете добавить код для возврата кнопки к обычному состоянию
                MultiplayerLabel.setIcon(MultiplayerIcon);
            }
        });
        layeredPane.add(MultiplayerLabel, Integer.valueOf(1)); // Добавляем на передний слой


        // Создайте изображение с текстом, которое вы хотите добавить
        ImageIcon localGameIcon = new ImageIcon("resources/localgame.png");
        // Создайте JLabel с изображением
        JLabel localGameLabel = new JLabel(localGameIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int localgameX = (screenSize.width - localGameIcon.getIconWidth()) / 2;
        int localgameY = 530; // Смещение на 100 пикселей ниже
        localGameLabel.setBounds(localgameX, localgameY, localGameIcon.getIconWidth(), localGameIcon.getIconHeight());

        // Сделайте JLabel интерактивным (как кнопку)
        localGameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();
                // Здесь вы можете добавить код для обработки события щелчка на изображении
                LocalGameWindow localGameWindow = new LocalGameWindow(clientGui);
                localGameWindow.setVisible(true);
                // Закрыть текущее окно "Main Menu"
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                // Здесь вы можете добавить код для подсветки кнопки при наведении
                localGameLabel.setIcon(new ImageIcon("resources/localgameUP.png"));
                // Воспроизведите звук клика
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Здесь вы можете добавить код для возврата кнопки к обычному состоянию
                localGameLabel.setIcon(localGameIcon);
            }
        });
        layeredPane.add(localGameLabel, Integer.valueOf(1)); // Добавляем на передний слой

        ImageIcon InfoIcon = new ImageIcon("resources/info.png");
        // Создайте JLabel с изображением
        JLabel InfoLabel = new JLabel(InfoIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int infoX = (screenSize.width - localGameIcon.getIconWidth()) / 2;
        int infoY = 590; // Смещение на 100 пикселей ниже
        InfoLabel.setBounds(infoX, infoY, InfoIcon.getIconWidth(), InfoIcon.getIconHeight());

        // Сделайте JLabel интерактивным (как кнопку)
        InfoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();
                InfoWindow infoWindow = new InfoWindow(MainMenuWindow.this); // "this" - ссылка на текущее окно
                infoWindow.setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                // Здесь вы можете добавить код для подсветки кнопки при наведении
                InfoLabel.setIcon(new ImageIcon("resources/infoUP.png"));
                // Воспроизведите звук клика
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Здесь вы можете добавить код для возврата кнопки к обычному состоянию
                InfoLabel.setIcon(InfoIcon);
            }
        });
        layeredPane.add(InfoLabel, Integer.valueOf(1)); // Добавляем на передний слой


        ImageIcon quitGameIcon = new ImageIcon("resources/quit.png");
        // Создайте JLabel с изображением
        JLabel quitGameLabel = new JLabel(quitGameIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int quitX = (screenSize.width - localGameIcon.getIconWidth()) / 2;
        int quitY = 650; // Смещение на 100 пикселей ниже
        quitGameLabel.setBounds(quitX, quitY, quitGameIcon.getIconWidth(), quitGameIcon.getIconHeight());

        // Сделайте JLabel интерактивным (как кнопку)
        quitGameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();
                System.exit(0);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                // Здесь вы можете добавить код для подсветки кнопки при наведении
                quitGameLabel.setIcon(new ImageIcon("resources/quitUP.png"));
                // Воспроизведите звук клика
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Здесь вы можете добавить код для возврата кнопки к обычному состоянию
                quitGameLabel.setIcon(quitGameIcon);
            }
        });
        layeredPane.add(quitGameLabel, Integer.valueOf(1)); // Добавляем на передний слой


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