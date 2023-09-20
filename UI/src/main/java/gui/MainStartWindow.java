package gui;

import guiClient.clientGui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.sound.sampled.*;


public class MainStartWindow extends JFrame {
    public MainStartWindow(clientGui clientGui) {
        super("Main Start");
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

        // Создаем JLabel для нового анимированного GIF и добавляем его на передний слой
        JLabel additionalGifLabel = new JLabel(new ImageIcon("resources/AF1.gif"));
        additionalGifLabel.setBounds(0, -200, screenSize.width, screenSize.height); // Установите координаты и размер для центрирования
        layeredPane.add(additionalGifLabel, Integer.valueOf(1)); // Добавляем на передний слой поверх кнопки


        // Создайте изображение с текстом, которое вы хотите добавить
        ImageIcon StartIcon = new ImageIcon("resources/st.png");
        // Создайте JLabel с изображением
        JLabel StartLabel = new JLabel(StartIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int StartX = (screenSize.width - StartIcon.getIconWidth()) / 2;
        int StartY = 600; // Смещение на 100 пикселей ниже
        StartLabel.setBounds(StartX, StartY, StartIcon.getIconWidth(), StartIcon.getIconHeight());

        // Сделайте JLabel интерактивным (как кнопку)
        StartLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();
                // Здесь вы можете добавить код для обработки события щелчка на изображении
                MainMenuWindow gameWindow = new MainMenuWindow(clientGui);
                gameWindow.setVisible(true);
                // Закрыть текущее окно "Main Menu"
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Здесь вы можете добавить код для подсветки кнопки при наведении
                StartLabel.setIcon(new ImageIcon("resources/str.png"));
                // Воспроизведите звук клика
                playTapSound();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Здесь вы можете добавить код для возврата кнопки к обычному состоянию
                StartLabel.setIcon(StartIcon);
            }
        });


        // Добавьте JLabel с изображением на передний слой
        layeredPane.add(StartLabel, Integer.valueOf(1)); // Добавляем на задний слой
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