package clientbotfarm;

import clientbotfarmrequest.AuthorizationRequest;
import clientbotfarmrequest.RegistrationBotFarmRequest;
import clientbotfarmrequest.RegistrationRequest;
import clientbotfarmrequest.ViewCreatedRoomsRequest;
import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class ClientBotFarm {
    private static final Logger logger = LogManager.getLogger(ClientBotFarm.class);
    final Socket socket;
    final BufferedReader bufferedReader;
    final BufferedWriter bufferedWriter;

    final Socket socketBot;
    final BufferedReader bufferedReaderBot;
    final BufferedWriter bufferedWriterBot;
    final Gson gson;
    int roomId = 0;

    private ClientBotFarm(@NotNull final String host, final int serverPort, final int botFarmPort) throws IOException {
        socket = new Socket(host, serverPort);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        socketBot = new Socket("localhost", botFarmPort);
        bufferedReaderBot = new BufferedReader(new InputStreamReader(socketBot.getInputStream()));
        bufferedWriterBot = new BufferedWriter(new OutputStreamWriter(socketBot.getOutputStream()));
        gson = new Gson();
    }

    void close() throws IOException {
        socket.close();
        socketBot.close();
    }

    public static void main(String[] args) throws IOException {
        final String host;
        final int serverPort;
        final int botFarmPort;
        final String botPlayerName;
        final String player;
        final Properties appProps = new Properties();
        final File file = new File("clientbotfarm/file.properties");
        try (FileInputStream propertiesInput = new FileInputStream(file)) {
            appProps.load(propertiesInput);
            host = appProps.getProperty("host");
            serverPort = Integer.parseInt(appProps.getProperty("port"));
            botFarmPort = Integer.parseInt(appProps.getProperty("botFarmPort"));
            player = appProps.getProperty("player");
            botPlayerName = appProps.getProperty("botName");
        } catch (IOException e) {
            logger.log(Level.ERROR, "Cannot read from file.properties");
            throw e;
        }
        ClientBotFarm clientBotFarm = new ClientBotFarm(host, serverPort, botFarmPort);
        try {
            switch (player) {
                case "bot" -> {
                    System.out.println("enter bot name");
                    final Scanner scanner = new Scanner(System.in);
                    final String botName = scanner.nextLine();

                    final RegistrationRequest registrationRequest = new RegistrationRequest(botName);
                    SendMethodsGameServer.sendRequestGameServer(registrationRequest, clientBotFarm);

                    final AuthorizationRequest authorizationRequest = new AuthorizationRequest(botName);
                    SendMethodsGameServer.sendRequestGameServer(authorizationRequest, clientBotFarm);

                    final ViewCreatedRoomsRequest viewCreatedRoomsRequest = new ViewCreatedRoomsRequest(1831930336);
                    SendMethodsGameServer.sendRequestGameServer(viewCreatedRoomsRequest, clientBotFarm);

                    final RegistrationBotFarmRequest registrationRequestBot = new RegistrationBotFarmRequest(botPlayerName);
                    SendMethodsBotFarm.sendRequestBotFarm(registrationRequestBot, clientBotFarm);

                    clientBotFarm.bufferedReader.readLine();
                    clientBotFarm.bufferedReader.readLine();


                    final String line = clientBotFarm.bufferedReader.readLine();
                    final String line2 = clientBotFarm.bufferedReaderBot.readLine();
                    System.out.println(line2);

                    if (line != null) {
                        GetResponsesMethodsBot.viewOnInComeMessageBot(clientBotFarm, line);
                        if (line.contains("GAMEOVER")) {
                            SendMethodsGameServer.sendRequestGameServer(viewCreatedRoomsRequest, clientBotFarm);
                        }
                    }

                    GetResponsesMethodsBot.getMessageBot(clientBotFarm);
                    GetResponsesMethodsBotFarm.getMessageBotFarm(clientBotFarm);
                    SendMethodsGameServer.sendMessageGameServer(clientBotFarm);


                    clientBotFarm.close();
                }
                case "human" -> {
                    GetResponsesMethodsHuman.getMessageHuman(clientBotFarm);
                    SendMethodsGameServer.sendMessageGameServer(clientBotFarm);
                    clientBotFarm.close();
                }
            }
        } catch (IOException e) {
            logger.log(Level.ERROR, "Работа клиента была прервана");
            throw e;
        }
    }
}


