package client;

import clientrequest.*;
import clientresponse.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.deeplay.Application;


import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

class Client {

    private final Socket socket;
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;
    private final Gson gson;
    private int roomId = 0;

    private static final Logger logger = LogManager.getLogger(Client.class);


    private Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        gson = new Gson();
    }

    private void sendRequest(Request request) throws IOException {
        String jsonRequest = gson.toJson(request);
        bufferedWriter.write(jsonRequest);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    private void getMessage() {
        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    String line = bufferedReader.readLine();
                    if (line != null) {
                        viewOnInComeMessage(this, line);
                    }
                } catch (IOException e) {
                    logger.log(Level.ERROR, "Обрыв канала чтения");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendMessage() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (socket.isConnected()) {
            String msg = scanner.nextLine();
            createJsonAndSendCommand(this, msg);
        }
    }

    private <T extends Response> T getResponse(Class<T> responseType, String jsonResponse) throws IOException {
        return gson.fromJson(jsonResponse, responseType);
    }

    public void close() throws IOException {
        socket.close();
    }


    public static void main(String[] args) throws IOException {
        String host;
        int port;
        Properties appProps = new Properties();
        File file = new File("client/file.properties");

        try (FileInputStream propertiesInput = new FileInputStream(file)) {
            appProps.load(propertiesInput);
            host = appProps.getProperty("host");
            port = Integer.parseInt(appProps.getProperty("port"));
        } catch (IOException e) {
            logger.log(Level.ERROR, "Cannot read from file.properties");
            throw e;
        }

        try {
            Client client = new Client(host, port);

            client.getMessage();
            client.sendMessage();

            client.close();
        } catch (IOException e) {
            logger.log(Level.ERROR, "Работа клиента была прервана");
            throw e;
        }

    }

    private void viewOnInComeMessage(Client client, String input) throws IOException {

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

            case "EXIT" -> viewExit(client);

            case "SURRENDER" -> viewSurrender(client, input);

            case "GUI" -> viewGui(client, input);

            default -> viewDefault(commandName);
        }
    }

    private void createJsonAndSendCommand(Client client, String input) throws IOException {
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

    private void viewRegistration(Client client, String input) throws IOException {
        RegistrationResponse registrationResponse = client.getResponse(RegistrationResponse.class, input);
        System.out.println("Registration response: " + registrationResponse.message);

    }

    private void viewAuthorization(Client client, String input) throws IOException {
        AuthorizationResponse authorizationResponse = client.getResponse(AuthorizationResponse.class, input);
        System.out.println("Authorization response: " + authorizationResponse.message);
    }

    private void viewCreateRoom(Client client, String input) throws IOException {
        CreateRoomResponse createRoomResponse = client.getResponse(CreateRoomResponse.class, input);
        if (createRoomResponse.status.equals("fail")) {
            System.out.println("Create room response: " + createRoomResponse.message);
        }
        roomId = createRoomResponse.getRoomId();
        System.out.println("Create room response: " + createRoomResponse.message + ", Room ID: " + createRoomResponse.getRoomId());

    }

    private void viewConnectToRoom(Client client, String input) throws IOException {
        ConnectToRoomResponse connectToRoomResponse = client.getResponse(ConnectToRoomResponse.class, input);
        System.out.println("Connect to room response: " + connectToRoomResponse.message);
    }

    private void viewLeaveRoom(Client client, String input) throws IOException {
        LeaveRoomResponse leaveRoomResponse = client.getResponse(LeaveRoomResponse.class, input);
        System.out.println("Leave room response: " + leaveRoomResponse.message);
    }

    private void viewWhereICanGo(Client client, String input) throws IOException {
        WhereIcanGoResponse whereIcanGoResponse = client.getResponse(WhereIcanGoResponse.class, input);
        System.out.println(whereIcanGoResponse.board);
        System.out.println("Your available moves " + whereIcanGoResponse.availableMoves);
    }

    private void viewGameOver(Client client, String input) throws IOException {
        GameoverResponse gameoverResponse = client.getResponse(GameoverResponse.class, input);
        System.out.println("Game over response " + gameoverResponse.message);
    }

    private void viewMakeMove(Client client, String input) throws IOException {
        MakeMoveResponse makeMoveResponse = client.getResponse(MakeMoveResponse.class, input);
        System.out.println("MakeMove response " + makeMoveResponse.message);
    }

    private void viewStartGame(Client client, String input) throws IOException {
        StartGameResponse startGameResponse = client.getResponse(StartGameResponse.class, input);
        System.out.println("StartGame response " + startGameResponse.message);
    }

    private void viewSurrender(Client client, String input) throws IOException {
        SurrenderResponse surrenderResponse = client.getResponse(SurrenderResponse.class, input);
        System.out.println(surrenderResponse.message);
    }

    private void viewGui(Client client, String input) throws IOException {
        GUIResponse guiResponse = client.getResponse(GUIResponse.class, input);
        System.out.println(guiResponse.message);
        SwingUtilities.invokeLater(() -> Application.startGUIInterface());
    }

    private void viewExit(Client client) throws IOException {
        client.close();
        System.exit(0);
    }

    private void viewDefault(String commandName) throws IOException {
        System.out.println("Unknown command: " + commandName);
    }


    private void commandRegistration(Client client, String[] commandParts) throws IOException {
        if (commandParts.length > 1) {
            String nickname = commandParts[1];
            RegistrationRequest registrationRequest = new RegistrationRequest(nickname);
            client.sendRequest(registrationRequest);
        } else {
            System.out.println("Usage: register <nickname>");
        }
    }

    private void commandAuthorization(Client client, String[] commandParts) throws IOException {
        if (commandParts.length > 1) {
            String nickname = commandParts[1];
            AuthorizationRequest authorizationRequest = new AuthorizationRequest(nickname);
            client.sendRequest(authorizationRequest);
        } else {
            System.out.println("Usage: login <nickname>");
        }
    }

    private void commandCreateRoom(Client client) throws IOException {
        if (roomId != 0) {
            System.out.println("You can't create another room because you are already in room.");
        } else {
            CreateRoomRequest createRoomRequest = new CreateRoomRequest();
            client.sendRequest(createRoomRequest);
        }
    }

    private void commandConnectToRoom(Client client, String[] commandParts) throws IOException {
        if (roomId != 0) {
            System.out.println("You can't connect to another room because you are already in room.");
        }
        if (commandParts.length > 1) {
            int roomId = Integer.parseInt(commandParts[1]);
            ConnectToRoomRequest connectToRoomRequest = new ConnectToRoomRequest(roomId);
            client.sendRequest(connectToRoomRequest);
            this.roomId = roomId;
        } else {
            System.out.println("Usage: connect to room <room_id>.");
        }
    }

    private void commandLeaveRoom(Client client) throws IOException {
        if (roomId == 0) {
            System.out.println("You can't leave room because you are not in room.");
        } else {
            LeaveRoomRequest leaveRoomRequest = new LeaveRoomRequest();
            client.sendRequest(leaveRoomRequest);
            roomId = 0;
        }
    }

    private void commandStartGame(Client client) throws IOException {
        if (roomId == 0) {
            System.out.println("You can't start game because you are not in room.");
        } else {
            StartGameRequest startGameRequest = new StartGameRequest(roomId);
            client.sendRequest(startGameRequest);
        }
    }

    private void commandMakeMove(Client client, String[] commandParts) throws IOException {
        if (commandParts.length > 2) {
            int row = Integer.parseInt(commandParts[1]);
            int col = Integer.parseInt(commandParts[2]);
            MakeMoveRequest makeMoveRequest = new MakeMoveRequest(row, col);
            client.sendRequest(makeMoveRequest);
        } else {
            System.out.println("wrong coordinates");
        }
    }

    private void commandSurrender(Client client) throws IOException {
        SurrenderRequest surrenderRequest = new SurrenderRequest();
        client.sendRequest(surrenderRequest);
    }

    private void commandGui(Client client) throws IOException {
        GUIRequest guiRequest = new GUIRequest();
        client.sendRequest(guiRequest);
    }

    private void commandExit(Client client) throws IOException {
        client.close();
        System.exit(0);
    }

    private void commandDefault(String commandName) {
        System.out.println("Unknown command: " + commandName);
    }
}


