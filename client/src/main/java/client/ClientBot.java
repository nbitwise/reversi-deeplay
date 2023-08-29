package client;

import clientrequest.*;
import clientresponse.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import logic.Board;
import logic.Cell;
import logic.Move;
import logic.Player;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gui.GUI;

import io.deeplay.Application;
import parsing.BoardParser;


import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

class ClientBot {

    private final Socket socket;
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;
    private final Gson gson;
    private int roomId = 0;

    private static final Logger logger = LogManager.getLogger(Client.class);


    private ClientBot(String host, int port) throws IOException {
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


    public static void main(String[] args) {
        try {
            ClientBot client = new ClientBot("localhost", 6070);
            System.out.println("enter bot name");
            Scanner scanner = new Scanner(System.in);
            String botName = scanner.nextLine();

            RegistrationRequest registrationRequest = new RegistrationRequest(botName);
            client.sendRequest(registrationRequest);

            AuthorizationRequest authorizationRequest = new AuthorizationRequest(botName);
            client.sendRequest(authorizationRequest);

            ViewCreatedRoomsRequest viewCreatedRoomsRequest = new ViewCreatedRoomsRequest(1);
            client.sendRequest(viewCreatedRoomsRequest);

            try {
                client.bufferedReader.readLine();
                client.bufferedReader.readLine();
                String line = client.bufferedReader.readLine();
                if (line != null) {
                    client.viewOnInComeMessage(client, line);
                }
            } catch (IOException e) {
                logger.log(Level.ERROR, "Обрыв канала чтения");
                e.printStackTrace();
            }


            client.getMessage();
            client.sendMessage();


            client.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.ERROR, "Работа клиента была прервана");
        }
    }

    private void viewOnInComeMessage(ClientBot client, String input) throws IOException {

        JsonObject request = JsonParser.parseString(input).getAsJsonObject();
        String commandName = request.get("command").getAsString().toUpperCase();

        switch (commandName) {
            case "REGISTRATION" -> {
                RegistrationResponse registrationResponse = client.getResponse(RegistrationResponse.class, input);
                System.out.println("Registration response: " + registrationResponse.message);
            }
            case "AUTHORIZATION" -> {
                AuthorizationResponse authorizationResponse = client.getResponse(AuthorizationResponse.class, input);
                System.out.println("Authorization response: " + authorizationResponse.message);
            }
            case "VIEWROOMS" -> {
                ViewCreatedRoomsResponse viewCreatedRoomsResponse = client.getResponse(ViewCreatedRoomsResponse.class, input);
                if (viewCreatedRoomsResponse.status.equals("fail")) {
                    CreateRoomRequest createRoomRequest = new CreateRoomRequest();
                    client.sendRequest(createRoomRequest);

                    System.out.println("Room was created");
                } else {
                    ConnectToRoomRequest connectToRoomRequest = new ConnectToRoomRequest(1);
                    client.sendRequest(connectToRoomRequest);
                    System.out.println("Connected to room");
                }
            }
            case "CREATEROOM" -> {
                CreateRoomResponse createRoomResponse = client.getResponse(CreateRoomResponse.class, input);
                if (createRoomResponse.status.equals("fail")) {
                    System.out.println("Create room response: " + createRoomResponse.message);
                }
                roomId = createRoomResponse.getRoomId();
                System.out.println("Create room response: " + createRoomResponse.message + ", Room ID: " + createRoomResponse.getRoomId());
            }
            case "CONNECTTOROOM" -> {
                ConnectToRoomResponse connectToRoomResponse = client.getResponse(ConnectToRoomResponse.class, input);
                if (connectToRoomResponse.message.equals("White player connected")) {
                    StartGameRequest startGameRequest = new StartGameRequest(client.roomId);
                    client.sendRequest(startGameRequest);
                    System.out.println("Room was created");
                }
                System.out.println("Connect to room response: " + connectToRoomResponse.message);
            }
            case "LEAVEROOM" -> {
                LeaveRoomResponse leaveRoomResponse = client.getResponse(LeaveRoomResponse.class, input);
                System.out.println("Leave room response: " + leaveRoomResponse.message);
            }
            case "WHEREICANGORESPONSE" -> {
                WhereIcanGoResponse whereIcanGoResponse = client.getResponse(WhereIcanGoResponse.class, input);

                System.out.println(whereIcanGoResponse.availableMoves);
                if (whereIcanGoResponse.color.equals("black")) {
                    Player.BotPlayer botPlayer = new Player.BotPlayer(Cell.BLACK);
                    Move move = botPlayer.makeMove(BoardParser.parse(whereIcanGoResponse.boardStringWON, 'B', 'W', '-'));

                    MakeMoveRequest makeMoveRequest = new MakeMoveRequest(move.row + 1, move.col + 1);
                    client.sendRequest(makeMoveRequest);
                } else {
                    Player.BotPlayer botPlayer = new Player.BotPlayer(Cell.WHITE);
                    Move move = botPlayer.makeMove(BoardParser.parse(whereIcanGoResponse.boardStringWON, 'B', 'W', '-'));

                    MakeMoveRequest makeMoveRequest = new MakeMoveRequest(move.row + 1, move.col + 1);
                    client.sendRequest(makeMoveRequest);
                }
            }
            case "GAMEOVER" -> {
                GameoverResponse gameoverResponse = client.getResponse(GameoverResponse.class, input);
                System.out.println("Game over response " + gameoverResponse.message);
            }
            case "MAKEMOVE" -> {
                MakeMoveResponse makeMoveResponse = client.getResponse(MakeMoveResponse.class, input);
                System.out.println(makeMoveResponse.message);
                if(makeMoveResponse.status.equals("fail")){
                    WhereICanGoRequest whereICanGoRequest = new WhereICanGoRequest();
                    client.sendRequest(whereICanGoRequest);
                }
            }
            case "STARTGAME" -> {
                StartGameResponse startGameResponse = client.getResponse(StartGameResponse.class, input);
                System.out.println("StartGame response " + startGameResponse.message);

            }
            case "EXIT" -> {
                client.close();
                System.exit(0);
            }
            case "SURRENDER" -> {
                SurrenderResponse surrenderResponse = client.getResponse(SurrenderResponse.class, input);
                System.out.println(surrenderResponse.message);
            }
            case "GUI" -> {
                GUIResponse guiResponse = client.getResponse(GUIResponse.class, input);
                System.out.println(guiResponse.message);
                SwingUtilities.invokeLater(() -> Application.startGUIInterface());
            }
            default -> {
                System.out.println("Unknown command: " + commandName);
            }
        }
    }

    private void createJsonAndSendCommand(ClientBot client, String input) throws IOException {
        String[] commandParts = input.split("\\s+");
        String command = commandParts[0];

        switch (command) {
            case "REGISTRATION" -> {
                if (commandParts.length > 1) {
                    String nickname = commandParts[1];
                    RegistrationRequest registrationRequest = new RegistrationRequest(nickname);
                    client.sendRequest(registrationRequest);
                } else {
                    System.out.println("Usage: register <nickname>");
                }
            }
            case "AUTHORIZATION" -> {
                if (commandParts.length > 1) {
                    String nickname = commandParts[1];
                    AuthorizationRequest authorizationRequest = new AuthorizationRequest(nickname);
                    client.sendRequest(authorizationRequest);
                } else {
                    System.out.println("Usage: login <nickname>");
                }
            }
            case "CREATEROOM" -> {
                if (roomId != 0) {
                    System.out.println("You can't create another room because you are already in room.");
                    break;
                }
                CreateRoomRequest createRoomRequest = new CreateRoomRequest();
                client.sendRequest(createRoomRequest);
            }
            case "CONNECTTOROOM" -> {
                if (roomId != 0) {
                    System.out.println("You can't connect to another room because you are already in room.");
                    break;
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
            case "LEAVEROOM" -> {
                if (roomId == 0) {
                    System.out.println("You can't leave room because you are not in room.");
                    break;
                }
                LeaveRoomRequest leaveRoomRequest = new LeaveRoomRequest();
                client.sendRequest(leaveRoomRequest);
                roomId = 0;
            }
            case "STARTGAME" -> {
                if (roomId == 0) {
                    System.out.println("You can't start game because you are not in room.");
                    break;
                }
                StartGameRequest startGameRequest = new StartGameRequest(roomId);
                client.sendRequest(startGameRequest);
            }
            case "MAKEMOVE" -> {
                if (commandParts.length > 2) {
                    int row = Integer.parseInt(commandParts[1]);
                    int col = Integer.parseInt(commandParts[2]);
                    MakeMoveRequest makeMoveRequest = new MakeMoveRequest(row, col);
                    client.sendRequest(makeMoveRequest);
                } else {
                    System.out.println("wrong coordinates");
                }
            }
            case "SURRENDER" -> {
                SurrenderRequest surrenderRequest = new SurrenderRequest();
                client.sendRequest(surrenderRequest);
            }
            case "GUI" -> {
                GUIRequest guiRequest = new GUIRequest();
                client.sendRequest(guiRequest);
            }
            case "EXIT" -> {
                client.close();
                System.exit(0);
            }
            default -> System.out.println("Unknown command: " + command);
        }
    }
}
