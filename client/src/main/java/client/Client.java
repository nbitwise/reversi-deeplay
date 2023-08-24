package client;

import clientrequest.*;
import clientresponse.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    private final Socket socket;
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;
    private final Gson gson;
    private final ExecutorService executorService;

    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        gson = new Gson();
        executorService = Executors.newFixedThreadPool(2); // Adjust the thread pool size as needed
    }

    public void sendRequest(Request request) throws IOException {
        String jsonRequest = gson.toJson(request);
        bufferedWriter.write(jsonRequest);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    public void getMessage() {
        new Thread(() -> {
            String msg;
            while (socket.isConnected()) {
                try {
                    String line = bufferedReader.readLine();
                    if (line != null) {
                        viewOnInComeMessage(this, line);
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }).start();
    }

    public void sendMessage() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (socket.isConnected()) {
            String msg = scanner.nextLine();
            createJsonAndSendCommand(this, msg);
        }
    }

    public <T extends Response> T getResponse(Class<T> responseType, String jsonResponse) throws IOException {
        return gson.fromJson(jsonResponse, responseType);
    }

    public void close() throws IOException {
        socket.close();
        executorService.shutdown();
    }


    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 6070);

            client.getMessage();
            client.sendMessage();

            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void viewOnInComeMessage(Client client, String input) throws IOException {

        JsonObject request = JsonParser.parseString(input).getAsJsonObject();
        String commandName = request.get("command").getAsString().toUpperCase();

        switch (commandName) {
            case "REGISTRATION":
                RegistrationResponse registrationResponse = client.getResponse(RegistrationResponse.class, input);
                System.out.println("Registration response: " + registrationResponse.message);
                break;
            case "AUTHORIZATION":
                AuthorizationResponse authorizationResponse = client.getResponse(AuthorizationResponse.class, input);
                System.out.println("Authorization response: " + authorizationResponse.message);
                break;
            case "CREATEROOM":
                CreateRoomResponse createRoomResponse = client.getResponse(CreateRoomResponse.class, input);
                if (createRoomResponse.status.equals("fail")) {
                    System.out.println("Create room response: " + createRoomResponse.message);
                }
                System.out.println("Create room response: " + createRoomResponse.message + ", Room ID: " + createRoomResponse.getRoomId());
                break;
            case "CONNECTTOROOM":
                ConnectToRoomResponse connectToRoomResponse = client.getResponse(ConnectToRoomResponse.class, input);
                System.out.println("Connect to room response: " + connectToRoomResponse.message);
                break;
            case "LEAVEROOM":
                LeaveRoomResponse leaveRoomResponse = client.getResponse(LeaveRoomResponse.class, input);
                System.out.println("Leave room response: " + leaveRoomResponse.message);
                break;
            case "WHEREICANGORESPONSE":
                WhereIcanGoResponse whereIcanGoResponse = client.getResponse(WhereIcanGoResponse.class, input);
                System.out.println(whereIcanGoResponse.board);
                System.out.println("Your available moves " + whereIcanGoResponse.availableMoves);
                break;
            case "GAMEOVER":
                GameoverResponse gameoverResponse = client.getResponse(GameoverResponse.class, input);
                System.out.println("Game over response " + gameoverResponse.message);
                break;
            case "MAKEMOVE":
                MakeMoveResponse makeMoveResponse = client.getResponse(MakeMoveResponse.class, input);
                System.out.println("MakeMove response " + makeMoveResponse.getMessage());
                break;
            case "STARTGAME":
                StartGameResponse startGameResponse = client.getResponse(StartGameResponse.class, input);
                System.out.println("StartGame response " + startGameResponse.message);
                break;
            case "EXIT":
                client.close();
                System.exit(0);
                break;
                case "SURRENDER":
                SurrenderResponse surrenderResponse = (SurrenderResponse)client.getResponse(SurrenderResponse.class, input);
                System.out.println(surrenderResponse.message);
                break;
            default:
                System.out.println("Unknown command: " + commandName);
                break;
        }
    }

    private static void createJsonAndSendCommand(Client client, String input) throws IOException {
        String[] commandParts = input.split("\\s+");
        String command = commandParts[0];

        switch (command) {
            case "REGISTRATION":
                if (commandParts.length > 1) {
                    String nickname = commandParts[1];
                    RegistrationRequest registrationRequest = new RegistrationRequest(nickname);
                    client.sendRequest(registrationRequest);
                } else {
                    System.out.println("Usage: register <nickname>");
                }
                break;
            case "AUTHORIZATION":
                if (commandParts.length > 1) {
                    String nickname = commandParts[1];
                    AuthorizationRequest authorizationRequest = new AuthorizationRequest(nickname);
                    client.sendRequest(authorizationRequest);
                } else {
                    System.out.println("Usage: login <nickname>");
                }
                break;
            case "CREATEROOM":
                CreateRoomRequest createRoomRequest = new CreateRoomRequest();
                client.sendRequest(createRoomRequest);
                break;
            case "CONNECTTOROOM":
                if (commandParts.length > 1) {
                    int roomId = Integer.parseInt(commandParts[1]);
                    ConnectToRoomRequest connectToRoomRequest = new ConnectToRoomRequest(roomId);
                    client.sendRequest(connectToRoomRequest);
                } else {
                    System.out.println("Usage: connecttoroom <room_id>");
                }
                break;
            case "LEAVEROOM":
                LeaveRoomRequest leaveRoomRequest = new LeaveRoomRequest();
                client.sendRequest(leaveRoomRequest);
                break;
            case "STARTGAME":
                if (commandParts.length > 1) {
                    int roomId = Integer.parseInt(commandParts[1]);
                    StartGameRequest startGameRequest = new StartGameRequest(roomId);
                    client.sendRequest(startGameRequest);
                } else {
                    System.out.println("Usage: connecttoroom you didn't enter id of the room");
                }
                break;
            case "MAKEMOVE":
                if (commandParts.length > 2) {
                    int row = Integer.parseInt(commandParts[1]);
                    int col = Integer.parseInt(commandParts[2]);
                    MakeMoveRequest makeMoveRequest = new MakeMoveRequest(row, col);
                    client.sendRequest(makeMoveRequest);
                } else {
                    System.out.println("wrong coordinates");
                }
                break;
            case "SURRENDER":
                SurrenderRequest surrenderRequest = new SurrenderRequest();
                client.sendRequest(surrenderRequest);
                break;
            case "EXIT":
                client.close();
                System.exit(0);
                break;
            default:
                System.out.println("Unknown command: " + command);
                break;
        }
    }
}
