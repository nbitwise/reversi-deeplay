package gui;

import guiClient.clientGui;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class MultiplayerWindow extends JFrame {

    public MultiplayerWindow(clientGui clientGui) {
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
        JLabel gifLabel = new JLabel(new ImageIcon("resources/A3.gif"));
        gifLabel.setBounds(0, 0, screenSize.width, screenSize.height);
        layeredPane.add(gifLabel, Integer.valueOf(0)); // Добавляем на задний слой

        // Создаем JLabel для меню и добавляем его на передний слой
        JLabel menuLabel = new JLabel(new ImageIcon("resources/menu.png"));
        menuLabel.setBounds(10, -170, screenSize.width, screenSize.height); // Установите координаты и размер для центрирования
        layeredPane.add(menuLabel, Integer.valueOf(1)); // Добавляем на передний слой поверх кнопки

        // Добавьте здесь другие компоненты и настройте их расположение

        // Создайте изображение с текстом, которое вы хотите добавить
        ImageIcon CreateRoomIcon = new ImageIcon("resources/create.png");
        // Создайте JLabel с изображением
        JLabel CreateRoomLabel = new JLabel(CreateRoomIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int crX = (screenSize.width - CreateRoomIcon.getIconWidth()) / 2 + 10;
        int crY = 500; // Смещение на 100 пикселей ниже
        CreateRoomLabel.setBounds(crX, crY, CreateRoomIcon.getIconWidth(), CreateRoomIcon.getIconHeight());

        // Сделайте JLabel интерактивным (как кнопку)
        CreateRoomLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();
                // Здесь вы можете добавить код для обработки события щелчка на изображении
                MainGameWindow gameWindow = new MainGameWindow(clientGui);
                gameWindow.setVisible(true);
                // Закрыть текущее окно "Main Menu"
                //createroomrequest();

                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                // Здесь вы можете добавить код для подсветки кнопки при наведении
                CreateRoomLabel.setIcon(new ImageIcon("resources/createUP.png"));
                // Воспроизведите звук клика
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Здесь вы можете добавить код для возврата кнопки к обычному состоянию
                CreateRoomLabel.setIcon(CreateRoomIcon);
            }
        });
        layeredPane.add(CreateRoomLabel, Integer.valueOf(1)); // Добавляем на передний слой


        // Создайте изображение с текстом, которое вы хотите добавить
        ImageIcon ConnectToRoomIcon = new ImageIcon("resources/connect.png");
        // Создайте JLabel с изображением
        JLabel ConnectToRoomLabel = new JLabel(ConnectToRoomIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int ctrX = (screenSize.width - ConnectToRoomIcon.getIconWidth()) / 2 + 10;
        int ctrY = 590; // Смещение на 100 пикселей ниже
        ConnectToRoomLabel.setBounds(ctrX, ctrY, ConnectToRoomIcon.getIconWidth(), ConnectToRoomIcon.getIconHeight());

        // Сделайте JLabel интерактивным (как кнопку)
        ConnectToRoomLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();
                // Здесь вы можете добавить код для обработки события щелчка на изображении
                // Закрыть текущее окно "Main Menu"
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                // Здесь вы можете добавить код для подсветки кнопки при наведении
                ConnectToRoomLabel.setIcon(new ImageIcon("resources/connectUP.png"));
                // Воспроизведите звук клика
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Здесь вы можете добавить код для возврата кнопки к обычному состоянию
                ConnectToRoomLabel.setIcon(ConnectToRoomIcon);
            }
        });
        layeredPane.add(ConnectToRoomLabel, Integer.valueOf(1)); // Добавляем на передний слой

        // Создайте изображение с текстом, которое вы хотите добавить
        ImageIcon BackIcon = new ImageIcon("resources/back.png");
        // Создайте JLabel с изображением
        JLabel BackLabel = new JLabel(BackIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int backmX = (screenSize.width - BackIcon.getIconWidth()) / 2 + 10;
        int backmY = 680; // Смещение на 100 пикселей ниже
        BackLabel.setBounds(backmX, backmY, BackIcon.getIconWidth(), BackIcon.getIconHeight());

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