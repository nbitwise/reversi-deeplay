package gui;

import guiClient.clientGui;
import logic.*;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

//import static Replayer.Replayer.updateBoardColors;
import static gui.LocalGameBotVsBot.updateBoardColors;
import static gui.LocalGameWindow.newMusicPlayer;

public class LocalGameVsBot extends JFrame {
    private final int BOARD_SIZE = 8;
    private final JButton[][] buttons;
    private Board board;
    private ArrayList<PlayerPanel> playerList;
    Color liteBlue = new Color(147, 227, 255);
    Color pink = new Color(234, 147, 255);
    Color purple = new Color(132, 2, 182);
    Color blue = new Color(27, 61, 182);
    Cell playerColor = Cell.WHITE;
    Player bot;

    private ImageIcon playIcon;
    private ImageIcon pauseIcon;
    private boolean isPaused = false;

    public LocalGameVsBot(clientGui clientGui) {
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
        int sbX = 130;
        int sbY = 130; // Смещение на 100 пикселей ниже
        selectBotLabel.setBounds(sbX, sbY, selectBotIcon.getIconWidth(), selectBotIcon.getIconHeight());
        layeredPane.add(selectBotLabel, Integer.valueOf(1));

        ImageIcon colorIcon = new ImageIcon("resources/color.png");
        // Создайте JLabel с изображением
        JLabel colorLabel = new JLabel(colorIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int ccX = 100;
        int ccY = 490; // Смещение на 100 пикселей ниже
        colorLabel.setBounds(ccX, ccY, colorIcon.getIconWidth(), colorIcon.getIconHeight());
        layeredPane.add(colorLabel, Integer.valueOf(1));

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


        ImageIcon setPurpleIcon = new ImageIcon("resources/setPurple.png");
        // Создайте JLabel с изображением
        JLabel setPurpleLabel = new JLabel(setPurpleIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int sPX = 180;
        int sPY = 550; // Смещение на 100 пикселей ниже
        setPurpleLabel.setBounds(sPX, sPY, setPurpleIcon.getIconWidth(), setPurpleIcon.getIconHeight());

        // Сделайте JLabel интерактивным (как кнопку)
        setPurpleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                // Здесь вы можете добавить код для подсветки кнопки при наведении
                setPurpleLabel.setIcon(new ImageIcon("resources/setPurpleUP.png"));
                // Воспроизведите звук клика
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Здесь вы можете добавить код для возврата кнопки к обычному состоянию
                setPurpleLabel.setIcon(setPurpleIcon);
            }
        });
        layeredPane.add(setPurpleLabel, Integer.valueOf(1)); // Добавляем на передний слой


        ImageIcon setBlueIcon = new ImageIcon("resources/setBlue.png");
        // Создайте JLabel с изображением
        JLabel setBlueLabel = new JLabel(setBlueIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int sBX = 194;
        int sBY = 590; // Смещение на 100 пикселей ниже
        setBlueLabel.setBounds(sBX, sBY, setBlueIcon.getIconWidth(), setBlueIcon.getIconHeight());

        // Сделайте JLabel интерактивным (как кнопку)
        setBlueLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                // Здесь вы можете добавить код для подсветки кнопки при наведении
                setBlueLabel.setIcon(new ImageIcon("resources/setBlueUP.png"));
                // Воспроизведите звук клика
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Здесь вы можете добавить код для возврата кнопки к обычному состоянию
                setBlueLabel.setIcon(setBlueIcon);
            }
        });
        layeredPane.add(setBlueLabel, Integer.valueOf(1)); // Добавляем на передний слой







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
        ImageIcon SurrenderIcon = new ImageIcon("resources/surrender.png");
        // Создайте JLabel с изображением
        JLabel SurrenderLabel = new JLabel(SurrenderIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int surX = 1020;
        int surY = 818; // Смещение на 100 пикселей ниже
        SurrenderLabel.setBounds(surX, surY, SurrenderIcon.getIconWidth(), SurrenderIcon.getIconHeight());

        // Сделайте JLabel интерактивным (как кнопку)
        SurrenderLabel.addMouseListener(new MouseAdapter() {
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
                SurrenderLabel.setIcon(new ImageIcon("resources/surrenderUP.png"));
                // Воспроизведите звук клика
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Здесь вы можете добавить код для возврата кнопки к обычному состоянию
                SurrenderLabel.setIcon(SurrenderIcon);
            }
        });
        layeredPane.add(SurrenderLabel, Integer.valueOf(1)); // Добавляем на передний слой


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


        ImageIcon easy2xIcon = new ImageIcon("resources/easy2x.png");
        JLabel easy2xLabel = new JLabel(easy2xIcon);
        int exX = 185;
        int exY = 200; // Смещение на 100 пикселей ниже
        easy2xLabel.setBounds(exX, exY, easy2xIcon.getIconWidth(), easy2xIcon.getIconHeight());

        easy2xLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();


            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                easy2xLabel.setIcon(new ImageIcon("resources/easy2xUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                easy2xLabel.setIcon(easy2xIcon);
            }
        });
        layeredPane.add(easy2xLabel, Integer.valueOf(1)); // Добавляем на передний слой


        ImageIcon easyIcon = new ImageIcon("resources/easy.png");
        JLabel easyLabel = new JLabel(easyIcon);
        int eX = 195;
        int eY = 246; // Смещение на 100 пикселей ниже
        easyLabel.setBounds(eX, eY, easyIcon.getIconWidth(), easy2xIcon.getIconHeight());

        easyLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();


            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                easyLabel.setIcon(new ImageIcon("resources/easyUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                easyLabel.setIcon(easyIcon);
            }
        });
        layeredPane.add(easyLabel, Integer.valueOf(1)); // Добавляем на передний слой


        ImageIcon mediumIcon = new ImageIcon("resources/medium.png");
        JLabel mediumLabel = new JLabel(mediumIcon);
        int mX = 192;
        int mY = 300; // Смещение на 100 пикселей ниже
        mediumLabel.setBounds(mX, mY, mediumIcon.getIconWidth(), mediumIcon.getIconHeight());

        mediumLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();


            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                mediumLabel.setIcon(new ImageIcon("resources/mediumUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mediumLabel.setIcon(mediumIcon);
            }
        });
        layeredPane.add(mediumLabel, Integer.valueOf(1)); // Добавляем на передний слой

        ImageIcon hardIcon = new ImageIcon("resources/hard.png");
        JLabel hardLabel = new JLabel(hardIcon);
        int hX = 196;
        int hY = 347; // Смещение на 100 пикселей ниже
        hardLabel.setBounds(hX, hY, hardIcon.getIconWidth(), hardIcon.getIconHeight());

        hardLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();


            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                hardLabel.setIcon(new ImageIcon("resources/hardUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hardLabel.setIcon(hardIcon);
            }
        });
        layeredPane.add(hardLabel, Integer.valueOf(1)); // Добавляем на передний слой

        ImageIcon killerIcon = new ImageIcon("resources/killer.png");
        JLabel killerLabel = new JLabel(killerIcon);
        int kX = 190;
        int kY = 398; // Смещение на 100 пикселей ниже
        killerLabel.setBounds(kX, kY, killerIcon.getIconWidth(), killerIcon.getIconHeight());

        killerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();


            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                killerLabel.setIcon(new ImageIcon("resources/killerUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                killerLabel.setIcon(killerIcon);
            }
        });
        layeredPane.add(killerLabel, Integer.valueOf(1)); // Добавляем на передний слой


        ImageIcon murdererIcon = new ImageIcon("resources/murder.png");
        JLabel murdererLabel = new JLabel(murdererIcon);
        int mrX = 165;
        int mrY = 442; // Смещение на 100 пикселей ниже
        murdererLabel.setBounds(mrX, mrY, murdererIcon.getIconWidth(), murdererIcon.getIconHeight());

        murdererLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();


            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                murdererLabel.setIcon(new ImageIcon("resources/murderUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                murdererLabel.setIcon(murdererIcon);
            }
        });
        layeredPane.add(murdererLabel, Integer.valueOf(2)); // Добавляем на передний слой



        // Создаем доску 8x8
        int boardSize = 8;
        buttons = new JButton[boardSize][boardSize];
        int buttonSize = 71; // Размер каждой кнопки
        int boardWidth = boardSize * buttonSize;
        int boardHeight = boardSize * buttonSize;
        int boardX = (screenSize.width - boardWidth) / 2 - 11;
        int boardY = (screenSize.height - boardHeight) / 2 - 12;

        Color buttonsBackground = new Color(250, 98, 236);

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
                button.addActionListener(new BoardButtonListener(row, col, this)); // Добавляем обработчик события
                layeredPane.add(button, Integer.valueOf(1)); // Добавляем на передний слой
            }
        }
        board = new Board();
        startBoardPosition();


        bot = new AIBot(playerColor.reverse(), 2);

        if (playerColor.equals(Cell.WHITE)) {

            Move move = bot.makeMove(board.getBoardCopy());
            board.placePiece(move.row, move.col, playerColor.reverse());
            updateBoardColors(board, BOARD_SIZE, buttons);
            for (Move m : board.getAllAvailableMoves(playerColor)) {
                buttons[m.row][m.col].setBackground(liteBlue);
            }
        }
        setVisible(true);
    }


    private void startBoardPosition() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                buttons[row][col].setBackground(null);
            }
        }
        buttons[3][3].setBackground(blue);
        buttons[4][4].setBackground(blue);
        buttons[3][4].setBackground(purple);
        buttons[4][3].setBackground(purple);
        buttons[2][3].setBackground(liteBlue);
        buttons[3][2].setBackground(liteBlue);
        buttons[4][5].setBackground(liteBlue);
        buttons[5][4].setBackground(liteBlue);
    }


    private class BoardButtonListener implements ActionListener {
        private final int row;
        private final int col;

        private LocalGameVsBot lgvb;

        public BoardButtonListener(int row, int col, LocalGameVsBot lgvb) {
            this.row = row;
            this.col = col;
            this.lgvb = lgvb;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!buttons[row][col].getBackground().equals(liteBlue)) {
                return;
            } else {
                board.placePiece(row, col, playerColor);
                updateBoardColors(board, BOARD_SIZE, buttons);

                while (true) {
                    Move move = bot.makeMove(board);
                    board.placePiece(move.row, move.col, playerColor.reverse());
                    updateBoardColors(board, BOARD_SIZE, buttons);
                    List<Move> availableMoves = board.getAllAvailableMoves(playerColor);
                    if (!availableMoves.isEmpty()) {
                        for (Move m : availableMoves) {
                            buttons[m.row][m.col].setBackground(liteBlue);
                        }
                        break;
                    }
                    if ((board.getAllAvailableMoves(Cell.WHITE).isEmpty() &&
                            board.getAllAvailableMoves(Cell.BLACK).isEmpty())) {
                        //игра закончена результаты
                        String winner = getWinnerString();
                        JOptionPane.showMessageDialog(lgvb, winner);
                        return;
                    }
                }
            }
            if ((board.getAllAvailableMoves(Cell.WHITE).isEmpty() &&
                    board.getAllAvailableMoves(Cell.BLACK).isEmpty())) {
                //игра закончена результаты
                String winner = getWinnerString();
                JOptionPane.showMessageDialog(lgvb, winner);
            }
        }
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
