package gui;

import guiClient.clientGui;
import logic.*;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import static gui.LocalGameWindow.newMusicPlayer;

//import static Replayer.Replayer.updateBoardColors;

public class LocalGameBotVsBot extends JFrame {
    private final int BOARD_SIZE = 8;
    private final JButton[][] buttons;
    ImageIcon blackPieceIcon = new ImageIcon("resources/blackPiece.png");
    ImageIcon whitePieceIcon = new ImageIcon("resources/whitePiece.png");
    private Board board;
    Color liteBlue = new Color(147, 227, 255);
    Color pink = new Color(234, 147, 255);
    static Color purple = new Color(132, 2, 182);
    static Color blue = new Color(27, 61, 182);
    private ArrayList<PlayerPanel> playerList;
    boolean buttonClicked = false;
    Player botBlack;
    Player botWhite;
    private ImageIcon playIcon;
    private ImageIcon pauseIcon;
    private boolean isPaused = false;

    public LocalGameBotVsBot(clientGui clientGui) {
        super("Local Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        // Создаем панель с размещением слоев
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBackground(pink); // Устанавливаем черный цвет фона
        setContentPane(layeredPane);

        // Создаем JLabel для отображения анимированного GIF и добавляем его на задний слой
        JLabel gifLabel = new JLabel(new ImageIcon("resources/GBG.gif"));
        gifLabel.setBounds(0, 0, screenSize.width, screenSize.height);
        layeredPane.add(gifLabel, Integer.valueOf(0)); // Добавляем на задний слой

        ImageIcon frameIcon = new ImageIcon("resources/frame.gif");
        // Создайте JLabel с изображением
        JLabel frameLabel = new JLabel(frameIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int fX = 1380;
        int fY = 60; // Смещение на 100 пикселей ниже
        frameLabel.setBounds(fX, fY, frameIcon.getIconWidth(), frameIcon.getIconHeight());
        layeredPane.add(frameLabel, Integer.valueOf(2));

        ImageIcon selectBotIcon = new ImageIcon("resources/botselect.png");
        // Создайте JLabel с изображением
        JLabel selectBotLabel = new JLabel(selectBotIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int sbX = 140;
        int sbY = 125; // Смещение на 100 пикселей ниже
        selectBotLabel.setBounds(sbX, sbY, selectBotIcon.getIconWidth(), selectBotIcon.getIconHeight());
        layeredPane.add(selectBotLabel, Integer.valueOf(1));

        ImageIcon purIcon = new ImageIcon("resources/purpleBOT.png");
        // Создайте JLabel с изображением
        JLabel purLabel = new JLabel(purIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int purX = 90;
        int purY = 180; // Смещение на 100 пикселей ниже
        purLabel.setBounds(purX, purY, purIcon.getIconWidth(), purIcon.getIconHeight());
        layeredPane.add(purLabel, Integer.valueOf(1));

        ImageIcon bluIcon = new ImageIcon("resources/blueBOT.png");
        // Создайте JLabel с изображением
        JLabel bluLabel = new JLabel(bluIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int sbuX = 320;
        int sbuY = 180; // Смещение на 100 пикселей ниже
        bluLabel.setBounds(sbuX, sbuY, bluIcon.getIconWidth(), bluIcon.getIconHeight());
        layeredPane.add(bluLabel, Integer.valueOf(1));



        ImageIcon playersIcon = new ImageIcon("resources/players.gif");
        // Создайте JLabel с изображением
        JLabel playersLabel = new JLabel(playersIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int plX = 1570;
        int plY = 60; // Смещение на 100 пикселей ниже
        playersLabel.setBounds(plX, plY, playersIcon.getIconWidth(), playersIcon.getIconHeight());
        layeredPane.add(playersLabel, Integer.valueOf(3));


        ImageIcon purpleIcon = new ImageIcon("resources/purple.png");
        // Создайте JLabel с изображением
        JLabel purpleLabel = new JLabel(purpleIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int pX = 1410;
        int pY = 90; // Смещение на 100 пикселей ниже
        purpleLabel.setBounds(pX, pY, purpleIcon.getIconWidth(), purpleIcon.getIconHeight());
        layeredPane.add(purpleLabel, Integer.valueOf(3));

        ImageIcon blueIcon = new ImageIcon("resources/blue.png");
        // Создайте JLabel с изображением
        JLabel blueLabel = new JLabel(blueIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int blX = 1650;
        int blY = 95; // Смещение на 100 пикселей ниже
        blueLabel.setBounds(blX, blY, blueIcon.getIconWidth(), blueIcon.getIconHeight());
        layeredPane.add(blueLabel, Integer.valueOf(3));

        ImageIcon circleBIcon = new ImageIcon("resources/circleB1.png");
        // Создайте JLabel с изображением
        JLabel circleBLabel = new JLabel(circleBIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int ciBX = 1750;
        int ciBY = 908; // Смещение на 100 пикселей ниже
        circleBLabel.setBounds(ciBX, ciBY, circleBIcon.getIconWidth(), circleBIcon.getIconHeight());
        layeredPane.add(circleBLabel, Integer.valueOf(1));

        ImageIcon circleIcon = new ImageIcon("resources/circle.gif");
        // Создайте JLabel с изображением
        JLabel circleLabel = new JLabel(circleIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int ciX = 1734;
        int ciY = 889; // Смещение на 100 пикселей ниже
        circleLabel.setBounds(ciX, ciY, circleIcon.getIconWidth(), circleIcon.getIconHeight());
        layeredPane.add(circleLabel, Integer.valueOf(2));

        playIcon = new ImageIcon("resources/playPause.png");
        pauseIcon = new ImageIcon("resources/playPauseUP.png");


        ImageIcon PlayPauseIcon = new ImageIcon("resources/playPause.png");
        // Создайте JLabel с изображением
        JLabel PlayPauseLabel = new JLabel(PlayPauseIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int ppaX = 1804;
        int ppaY = 935; // Смещение на 100 пикселей ниже
        PlayPauseLabel.setBounds(ppaX, ppaY, PlayPauseIcon.getIconWidth(), PlayPauseIcon.getIconHeight());

        PlayPauseLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Проверяем состояние и выполняем соответствующие действия
                if (isPaused) {
                    newMusicPlayer.play();
                    PlayPauseLabel.setIcon(playIcon);
                } else {
                    newMusicPlayer.pause();
                    PlayPauseLabel.setIcon(pauseIcon);
                }
                isPaused = !isPaused; // Меняем состояние
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isPaused) {
                    PlayPauseLabel.setIcon(pauseIcon); // При наведении устанавливаем активную иконку
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isPaused) {
                    PlayPauseLabel.setIcon(playIcon); // Возвращаем обычную иконку после ухода мыши
                }
            }
        });

        layeredPane.add(PlayPauseLabel, Integer.valueOf(3));


        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setForeground(Color.BLUE);
        volumeSlider.setBackground(Color.BLUE);
        int vsX = 1784;
        int vsY = 1005; // Смещение на 100 пикселей ниже
        int vsW = 100;
        int vsH = 20;
        volumeSlider.setBounds(vsX, vsY, vsW, vsH);

        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int volumeValue = volumeSlider.getValue();
                float volume = volumeValue / 100.0f;
                newMusicPlayer.setVolume(volume);
            }
        });
        layeredPane.add(volumeSlider, Integer.valueOf(3));



        ImageIcon randomPIcon = new ImageIcon("resources/randomP.png");
        JLabel randomPLabel = new JLabel(randomPIcon);
        int rPX = 80;
        int rPY = 240; // Смещение на 100 пикселей ниже
        randomPLabel.setBounds(rPX, rPY, randomPIcon.getIconWidth(), randomPIcon.getIconHeight());

        randomPLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Если кнопка уже была нажата, отмените выделение
                if (buttonClicked) {
                    randomPLabel.setIcon(randomPIcon);
                    buttonClicked = false;
                } else {
                    buttonClicked = true;
                    // Ваш код для обработки нажатия на кнопку (если необходимо)
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                randomPLabel.setIcon(new ImageIcon("resources/randomPUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!buttonClicked) {
                    randomPLabel.setIcon(randomPIcon);
                }
            }
        });
        layeredPane.add(randomPLabel, Integer.valueOf(1)); // Добавляем на передний слой



        ImageIcon minMaxPIcon = new ImageIcon("resources/minmaxP.png");
        JLabel minMaxPLabel = new JLabel(minMaxPIcon);
        int mipX = 80;
        int mipY = 280; // Смещение на 100 пикселей ниже
        minMaxPLabel.setBounds(mipX, mipY, minMaxPIcon.getIconWidth(), minMaxPIcon.getIconHeight());

        minMaxPLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Если кнопка уже была нажата, отмените выделение
                if (buttonClicked) {
                    minMaxPLabel.setIcon(minMaxPIcon);
                    buttonClicked = false;
                } else {
                    buttonClicked = true;
                    // Ваш код для обработки нажатия на кнопку (если необходимо)
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                minMaxPLabel.setIcon(new ImageIcon("resources/minmaxPUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!buttonClicked) {
                    minMaxPLabel.setIcon(minMaxPIcon);
                }
            }
        });
        layeredPane.add(minMaxPLabel, Integer.valueOf(1)); // Добавляем на передний слой



        ImageIcon expectMaxPIcon = new ImageIcon("resources/expectimaxP.png");
        JLabel expectMaxPLabel = new JLabel(expectMaxPIcon);
        int expX = 80;
        int expY = 320; // Смещение на 100 пикселей ниже
        expectMaxPLabel.setBounds(expX, expY, expectMaxPIcon.getIconWidth(), expectMaxPIcon.getIconHeight());

        expectMaxPLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Если кнопка уже была нажата, отмените выделение
                if (buttonClicked) {
                    expectMaxPLabel.setIcon(expectMaxPIcon);
                    buttonClicked = false;
                } else {
                    buttonClicked = true;
                    // Ваш код для обработки нажатия на кнопку (если необходимо)
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                expectMaxPLabel.setIcon(new ImageIcon("resources/expectimaxPUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!buttonClicked) {
                    expectMaxPLabel.setIcon(expectMaxPIcon);
                }
            }
        });
        layeredPane.add(expectMaxPLabel, Integer.valueOf(1)); // Добавляем на передний слой


        ImageIcon parallelMaxPIcon = new ImageIcon("resources/parallmaxP.png");
        JLabel parallelMaxPLabel = new JLabel(parallelMaxPIcon);
        int ppX = 80;
        int ppY = 360; // Смещение на 100 пикселей ниже
        parallelMaxPLabel.setBounds(ppX, ppY, parallelMaxPIcon.getIconWidth(), parallelMaxPIcon.getIconHeight());

        parallelMaxPLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Если кнопка уже была нажата, отмените выделение
                if (buttonClicked) {
                    parallelMaxPLabel.setIcon(parallelMaxPIcon);
                    buttonClicked = false;
                } else {
                    buttonClicked = true;
                    // Ваш код для обработки нажатия на кнопку (если необходимо)
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                parallelMaxPLabel.setIcon(new ImageIcon("resources/parallmaxPUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!buttonClicked) {
                    parallelMaxPLabel.setIcon(parallelMaxPIcon);
                }
            }
        });
        layeredPane.add(parallelMaxPLabel, Integer.valueOf(1)); // Добавляем на передний слой


        ImageIcon megaMaxPIcon = new ImageIcon("resources/megamaxP.png");
        JLabel megaMaxPLabel = new JLabel(megaMaxPIcon);
        int mmpX = 80;
        int mmpY = 400; // Смещение на 100 пикселей ниже
        megaMaxPLabel.setBounds(mmpX, mmpY, megaMaxPIcon.getIconWidth(), megaMaxPIcon.getIconHeight());

        megaMaxPLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Если кнопка уже была нажата, отмените выделение
                if (buttonClicked) {
                    megaMaxPLabel.setIcon(megaMaxPIcon);
                    buttonClicked = false;
                } else {
                    buttonClicked = true;
                    // Ваш код для обработки нажатия на кнопку (если необходимо)
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                megaMaxPLabel.setIcon(new ImageIcon("resources/megamaxPUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!buttonClicked) {
                    megaMaxPLabel.setIcon(megaMaxPIcon);
                }
            }
        });
        layeredPane.add(megaMaxPLabel, Integer.valueOf(1)); // Добавляем на передний слой


        ImageIcon monteCarloPIcon = new ImageIcon("resources/montecarloP.png");
        JLabel monteCarloPLabel = new JLabel(monteCarloPIcon);
        int mcpX = 80;
        int mcpY = 440; // Смещение на 100 пикселей ниже
        monteCarloPLabel.setBounds(mcpX, mcpY, monteCarloPIcon.getIconWidth(), monteCarloPIcon.getIconHeight());

        monteCarloPLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Если кнопка уже была нажата, отмените выделение
                if (buttonClicked) {
                    monteCarloPLabel.setIcon(monteCarloPIcon);
                    buttonClicked = false;
                } else {
                    buttonClicked = true;
                    // Ваш код для обработки нажатия на кнопку (если необходимо)
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                monteCarloPLabel.setIcon(new ImageIcon("resources/montecarloPUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!buttonClicked) {
                    monteCarloPLabel.setIcon(monteCarloPIcon);
                }
            }
        });
        layeredPane.add(monteCarloPLabel, Integer.valueOf(1)); // Добавляем на передний слой




        ImageIcon randomBIcon = new ImageIcon("resources/randomB.png");
        JLabel randomBLabel = new JLabel(randomBIcon);
        int rBX = 320;
        int rBY = 240; // Смещение на 100 пикселей ниже
        randomBLabel.setBounds(rBX, rBY, randomBIcon.getIconWidth(), randomBIcon.getIconHeight());

        randomBLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Если кнопка уже была нажата, отмените выделение
                if (buttonClicked) {
                    randomBLabel.setIcon(randomBIcon);
                    buttonClicked = false;
                } else {
                    buttonClicked = true;
                    // Ваш код для обработки нажатия на кнопку (если необходимо)
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                randomBLabel.setIcon(new ImageIcon("resources/randomBUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!buttonClicked) {
                    randomBLabel.setIcon(randomBIcon);
                }
            }
        });
        layeredPane.add(randomBLabel, Integer.valueOf(1)); // Добавляем на передний слой


        ImageIcon minMaxBIcon = new ImageIcon("resources/minmaxB.png");
        JLabel minMaxBLabel = new JLabel(minMaxBIcon);
        int miBX = 320;
        int miBY = 280; // Смещение на 100 пикселей ниже
        minMaxBLabel.setBounds(miBX, miBY, minMaxBIcon.getIconWidth(), minMaxBIcon.getIconHeight());

        minMaxBLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Если кнопка уже была нажата, отмените выделение
                if (buttonClicked) {
                    minMaxBLabel.setIcon(minMaxBIcon);
                    buttonClicked = false;
                } else {
                    buttonClicked = true;
                    // Ваш код для обработки нажатия на кнопку (если необходимо)
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                minMaxBLabel.setIcon(new ImageIcon("resources/minmaxBUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!buttonClicked) {
                    minMaxBLabel.setIcon(minMaxBIcon);
                }
            }
        });
        layeredPane.add(minMaxBLabel, Integer.valueOf(1)); // Добавляем на передний слой



        ImageIcon expectMaxBIcon = new ImageIcon("resources/expectimaxB.png");
        JLabel expectMaxBLabel = new JLabel(expectMaxBIcon);
        int exbX = 320;
        int exbY = 320; // Смещение на 100 пикселей ниже
        expectMaxBLabel.setBounds(exbX, exbY, expectMaxBIcon.getIconWidth(), expectMaxBIcon.getIconHeight());

        expectMaxBLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Если кнопка уже была нажата, отмените выделение
                if (buttonClicked) {
                    expectMaxBLabel.setIcon(expectMaxBIcon);
                    buttonClicked = false;
                } else {
                    buttonClicked = true;
                    // Ваш код для обработки нажатия на кнопку (если необходимо)
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                expectMaxBLabel.setIcon(new ImageIcon("resources/expectimaxBUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!buttonClicked) {
                    expectMaxBLabel.setIcon(expectMaxBIcon);
                }
            }
        });
        layeredPane.add(expectMaxBLabel, Integer.valueOf(1)); // Добавляем на передний слой


        ImageIcon parallelMaxBIcon = new ImageIcon("resources/parallmaxB.png");
        JLabel parallelMaxPLaBel = new JLabel(parallelMaxBIcon);
        int pbX = 320;
        int pbY = 360; // Смещение на 100 пикселей ниже
        parallelMaxPLaBel.setBounds(pbX, pbY, parallelMaxBIcon.getIconWidth(), parallelMaxBIcon.getIconHeight());

        parallelMaxPLaBel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Если кнопка уже была нажата, отмените выделение
                if (buttonClicked) {
                    parallelMaxPLaBel.setIcon(parallelMaxBIcon);
                    buttonClicked = false;
                } else {
                    buttonClicked = true;
                    // Ваш код для обработки нажатия на кнопку (если необходимо)
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                parallelMaxPLaBel.setIcon(new ImageIcon("resources/parallmaxBUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!buttonClicked) {
                    parallelMaxPLaBel.setIcon(parallelMaxBIcon);
                }
            }
        });
        layeredPane.add(parallelMaxPLaBel, Integer.valueOf(1)); // Добавляем на передний слой


        ImageIcon megaMaxBIcon = new ImageIcon("resources/megamaxB.png");
        JLabel megaMaxBLabel = new JLabel(megaMaxBIcon);
        int mmbX = 320;
        int mmbY = 400; // Смещение на 100 пикселей ниже
        megaMaxBLabel.setBounds(mmbX, mmbY, megaMaxBIcon.getIconWidth(), megaMaxBIcon.getIconHeight());

        megaMaxBLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Если кнопка уже была нажата, отмените выделение
                if (buttonClicked) {
                    megaMaxBLabel.setIcon(megaMaxBIcon);
                    buttonClicked = false;
                } else {
                    buttonClicked = true;
                    // Ваш код для обработки нажатия на кнопку (если необходимо)
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                megaMaxBLabel.setIcon(new ImageIcon("resources/megamaxBUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!buttonClicked) {
                    megaMaxBLabel.setIcon(megaMaxBIcon);
                }
            }
        });
        layeredPane.add(megaMaxBLabel, Integer.valueOf(1)); // Добавляем на передний слой


        ImageIcon monteCarloBIcon = new ImageIcon("resources/montecarloB.png");
        JLabel monteCarloBLabel = new JLabel(monteCarloBIcon);
        int mcbX = 320;
        int mcbY = 440; // Смещение на 100 пикселей ниже
        monteCarloBLabel.setBounds(mcbX, mcbY, monteCarloBIcon.getIconWidth(), monteCarloBIcon.getIconHeight());

        monteCarloBLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Если кнопка уже была нажата, отмените выделение
                if (buttonClicked) {
                    monteCarloBLabel.setIcon(monteCarloBIcon);
                    buttonClicked = false;
                } else {
                    buttonClicked = true;
                    // Ваш код для обработки нажатия на кнопку (если необходимо)
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                monteCarloBLabel.setIcon(new ImageIcon("resources/montecarloBUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!buttonClicked) {
                    monteCarloBLabel.setIcon(monteCarloBIcon);
                }
            }
        });
        layeredPane.add(monteCarloBLabel, Integer.valueOf(1)); // Добавляем на передний слой















        // Создайте изображение с текстом, которое вы хотите добавить
        ImageIcon NewGameIcon = new ImageIcon("resources/newgame.png");
        // Создайте JLabel с изображением
        JLabel NewGameLabel = new JLabel(NewGameIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int ngX = 845;
        int ngY = 180; // Смещение на 100 пикселей ниже
        NewGameLabel.setBounds(ngX, ngY, NewGameIcon.getIconWidth(), NewGameIcon.getIconHeight());

        // Сделайте JLabel интерактивным (как кнопку)
        NewGameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();
                // Здесь вы можете добавить код для обработки события щелчка на изображении
                MainGameWindow gameWindow = new MainGameWindow(clientGui);
                gameWindow.setVisible(true);
                // Закрыть текущее окно "Main Menu"
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                // Здесь вы можете добавить код для подсветки кнопки при наведении
                NewGameLabel.setIcon(new ImageIcon("resources/newgameUP.png"));
                // Воспроизведите звук клика
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Здесь вы можете добавить код для возврата кнопки к обычному состоянию
                NewGameLabel.setIcon(NewGameIcon);
            }
        });
        layeredPane.add(NewGameLabel, Integer.valueOf(1)); // Добавляем на передний слой

        // Создайте изображение с текстом, которое вы хотите добавить
        ImageIcon BackIcon = new ImageIcon("resources/backg.png");
        // Создайте JLabel с изображением
        JLabel BackLabel = new JLabel(BackIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int backX = 700;
        int backY = 818; // Смещение на 100 пикселей ниже
        BackLabel.setBounds(backX, backY, BackIcon.getIconWidth(), BackIcon.getIconHeight());

        // Сделайте JLabel интерактивным (как кнопку)
        BackLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();
                // Здесь вы можете добавить код для обработки события щелчка на изображении
                // Закрыть текущее окно "Main Menu"
                LocalGameWindow localGameWindow = new LocalGameWindow(clientGui);
                localGameWindow.setVisible(true);
                LocalGameWindow.newMusicPlayer.stop();

                MainGameWindow.musicPlayer = new MusicPlayer("resources/retro.wav");
                MainGameWindow.musicPlayer.play();
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                // Здесь вы можете добавить код для подсветки кнопки при наведении
                BackLabel.setIcon(new ImageIcon("resources/backgUP.png"));
                // Воспроизведите звук клика
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Здесь вы можете добавить код для возврата кнопки к обычному состоянию
                BackLabel.setIcon(BackIcon);
            }
        });
        layeredPane.add(BackLabel, Integer.valueOf(1)); // Добавляем на передний слой

        // Создаем доску 8x8
        int boardSize = 8;
        buttons = new JButton[boardSize][boardSize];
        int buttonSize = 71; // Размер каждой кнопки
        int boardWidth = boardSize * buttonSize;
        int boardHeight = boardSize * buttonSize;
        int boardX = (screenSize.width - boardWidth) / 2 - 11;
        int boardY = (screenSize.height - boardHeight) / 2 - 12;


        ImageIcon buttonNormalIcon = new ImageIcon("resources/button_normal.png");
        ImageIcon buttonHoverIcon = new ImageIcon("resources/button_hover.png");
        ImageIcon buttonPressedIcon = new ImageIcon("resources/button_pressed.png");


        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                JButton button = new JButton();
                button.setBounds(boardX + col * buttonSize, boardY + row * buttonSize, buttonSize, buttonSize);

                button.setRolloverIcon(buttonHoverIcon); // Наведение
                button.setPressedIcon(buttonPressedIcon); // Нажатие
                buttons[row][col] = button;
                layeredPane.add(button, Integer.valueOf(1)); // Добавляем на передний слой
            }
        }


        botBlack = new AIBot(Cell.BLACK, 5);
        botWhite = new Player.BotPlayer(Cell.WHITE);
        //сделать кнопки и поля для ввода настроек ботов, добавить то, что ниже в ActionListener кнопки старт игры ботов

        board = new Board();
        startBoardPosition();
        setVisible(true);

        while (!board.getAllAvailableMoves(botBlack.playerCell).isEmpty() || !board.getAllAvailableMoves(botWhite.playerCell).isEmpty()) {
            if (!board.getAllAvailableMoves(botBlack.playerCell).isEmpty()) {
                Move move = botBlack.makeMove(board.getBoardCopy());
                board.placePiece(move.row, move.col, botBlack.playerCell);
                updateBoardColors(board, BOARD_SIZE, buttons);
            }
            if (!board.getAllAvailableMoves(botWhite.playerCell).isEmpty()) {
                Move move = botWhite.makeMove(board.getBoardCopy());
                board.placePiece(move.row, move.col, botWhite.playerCell);
                updateBoardColors(board, BOARD_SIZE, buttons);
            }
        }
        JOptionPane.showMessageDialog(this, getWinnerString());
    }


    private void startBoardPosition() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                buttons[row][col].setBackground(null);
            }
        }
        buttons[3][3].setBackground(Color.white);
        buttons[4][4].setBackground(Color.white);
        buttons[3][4].setBackground(Color.black);
        buttons[4][3].setBackground(Color.black);
        buttons[2][3].setBackground(Color.GRAY);
        buttons[3][2].setBackground(Color.GRAY);
        buttons[4][5].setBackground(Color.GRAY);
        buttons[5][4].setBackground(Color.GRAY);
    }



    private String getWinnerString() {
        final int blackCount = board.getQuantityOfBlack();
        final int whiteCount = board.getQuantityOfWhite();
        String winner;
        if (blackCount > whiteCount) {
            winner = "Winner is black. Total score is ";

        } else if (whiteCount > blackCount) {
            winner = "Winner is white. Total score is ";

        } else {
            winner = "It's tie. Total score is ";
        }
        winner += blackCount + " - " + whiteCount;
        return winner;
    }
    public static void updateBoardColors(Board board, int board_size, JButton[][] buttons) {
        for (int row = 0; row < board_size; row++) {
            for (int col = 0; col < board_size; col++) {
                if (board.get(row, col).equals(Cell.BLACK)) {
                    buttons[row][col].setBackground(purple);
                } else if (board.get(row, col).equals(Cell.WHITE)) {
                    buttons[row][col].setBackground(blue);
                } else {
                    buttons[row][col].setBackground(null);
                }
            }
        }
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

