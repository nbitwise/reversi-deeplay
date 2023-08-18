package client;

import com.google.gson.Gson;
import clientrequest.*;
import clientresponse.*;
import org.jline.reader.*;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
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

    public <T extends Response> T getResponse(Class<T> responseType) throws IOException {
        String jsonResponse = bufferedReader.readLine();
        return gson.fromJson(jsonResponse, responseType);
    }

    public void close() throws IOException {
        socket.close();
        executorService.shutdown();
    }

    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 6070);

            Scanner scanner = new Scanner(System.in);
            boolean exitRequested = false;

            while (!exitRequested) {
                System.out.print("Enter command: ");
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) {
                    if (input.equalsIgnoreCase("exit")) {
                        exitRequested = true;
                    } else {
                        handleCommand(client, input);
                    }
                }
            }

            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void handleCommand(Client client, String input) throws IOException {
        String[] commandParts = input.split("\\s+");
        String command = commandParts[0];

        switch (command) {
            case "REGISTRATION":
                if (commandParts.length > 1) {
                    String nickname = commandParts[1];
                    RegistrationRequest registrationRequest = new RegistrationRequest(nickname);
                    client.sendRequest(registrationRequest);
                    RegistrationResponse registrationResponse = client.getResponse(RegistrationResponse.class);
                    System.out.println("Registration response: " + registrationResponse.message);
                } else {
                    System.out.println("Usage: register <nickname>");
                }
                break;
            case "AUTHORIZATION":
                if (commandParts.length > 1) {
                    String nickname = commandParts[1];
                    AuthorizationRequest authorizationRequest = new AuthorizationRequest(nickname);
                    client.sendRequest(authorizationRequest);
                    AuthorizationResponse authorizationResponse = client.getResponse(AuthorizationResponse.class);
                    System.out.println("Authorization response: " + authorizationResponse.message);
                } else {
                    System.out.println("Usage: login <nickname>");
                }
                break;
            case "CREATEROOM":
                CreateRoomRequest createRoomRequest = new CreateRoomRequest();
                client.sendRequest(createRoomRequest);
                CreateRoomResponse createRoomResponse = client.getResponse(CreateRoomResponse.class);
                System.out.println("Create room response: " + createRoomResponse.message + ", Room ID: " + createRoomResponse.getRoomId());
                break;
            case "CONNECTTOROOM":
                if (commandParts.length > 1) {
                    int roomId = Integer.parseInt(commandParts[1]);
                    ConnectToRoomRequest connectToRoomRequest = new ConnectToRoomRequest(roomId);
                    client.sendRequest(connectToRoomRequest);
                    ConnectToRoomResponse connectToRoomResponse = client.getResponse(ConnectToRoomResponse.class);
                    System.out.println("Connect to room response: " + connectToRoomResponse.message);
                } else {
                    System.out.println("Usage: connecttoroom <room_id>");
                }
                break;
            case "LEAVEROOM":
                LeaveRoomRequest leaveRoomRequest = new LeaveRoomRequest();
                client.sendRequest(leaveRoomRequest);
                LeaveRoomResponse leaveRoomResponse = client.getResponse(LeaveRoomResponse.class);
                System.out.println("Leave room response: " + leaveRoomResponse.message);
                break;
            case "EXIT":
                client.close();
                System.exit(0);
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

}
