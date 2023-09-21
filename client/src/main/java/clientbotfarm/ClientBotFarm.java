package clientbotfarm;

import clientrequest.*;
import clientresponse.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import logic.Move;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

class ClientBotFarm {

   /* private final Socket socket;
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;

    private final Socket socketBot;
    private final BufferedReader bufferedReaderBot;
    private final BufferedWriter bufferedWriterBot;
    private final Gson gson;
    private int roomId = 0;

    private static int countGame = 0;

    private static final Logger logger = LogManager.getLogger(ClientBotFarm.class);

    static int winnerW = 0;
    static int winnerB = 0;
    static int winnerT = 0;


    private ClientBotFarm(String host, int serverPort, int botFarmPort) throws IOException {
        socket = new Socket(host, serverPort);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        socketBot = new Socket(host, botFarmPort);
        bufferedReaderBot = new BufferedReader(new InputStreamReader(socketBot.getInputStream()));
        bufferedWriterBot = new BufferedWriter(new OutputStreamWriter(socketBot.getOutputStream()));
        gson = new Gson();
    }

    private void sendRequestGameServer(Request request) throws IOException {
        String jsonRequest = gson.toJson(request);
        bufferedWriter.write(jsonRequest);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    private void sendRequestBotServer(Request request) throws IOException {
        String jsonRequest = gson.toJson(request);
        bufferedWriterBot.write(jsonRequest);
        bufferedWriterBot.newLine();
        bufferedWriterBot.flush();
    }

    private void getMessageHuman() {
        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    String line = bufferedReader.readLine();
                    if (line != null) {
                        viewOnInComeMessageHuman(this, line);
                    }
                } catch (IOException e) {
                    logger.log(Level.ERROR, "Обрыв канала чтения");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getMessageBotGameServer() {
        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    String line = bufferedReader.readLine();
                    if (line != null) {
                        viewOnInComeMessageBot(this, line);
                    }
                } catch (IOException e) {
                    logger.log(Level.ERROR, "Обрыв канала чтения");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getMessageBotBotServer() {
        new Thread(() -> {
            while (socketBot.isConnected()) {
                try {
                    String line = bufferedReaderBot.readLine();
                    if (line != null) {
                        viewOnInComeMessageBot(this, line);
                    }
                } catch (IOException e) {
                    logger.log(Level.ERROR, "Обрыв канала чтения");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendMessageGameServer() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (socket.isConnected()) {
            String msg = scanner.nextLine();
            createJsonAndSendCommand(this, msg);
        }
    }

    private void sendMessageBotServer() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (socketBot.isConnected()) {
            String msg = scanner.nextLine();
            createJsonAndSendCommand(this, msg);
        }
    }

    private <T extends Response> T getResponse(Class<T> responseType, String jsonResponse) {
        return gson.fromJson(jsonResponse, responseType);
    }

    public void close() throws IOException {
        socket.close();
        socketBot.close();
    }


    public static void main(String[] args) throws IOException {
        String host;
        int serverPort;
        int botFarmPort;
        String player;
        String botPlayerName;
        Properties appProps = new Properties();
        File file = new File("client/file.properties");

        try (FileInputStream propertiesInput = new FileInputStream(file)) {
            appProps.load(propertiesInput);
            host = appProps.getProperty("host");
            serverPort = Integer.parseInt(appProps.getProperty("serverPort"));
            botFarmPort = Integer.parseInt(appProps.getProperty("botFarmPort"));
            player = appProps.getProperty("player");
            botPlayerName = appProps.getProperty("botName");

        } catch (IOException e) {
            logger.log(Level.ERROR, "Cannot read from file.properties");
            throw e;
        }
        ClientBotFarm client = new ClientBotFarm(host, serverPort, botFarmPort);
        try {
            switch (player) {
                case "bot" -> {

                    System.out.println("enter bot name");
                    Scanner scanner = new Scanner(System.in);
                    String botName = scanner.nextLine();

                    RegistrationRequest registrationRequest = new RegistrationRequest(botName);
                    client.sendRequestGameServer(registrationRequest);

                    AuthorizationRequest authorizationRequest = new AuthorizationRequest(botName);
                    client.sendRequestGameServer(authorizationRequest);

                    ViewCreatedRoomsRequest viewCreatedRoomsRequest = new ViewCreatedRoomsRequest(1);
                    client.sendRequestGameServer(viewCreatedRoomsRequest);

                    RegistrationBotFarmRequest registrationRequestBot = new RegistrationBotFarmRequest(botPlayerName);
                    client.sendRequestBotServer(registrationRequestBot);

                    try {
                        client.bufferedReader.readLine();
                        client.bufferedReader.readLine();
                        String line = client.bufferedReader.readLine();
                        String line2 = client.bufferedReaderBot.readLine();
                        System.out.println(line2);

                        if (line != null) {
                            client.viewOnInComeMessageBot(client, line);
                            System.out.println(line);
                            if (line.contains("GAMEOVER")) {
                                System.out.println("soderzhit gameover");
                                client.sendRequestGameServer(viewCreatedRoomsRequest);
                            }
                        }
                    } catch (IOException e) {
                        logger.log(Level.ERROR, "Обрыв канала чтения");
                        e.printStackTrace();
                    }

                    //       }

                    client.getMessageBotGameServer();
                    client.getMessageBotBotServer();
                    client.sendMessageGameServer();
                    client.sendMessageBotServer();
                    client.close();
                }
                case "human" -> {
                    client.getMessageHuman();
                    client.sendMessageGameServer();

                    client.close();

                }
            }
        } catch (IOException e) {
            logger.log(Level.ERROR, "Работа клиента была прервана");
            throw e;
        }
    }

    private void viewOnInComeMessageHuman(ClientBotFarm client, String input) throws IOException {

        JsonObject request = JsonParser.parseString(input).getAsJsonObject();
        String commandName = request.get("command").getAsString().toUpperCase();

        switch (commandName) {
            case "REGISTRATION" -> viewRegistration(client, input);

            case "AUTHORIZATION" -> viewAuthorization(client, input);

            case "CREATEROOM" -> viewCreateRoom(client, input);

            case "CONNECTTOROOM" -> viewConnectToRoom(client, input);

            case "LEAVEROOM" -> viewLeaveRoom(client, input);

            case "WHEREICANGORESPONSE" -> viewWhereICanGo(client, input);

            case "GAMEOVER" -> viewGameOver(client, input);

            case "MAKEMOVE" -> viewMakeMove(client, input);

            case "STARTGAME" -> viewStartGame(client, input);

            case "REGISTRATION_BOT_FARM" -> viewRegistrationBotFarm(client, input);

            case "EXIT" -> viewExit(client);

            case "SURRENDER" -> viewSurrender(client, input);

            case "GUI" -> viewGui(client, input);

            default -> viewDefault(commandName);
        }
    }

    private void createJsonAndSendCommand(ClientBotFarm client, String input) throws IOException {
        String[] commandParts = input.split("\\s+");
        String command = commandParts[0];

        switch (command) {
            case "REGISTRATION" -> commandRegistration(client, commandParts);

            case "AUTHORIZATION" -> commandAuthorization(client, commandParts);

            case "CREATEROOM" -> commandCreateRoom(client);

            case "CONNECTTOROOM" -> commandConnectToRoom(client, commandParts);

            case "LEAVEROOM" -> commandLeaveRoom(client);

            case "STARTGAME" -> commandStartGame(client);

            case "MAKEMOVE" -> commandMakeMove(client, commandParts);

            case "SURRENDER" -> commandSurrender(client);

            case "GUI" -> commandGui(client);

            case "EXIT" -> commandExit(client);

            default -> commandDefault(command);
        }
    }

    private void viewOnInComeMessageBot(ClientBotFarm client, String input) throws IOException {

        JsonObject request = JsonParser.parseString(input).getAsJsonObject();
        String commandName = request.get("command").getAsString().toUpperCase();

        switch (commandName) {

            case "REGISTRATION" -> viewRegistrationBot(client, input);

            case "AUTHORIZATION" -> viewAuthorizationBot(client, input);

            case "VIEWROOMS" -> viewViewRoomsBot(client, input);

            case "CREATEROOM" -> viewCreateRoomBot(client, input);

            case "CONNECTTOROOM" -> viewConnectToRoomBot(client, input);

            case "WHEREICANGORESPONSE" -> viewWhereICanGoGameBot(client, input);

            case "SEND_MOVE_TO_SERVER_BOT" -> viewSendMoveToGameServer(client, input);

            case "LEAVEROOM" -> viewLeaveRoomBot(client, input);

            case "GAMEOVER" -> viewGameOverBot(client, input);

            case "MAKEMOVE" -> viewMakeMoveBot(client, input);

            case "STARTGAME" -> viewStartGameBot(client, input);

            case "EXIT" -> viewExitBot(client);

            case "SURRENDER" -> viewSurrenderBot(client, input);

            case "GUI" -> viewGuiBot(client, input);

            default -> viewDefaultBot(commandName);
        }
    }

    private void viewRegistration(ClientBotFarm client, String input) {
        RegistrationResponse registrationResponse = client.getResponse(RegistrationResponse.class, input);
        System.out.println("Registration response: " + registrationResponse.message);

    }

    private void viewAuthorization(ClientBotFarm client, String input) {
        AuthorizationResponse authorizationResponse = client.getResponse(AuthorizationResponse.class, input);
        System.out.println("Authorization response: " + authorizationResponse.message);
    }

    private void viewCreateRoom(ClientBotFarm client, String input) {
        CreateRoomResponse createRoomResponse = client.getResponse(CreateRoomResponse.class, input);
        if (createRoomResponse.status.equals("fail")) {
            System.out.println("Create room response: " + createRoomResponse.message);
        }
        roomId = createRoomResponse.getRoomId();
        System.out.println("Create room response: " + createRoomResponse.message + ", Room ID: " + createRoomResponse.getRoomId());

    }

    private void viewConnectToRoom(ClientBotFarm client, String input) {
        ConnectToRoomResponse connectToRoomResponse = client.getResponse(ConnectToRoomResponse.class, input);
        System.out.println("Connect to room response: " + connectToRoomResponse.message);
    }

    private void viewLeaveRoom(ClientBotFarm client, String input) {
        LeaveRoomResponse leaveRoomResponse = client.getResponse(LeaveRoomResponse.class, input);
        System.out.println("Leave room response: " + leaveRoomResponse.message);
    }

    private void viewWhereICanGo(ClientBotFarm client, String input) {
        WhereIcanGoResponse whereIcanGoResponse = client.getResponse(WhereIcanGoResponse.class, input);
        System.out.println(whereIcanGoResponse.board);
        System.out.println("Your available moves " + whereIcanGoResponse.availableMoves);
    }

    private void viewGameOver(ClientBotFarm client, String input) {
        GameoverResponse gameoverResponse = client.getResponse(GameoverResponse.class, input);
        System.out.println("Game over response " + gameoverResponse.message);
    }

    private void viewMakeMove(ClientBotFarm client, String input) {
        MakeMoveResponse makeMoveResponse = client.getResponse(MakeMoveResponse.class, input);
        System.out.println("MakeMove response " + makeMoveResponse.message);
    }

    private void viewStartGame(ClientBotFarm client, String input) {
        StartGameResponse startGameResponse = client.getResponse(StartGameResponse.class, input);
        System.out.println("StartGame response " + startGameResponse.message);
    }

    private void viewRegistrationBotFarm(ClientBotFarm client, String input) {
        StartGameResponse startGameResponse = client.getResponse(StartGameResponse.class, input);
        System.out.println("Bot prisoedenen " + startGameResponse.message);
    }

    private void viewSurrender(ClientBotFarm client, String input) {
        SurrenderResponse surrenderResponse = client.getResponse(SurrenderResponse.class, input);
        System.out.println(surrenderResponse.message);
    }

    private void viewGui(ClientBotFarm client, String input) {
        GUIResponse guiResponse = client.getResponse(GUIResponse.class, input);
        System.out.println(guiResponse.message);
        SwingUtilities.invokeLater(Application::startGUIInterface);
    }

    private void viewExit(ClientBotFarm client) throws IOException {
        client.close();
        System.exit(0);
    }

    private void viewDefault(String commandName) {
        System.out.println("Unknown command: " + commandName);
    }


    private void commandRegistration(ClientBotFarm client, String[] commandParts) throws IOException {
        if (commandParts.length > 1) {
            String nickname = commandParts[1];
            RegistrationRequest registrationRequest = new RegistrationRequest(nickname);
            client.sendRequestGameServer(registrationRequest);
        } else {
            System.out.println("Usage: register <nickname>");
        }
    }

    private void commandAuthorization(ClientBotFarm client, String[] commandParts) throws IOException {
        if (commandParts.length > 1) {
            String nickname = commandParts[1];
            AuthorizationRequest authorizationRequest = new AuthorizationRequest(nickname);
            client.sendRequestGameServer(authorizationRequest);
        } else {
            System.out.println("Usage: login <nickname>");
        }
    }

    private void commandCreateRoom(ClientBotFarm client) throws IOException {
        if (roomId != 0) {
            System.out.println("You can't create another room because you are already in room.");
        } else {
            CreateRoomRequest createRoomRequest = new CreateRoomRequest();
            client.sendRequestGameServer(createRoomRequest);
        }
    }

    private void commandConnectToRoom(ClientBotFarm client, String[] commandParts) throws IOException {
        if (roomId != 0) {
            System.out.println("You can't connect to another room because you are already in room.");
        }
        if (commandParts.length > 1) {
            int roomId = Integer.parseInt(commandParts[1]);
            ConnectToRoomRequest connectToRoomRequest = new ConnectToRoomRequest(roomId);
            client.sendRequestGameServer(connectToRoomRequest);
            this.roomId = roomId;
        } else {
            System.out.println("Usage: connect to room <room_id>.");
        }
    }

    private void commandLeaveRoom(ClientBotFarm client) throws IOException {
        if (roomId == 0) {
            System.out.println("You can't leave room because you are not in room.");
        } else {
            LeaveRoomRequest leaveRoomRequest = new LeaveRoomRequest();
            client.sendRequestGameServer(leaveRoomRequest);
            roomId = 0;
        }
    }

    private void commandStartGame(ClientBotFarm client) throws IOException {
        if (roomId == 0) {
            System.out.println("You can't start game because you are not in room.");
        } else {
            StartGameRequest startGameRequest = new StartGameRequest(roomId);
            client.sendRequestGameServer(startGameRequest);
        }
    }

    private void commandMakeMove(ClientBotFarm client, String[] commandParts) throws IOException {
        if (commandParts.length > 2) {
            int row = Integer.parseInt(commandParts[1]);
            int col = Integer.parseInt(commandParts[2]);
            MakeMoveRequest makeMoveRequest = new MakeMoveRequest(row, col);
            client.sendRequestGameServer(makeMoveRequest);
        } else {
            System.out.println("wrong coordinates");
        }
    }

    private void commandSurrender(ClientBotFarm client) throws IOException {
        SurrenderRequest surrenderRequest = new SurrenderRequest();
        client.sendRequestGameServer(surrenderRequest);
    }

    private void commandGui(ClientBotFarm client) throws IOException {
        GUIRequest guiRequest = new GUIRequest();
        client.sendRequestGameServer(guiRequest);
    }

    private void commandExit(ClientBotFarm client) throws IOException {
        client.close();
        System.exit(0);
    }

    private void commandDefault(String commandName) {
        System.out.println("Unknown command: " + commandName);
    }


    private void viewRegistrationBot(ClientBotFarm client, String input) {
        RegistrationResponse registrationResponse = client.getResponse(RegistrationResponse.class, input);
        System.out.println("Registration response: " + registrationResponse.message);
    }

    private void viewAuthorizationBot(ClientBotFarm client, String input) {
        AuthorizationResponse authorizationResponse = client.getResponse(AuthorizationResponse.class, input);
        System.out.println("Authorization response: " + authorizationResponse.message);
    }

    private void viewViewRoomsBot(ClientBotFarm client, String input) throws IOException {
        ViewCreatedRoomsResponse viewCreatedRoomsResponse = client.getResponse(ViewCreatedRoomsResponse.class, input);
        if (viewCreatedRoomsResponse.status.equals("fail")) {
            CreateRoomRequest createRoomRequest = new CreateRoomRequest();
            client.sendRequestGameServer(createRoomRequest);

            System.out.println("Room was created");
        } else {
            ConnectToRoomRequest connectToRoomRequest = new ConnectToRoomRequest(1);
            client.sendRequestGameServer(connectToRoomRequest);
            System.out.println("Connected to room");
        }
    }

    private void viewCreateRoomBot(ClientBotFarm client, String input) {
        CreateRoomResponse createRoomResponse = client.getResponse(CreateRoomResponse.class, input);
        if (createRoomResponse.status.equals("fail")) {
            System.out.println("Create room response: " + createRoomResponse.message);
        }
        roomId = createRoomResponse.getRoomId();
        System.out.println("Create room response: " + createRoomResponse.message + ", Room ID: " + createRoomResponse.getRoomId());
    }

    private void viewConnectToRoomBot(ClientBotFarm client, String input) throws IOException {
        ConnectToRoomResponse connectToRoomResponse = client.getResponse(ConnectToRoomResponse.class, input);
        if (connectToRoomResponse.message.equals("White player connected")) {
            StartGameRequest startGameRequest = new StartGameRequest(client.roomId);
            client.sendRequestGameServer(startGameRequest);
            System.out.println("Room was created");
        }
        System.out.println("Connect to room response: " + connectToRoomResponse.message);
    }

    private void viewGameOverBot(ClientBotFarm client, String input) throws IOException {
        GameoverResponse gameoverResponse = client.getResponse(GameoverResponse.class, input);
        System.out.println("Game over response " + gameoverResponse.message);
        countGame++;
        System.out.println("game " + (countGame - 1) + " end");
        if (gameoverResponse.roomCreator && countGame < gameoverResponse.quantityOfGame) {
            StartGameRequest startGameRequest = new StartGameRequest(1);//ПОМЕНЯТЬ ХАРДКОД
            client.sendRequestGameServer(startGameRequest);
        }
        if (gameoverResponse.message.contains("Winner: Black")) {
            winnerB++;
        }
        if (gameoverResponse.message.contains("Winner: White")) {
            winnerW++;
        }
        if (countGame == gameoverResponse.quantityOfGame) {
            System.out.println("B: " + winnerB + "  W: " + winnerW);
        }
    }


    private void viewLeaveRoomBot(ClientBotFarm client, String input) {
        LeaveRoomResponse leaveRoomResponse = client.getResponse(LeaveRoomResponse.class, input);
        System.out.println("Leave room response: " + leaveRoomResponse.message);
    }

    private void viewStartGameBot(ClientBotFarm client, String input) {
        StartGameResponse startGameResponse = client.getResponse(StartGameResponse.class, input);
        System.out.println("StartGame response " + startGameResponse.message);
    }

    private void viewWhereICanGoGameBot(ClientBotFarm client, String input) throws IOException {
        WhereIcanGoResponse whereIcanGoResponse = client.getResponse(WhereIcanGoResponse.class, input);
        WhereICanGoBotFarmRequest whereICanGoBotFarmRequest = new WhereICanGoBotFarmRequest(whereIcanGoResponse.boardStringWON);

        System.out.println(whereIcanGoResponse.availableMoves);

        client.sendRequestBotServer(whereICanGoBotFarmRequest);

        //  move = botPlayer.makeMove(BoardParser.parse(whereIcanGoResponse.boardStringWON, 'B', 'W', '-'));
        // MakeMoveRequest makeMoveRequest = new MakeMoveRequest(move.row + 1, move.col + 1);
        //   client.sendRequestGameServer(makeMoveRequest);

    }

    private void viewSendMoveToGameServer(ClientBotFarm client, String input) throws IOException {
        JsonObject request = JsonParser.parseString(input).getAsJsonObject();
        int coordinates = request.get("move").getAsInt();

        Move move = new Move(coordinates / 10, coordinates % 10);
        MakeMoveRequest makeMoveRequest = new MakeMoveRequest(move.row + 1, move.col + 1);
        client.sendRequestGameServer(makeMoveRequest);
    }

    private void viewMakeMoveBot(ClientBotFarm client, String input) throws IOException {
        MakeMoveResponse makeMoveResponse = client.getResponse(MakeMoveResponse.class, input);
        System.out.println(makeMoveResponse.message);
        if (makeMoveResponse.status.equals("fail")) {
            WhereICanGoRequest whereICanGoRequest = new WhereICanGoRequest();
            client.sendRequestGameServer(whereICanGoRequest);
        }
    }

    private void viewSurrenderBot(ClientBotFarm client, String input) {
        SurrenderResponse surrenderResponse = client.getResponse(SurrenderResponse.class, input);
        System.out.println(surrenderResponse.message);
    }

    private void viewGuiBot(ClientBotFarm client, String input) {
        GUIResponse guiResponse = client.getResponse(GUIResponse.class, input);
        System.out.println(guiResponse.message);
        SwingUtilities.invokeLater(Application::startGUIInterface);
    }

    private void viewExitBot(ClientBotFarm client) throws IOException {
        client.close();
        System.exit(0);
    }

    private void viewDefaultBot(String commandName) {
        System.out.println("Unknown command: " + commandName);
    }*/
}


