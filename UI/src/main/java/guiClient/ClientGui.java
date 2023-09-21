package guiClient;

import com.google.gson.Gson;
import gui.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Properties;

import static gui.MainGameWindow.musicPlayer;
import static guiClient.GetResponsesMethodsHuman.getMessageHuman;
import static guiClient.SendMethods.sendMessage;

public class ClientGui extends JFrame {

    final Socket socket;
    public final BufferedReader bufferedReader;
    final BufferedWriter bufferedWriter;
    final Gson gson;
    int roomId = 0;

    private static int countGame = 0;

    private static final Logger logger = LogManager.getLogger(ClientGui.class);

    public RegAndAuth regAndAuth;
    public CreateAndConnect createAndConnect;
    public RoomGui roomGui;

    private ClientGui(@NotNull final String host, final int port) throws IOException {
        socket = new Socket(host, port);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        gson = new Gson();
    }

    public void close() throws IOException {
        socket.close();
    }


    public static void main(String[] args) throws IOException {
        String host;
        int port;
        String player;
        Properties appProps = new Properties();
        File file = new File("ui/file.properties");

        try (FileInputStream propertiesInput = new FileInputStream(file)) {
            appProps.load(propertiesInput);
            host = appProps.getProperty("host");
            port = Integer.parseInt(appProps.getProperty("port"));
            player = appProps.getProperty("player");
        } catch (IOException e) {
            logger.log(Level.ERROR, "Cannot read from file.properties");
            throw e;
        }


        ClientGui clientGui = new ClientGui(host, port);
        SwingUtilities.invokeLater(() -> {
            musicPlayer = new MusicPlayer("resources/retro.wav");
                musicPlayer.play();
            new MainStartWindow(clientGui).setVisible(true);
        });

        try {
            switch (player) {
                case "human" -> {
                    getMessageHuman(clientGui);
                    sendMessage(clientGui);
                    clientGui.close();
                }
            }
        } catch (IOException e) {
            logger.log(Level.ERROR, "Работа клиента была прервана");
            throw e;
        }
    }

}