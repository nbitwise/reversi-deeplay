package client;

import clientrequest.*;
import clientresponse.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
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


    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 6070);

            client.getMessage();
            client.sendMessage();

            client.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.ERROR, "Работа клиента была прервана");
        }
    }

    private void viewOnInComeMessage(Client client, String input) throws IOException {

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
                System.out.println("Connect to room response: " + connectToRoomResponse.message);
            }
            case "LEAVEROOM" -> {
                LeaveRoomResponse leaveRoomResponse = client.getResponse(LeaveRoomResponse.class, input);
                System.out.println("Leave room response: " + leaveRoomResponse.message);
            }
            case "WHEREICANGORESPONSE" -> {
                WhereIcanGoResponse whereIcanGoResponse = client.getResponse(WhereIcanGoResponse.class, input);
                System.out.println(whereIcanGoResponse.board);
                System.out.println("Your available moves " + whereIcanGoResponse.availableMoves);
            }
            case "GAMEOVER" -> {
                GameoverResponse gameoverResponse = client.getResponse(GameoverResponse.class, input);
                System.out.println("Game over response " + gameoverResponse.message);
            }
            case "MAKEMOVE" -> {
                MakeMoveResponse makeMoveResponse = client.getResponse(MakeMoveResponse.class, input);
                System.out.println("MakeMove response " + makeMoveResponse.getMessage());
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
            default -> System.out.println("Unknown command: " + commandName);
        }
    }

    private void createJsonAndSendCommand(Client client, String input) throws IOException {
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
            case "MOVE" -> {
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
            case "EXIT" -> {
                client.close();
                System.exit(0);
            }
            default -> System.out.println("Unknown command: " + command);
        }
    }
}
