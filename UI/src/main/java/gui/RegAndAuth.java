package gui;

import guiClient.clientGui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import static guiClient.SendMethods.createJsonAndSendCommand;

public class RegAndAuth extends JFrame {

    public JLabel registrationLabelError;
    JTextField registrationText;
    public JLabel authorizationLabelError;
    JTextField authorizationText;
    private Font customFont;
    public RegAndAuth(clientGui clientGui) {

        super("Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/Realpolitik3D.otf"));
            customFont = customFont.deriveFont(Font.BOLD | Font.ITALIC , 24);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBackground(Color.BLACK); // Устанавливаем черный цвет фона
        setContentPane(layeredPane);

        JLabel gifLabel = new JLabel(new ImageIcon("resources/game.gif"));
        gifLabel.setBounds(0, 0, screenSize.width, screenSize.height);
        layeredPane.add(gifLabel, Integer.valueOf(0)); // Добавляем на задний слой


        ImageIcon RegistrationIcon = new ImageIcon("resources/REG.png");
        JLabel RegistrationLabel = new JLabel(RegistrationIcon);
        int rX = 1190;
        int rY = 385; // Смещение на 100 пикселей ниже
        RegistrationLabel.setBounds(rX, rY, RegistrationIcon.getIconWidth(), RegistrationIcon.getIconHeight());

        RegistrationLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();
                try {
                    createJsonAndSendCommand(clientGui, "REGISTRATION " + registrationText.getText());
                } catch (IOException event) {
                    throw new RuntimeException(event);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                RegistrationLabel.setIcon(new ImageIcon("resources/REGUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                RegistrationLabel.setIcon(RegistrationIcon);
            }
        });
        layeredPane.add(RegistrationLabel, Integer.valueOf(1)); // Добавляем на передний слой

        ImageIcon RegInfoIcon = new ImageIcon("resources/register.png");
        JLabel RegInfoLabel = new JLabel(RegInfoIcon);
        int riX = 500;
        int riY = 320; // Смещение на 100 пикселей ниже
        RegInfoLabel.setBounds(riX, riY, RegInfoIcon.getIconWidth(), RegInfoIcon.getIconHeight());
        layeredPane.add(RegInfoLabel, Integer.valueOf(1)); // Добавляем на передний слой

        registrationText = new JTextField(10);
        registrationText.setFont(customFont);
        registrationText.setForeground(Color.MAGENTA); // Замените на ваш выбранный цвет
        int Rx = 800; // Замените на ваше желаемое смещение по X
        int Ry = 400; // Замените на ваше желаемое смещение по Y
        int Rwidth = 400; // Замените на ваше желаемую ширину
        int Rheight = 40; // Замените на ваше желаемую высоту
        registrationText.setBounds(Rx, Ry, Rwidth, Rheight);
        registrationText.setBackground(Color.black); // Замените на ваш выбранный цвет фона
        layeredPane.add(registrationText, Integer.valueOf(1));





        ImageIcon AuthorizationIcon = new ImageIcon("resources/AUTH.png");
        JLabel AuthorizationLabel = new JLabel(AuthorizationIcon);
        int aX = 1190;
        int aY = 584; // Смещение на 100 пикселей ниже
        AuthorizationLabel.setBounds(aX, aY, AuthorizationIcon.getIconWidth(), AuthorizationIcon.getIconHeight());

        AuthorizationLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();
                try {
                    createJsonAndSendCommand(clientGui, "AUTHORIZATION " + authorizationText.getText());
                } catch (IOException event) {
                    throw new RuntimeException(event);
                }
                clientGui.createAndConnect = new CreateAndConnect(clientGui);
                clientGui.createAndConnect.setVisible(true);
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playTapSound();
                AuthorizationLabel.setIcon(new ImageIcon("resources/AUTHUP.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                AuthorizationLabel.setIcon(AuthorizationIcon);
            }
        });
        layeredPane.add(AuthorizationLabel, Integer.valueOf(1)); // Добавляем на передний слой

        ImageIcon AuthInfoIcon = new ImageIcon("resources/authorization.png");
        JLabel AuthInfoLabel = new JLabel(AuthInfoIcon);
        int aiX = 530;
        int aiY = 520; // Смещение на 100 пикселей ниже
        AuthInfoLabel.setBounds(aiX, aiY, AuthInfoIcon.getIconWidth(), AuthInfoIcon.getIconHeight());
        layeredPane.add(AuthInfoLabel, Integer.valueOf(1)); // Добавляем на передний слой


        authorizationText = new JTextField(10);
        authorizationText.setFont(customFont);
        authorizationText.setForeground(Color.MAGENTA); // Замените на ваш выбранный цвет
        int Ax = 800; // Замените на ваше желаемое смещение по X
        int Ay = 600; // Замените на ваше желаемое смещение по Y
        int Awidth = 400; // Замените на ваше желаемую ширину
        int Aheight = 40; // Замените на ваше желаемую высоту
        authorizationText.setBounds(Ax, Ay, Awidth, Aheight);
        authorizationText.setBackground(Color.BLACK); // Замените на ваш выбранный цвет фона
        layeredPane.add(authorizationText, Integer.valueOf(1));




        ///??????????????????????
        registrationLabelError = new JLabel();
        registrationLabelError.setVisible(false);



        authorizationLabelError = new JLabel();
        authorizationLabelError.setVisible(false);
        //??????????????????????????


        layeredPane.add(registrationLabelError, Integer.valueOf(1));
        layeredPane.add(authorizationLabelError, Integer.valueOf(1));


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
