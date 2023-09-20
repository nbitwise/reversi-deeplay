package gui;

import guiClient.clientGui;
import logic.Board;
import logic.Cell;
import logic.Move;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static gui.LocalGameBotVsBot.updateBoardColors;
import static gui.LocalGameWindow.newMusicPlayer;

//import static Replayer.Replayer.updateBoardColors;

public class LocalGameVsHuman extends JFrame {
    private final int BOARD_SIZE = 8;
    private final JButton[][] buttons;
    ImageIcon blackPieceIcon = new ImageIcon("resources/blackPiece.png");
    ImageIcon whitePieceIcon = new ImageIcon("resources/whitePiece.png");
    private Board board;
    Color liteBlue = new Color(147, 227, 255);
    Color pink = new Color(234, 147, 255);
    Color purple = new Color(132, 2, 182);
    Color blue = new Color(27, 61, 182);
    private ArrayList<PlayerPanel> playerList;
    private ImageIcon playIcon;
    private ImageIcon pauseIcon;
    private boolean isPaused = false;
    Cell currentPlayerColor = Cell.BLACK;
    private JTextArea movesTextArea; // Компонент для отображения ходов игроков
    private JScrollPane scrollPane;
    private Font customFont;
    private Font playerFont;
    ImageIcon purplePlayerIcon = new ImageIcon("resources/purplemove.png");
    ImageIcon bluePlayerIcon = new ImageIcon("resources/bluemove.png");
    int num = 1;

    public LocalGameVsHuman(clientGui clientGui) {
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


        JLabel currentPlayerLabel = new JLabel();


        // START изменений


// Установите начальную иконку (например, фиолетового игрока)
        currentPlayerLabel.setIcon(purplePlayerIcon);
        int cplX = 845;
        int cplY = 805;
        currentPlayerLabel.setBounds(cplX, cplY, purplePlayerIcon.getIconWidth(), purplePlayerIcon.getIconHeight());
// Добавьте JLabel в вашу панель
        layeredPane.add(currentPlayerLabel, Integer.valueOf(3));

// Где-то в вашем коде, когда сменяется ход игроков, выполните следующее:
// Переключите иконку в зависимости от текущего игрока
        if (currentPlayerColor == Cell.BLACK) {
            currentPlayerLabel.setIcon(bluePlayerIcon); // Текущий игрок - синий
        } else {
            currentPlayerLabel.setIcon(purplePlayerIcon); // Текущий игрок - фиолетовый
        }

        try {
            playerFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/Visitor Rus.ttf"));
            playerFont = playerFont.deriveFont(Font.BOLD, 22);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/Visitor Rus.ttf"));
            customFont = customFont.deriveFont(Font.BOLD | Font.ITALIC, 14);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        ImageIcon frameIcon = new ImageIcon("resources/frame.gif");
        // Создайте JLabel с изображением
        JLabel frameLabel = new JLabel(frameIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int fX = 1380;
        int fY = 60; // Смещение на 100 пикселей ниже
        frameLabel.setBounds(fX, fY, frameIcon.getIconWidth(), frameIcon.getIconHeight());
        layeredPane.add(frameLabel, Integer.valueOf(2));

        ImageIcon playersIcon = new ImageIcon("resources/players.gif");
        // Создайте JLabel с изображением
        JLabel playersLabel = new JLabel(playersIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int plX = 1570;
        int plY = 60; // Смещение на 100 пикселей ниже
        playersLabel.setBounds(plX, plY, playersIcon.getIconWidth(), playersIcon.getIconHeight());
        layeredPane.add(playersLabel, Integer.valueOf(3));


        //IGROKI I ID
        ImageIcon purpleIcon = new ImageIcon("resources/purple.png");
        // Создайте JLabel с изображением
        JLabel purpleLabel = new JLabel(purpleIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int pX = 1410;
        int pY = 100; // Смещение на 100 пикселей ниже
        purpleLabel.setBounds(pX, pY, purpleIcon.getIconWidth(), purpleIcon.getIconHeight());
        layeredPane.add(purpleLabel, Integer.valueOf(3));


        JLabel purpleInfoLabel = new JLabel("Player 1: Purple");
        purpleInfoLabel.setFont(playerFont); // Установите шрифт
        purpleInfoLabel.setForeground(Color.magenta);
// Установите координаты для текстовой метки (сделайте справа от соответствующего лейбла)
        int infoX = pX + purpleIcon.getIconWidth(); // Примерное смещение
        int infoY = pY - 4; // Оставьте на том же уровне, что и исходный лейбл
        purpleInfoLabel.setBounds(infoX, infoY, 200, 30); // Установите размер и положение
        layeredPane.add(purpleInfoLabel, Integer.valueOf(3));


        ImageIcon blueIcon = new ImageIcon("resources/blue.png");
        // Создайте JLabel с изображением
        JLabel blueLabel = new JLabel(blueIcon);
        // Установите координаты для JLabel (чтобы оно было по центру экрана)
        int blX = 1660;
        int blY = 105; // Смещение на 100 пикселей ниже
        blueLabel.setBounds(blX, blY, blueIcon.getIconWidth(), blueIcon.getIconHeight());
        layeredPane.add(blueLabel, Integer.valueOf(3));

        JLabel blueInfoLabel = new JLabel("Player 2: Blue");
        blueInfoLabel.setFont(playerFont); // Установите шрифт
        blueInfoLabel.setForeground(Color.blue);
        int infoX2 = blX + blueIcon.getIconWidth() + 10; // Примерное смещение
        int infoY2 = blY - 9;
        blueInfoLabel.setBounds(infoX2, infoY2, 200, 30); // Установите размер и положение
        layeredPane.add(blueInfoLabel, Integer.valueOf(3));


//ПАНЕЛЬ ХОДА iГРОКОВ
        movesTextArea = new JTextArea();
        movesTextArea.setEditable(true);
        movesTextArea.setLineWrap(true); // Переносить текст на новую строку, если он не умещается
        movesTextArea.setWrapStyleWord(true); // Переносить текст по словам


        movesTextArea.setFont(customFont); // Устанавливаем кастомный шрифт
        //movesTextArea.setForeground(Color.white);
        // Создание JScrollPane для movesTextArea
        scrollPane = new JScrollPane(movesTextArea);
        scrollPane.setBounds(1410, 130, 440, 400); // Установите координаты и размер
//        scrollPane.setOpaque(false); // Делаем JScrollPane прозрачным
//        scrollPane.getViewport().setOpaque(false);
        layeredPane.add(scrollPane, Integer.valueOf(3)); // Добавляем на передний слой


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
                button.addActionListener(new BoardButtonListener(row, col, this)); // Добавляем обработчик события
                layeredPane.add(button, Integer.valueOf(1)); // Добавляем на передний слой
            }
        }
        board = new Board();
        startBoardPosition();

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

        private LocalGameVsHuman lgvh;

        public BoardButtonListener(int row, int col, LocalGameVsHuman lgvh) {
            this.row = row;
            this.col = col;
            this.lgvh = lgvh;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!buttons[row][col].getBackground().equals(liteBlue)) {
                return;
            } else {
                board.placePiece(row, col, currentPlayerColor);
                movesTextArea.setText(movesTextArea.getText() + num++ + ". Player " + currentPlayerColor +
                        " placed his piece on " + (row + 1) + " " + (col + 1) + "\n");
                updateBoardColors(board, BOARD_SIZE, buttons);
                Cell opponent = currentPlayerColor == Cell.BLACK ? Cell.WHITE : Cell.BLACK;
                if (!board.getAllAvailableMoves(opponent).isEmpty()) {
                    currentPlayerColor = opponent;
                }
                final List<Move> availableMoves = board.getAllAvailableMoves(currentPlayerColor);
                for (Move m : availableMoves) {
                    buttons[m.row][m.col].setBackground(liteBlue);
                }
            }
            if ((board.getAllAvailableMoves(Cell.WHITE).isEmpty() &&
                    board.getAllAvailableMoves(Cell.BLACK).isEmpty())) {
                //игра закончена результаты
                String winner = getWinnerString();
                JOptionPane.showMessageDialog(lgvh, winner);
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
