package client;

import com.google.gson.Gson;
import clientrequest.*;
import clientresponse.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import logic.Board;
import logic.Move;
import org.jline.reader.*;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    private final Socket socket;
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;
    private final Gson gson;
    //  private final ExecutorService executorService;

    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        gson = new Gson();
        //     executorService = Executors.newFixedThreadPool(2); // Adjust the thread pool size as needed
    }

    public void sendRequest(Request request) throws IOException {
        String jsonRequest = gson.toJson(request);
        bufferedWriter.write(jsonRequest);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }


    public void sendMessage() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (socket.isConnected()) {
            System.out.println("ya gotov chitat soobshenie");
            String msg = scanner.nextLine();
            System.out.println("ya prochital");
            handleCommand(this,msg);
        }
    }

    public void getMessage() {
        new Thread(() -> {
            String msg;
            while (socket.isConnected()) {
                try {
                    msg = bufferedReader.readLine();
                    handleServerMessage(this, msg);
                    /*JsonObject messageAsJson = JsonParser.parseString(msg).getAsJsonObject();
                    if (msg != null) {
                        handleServerMessage(this, msg); // Обрабатываем сообщение с сервера
                    }
                    if (messageAsJson.has("messageName")) {
                        if (messageAsJson.get("messageName").getAsString().equals("WhereIcanGoResponse")) {
                            Gson gson = new Gson();
                            WhereIcanGoResponse whereIcanGoResponse = gson.fromJson(msg, WhereIcanGoResponse.class);
                            System.out.println("davai hodi");
                            MakeMoveRequest makeMoveRequest = makeMove(whereIcanGoResponse);
                            sendRequest(makeMoveRequest);
                        }
                    }*/


                    System.out.println(msg);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }).start();
    }

    public <T extends Response> T getResponse(Class<T> responseType) throws IOException {
        String jsonResponse = null;
        while (jsonResponse == null) {
            jsonResponse = bufferedReader.readLine();
        }
        return gson.fromJson(jsonResponse, responseType);
    }

    public void close() throws IOException {
        socket.close();
        //     executorService.shutdown();
    }

    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 6070);

            ExecutorService executorService = Executors.newFixedThreadPool(2);

            client.getMessage(); // Запускаем поток для чтения сообщений с сервера

            executorService.submit(() -> {
                Scanner scanner = new Scanner(System.in);
                boolean exitRequested = false;
                while (!exitRequested) {
                    System.out.print("Enter command: ");
                    String input = scanner.nextLine().trim();
                    if (!input.isEmpty()) {
                        if (input.equalsIgnoreCase("exit")) {
                            exitRequested = true;
                        } else {
                            try {
                                handleCommand(client, input); // Обрабатываем команду и отправляем ответ на сервер
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                try {
                    client.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            executorService.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleServerMessage(Client client, String message) {
        JsonObject messageAsJson = JsonParser.parseString(message).getAsJsonObject();
        if (messageAsJson.has("messageName")) {
            String messageName = messageAsJson.get("messageName").getAsString();
            switch (messageName) {
                case "RegistrationResponse":
                    RegistrationResponse registrationResponse = gson.fromJson(message, RegistrationResponse.class);
                    System.out.println("Received Registration Response: " + registrationResponse.status + " - " + registrationResponse.message);
                    break;
                // Добавьте обработку других типов сообщений по аналогии
                default:
                    System.out.println("Unknown message received: " + messageName);
            }
        } else {
            System.out.println("Invalid message format: " + message);
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
            /*case "STARTGAME":
                startGameRequest startGamRequest = new startGameRequest();//автоматический запрос на старт игры)хотя по факту она уже стартанула, это же в руме все... мб переделать, что старт игры не в руме, а именно при запуске этой команды
                client.sendRequest(startGamRequest);
                break;
            case "MAKEMOVES":
                while (true) { //пока есть ходы
                    WhereIcanGoResponse whereIcanGoResponse = client.getResponse(WhereIcanGoResponse.class);
                    if (whereIcanGoResponse == null) continue;

                    //сдесь должен быть реализован вывод доски и ходы
                    System.out.println("davai hodi");
                    MakeMoveRequest makeMoveRequest = makeMove(whereIcanGoResponse);
                    client.sendRequest(makeMoveRequest);//отправляет свой ход
                    //  MadeMoveResponse madeMoveResponse = client.getResponse(MadeMoveResponse.class);
                    //   System.out.println(madeMoveResponse.message);
                }*/
            default:
                System.out.println("Unknown command: " + command);
        }
    }

  /*  void startGame(int roomId) throws IOException {
        startGameRequest gameVsHumanRequest = new startGameRequest();//автоматический запрос на старт игры)хотя по факту она уже стартанула, это же в руме все... мб переделать, что старт игры не в руме, а именно при запуске этой команды
        sendRequest(gameVsHumanRequest);
        while (true){ //пока есть ходы
            WhereIcanGoResponse whereIcanGoResponse=getResponse(WhereIcanGoResponse.class);
            MakeMoveRequest makeMoveRequest = makeMove(whereIcanGoResponse);
            sendRequest(makeMoveRequest);//отправляет свой ход
        }
//пока игра идет, тут должен быть цикл, который принимает доску, т отправляет свой ход
    }*/

   /* public static MakeMoveRequest makeMove(WhereIcanGoResponse whereIcanGoResponse) throws IOException {

        List<Move> availableMoves = whereIcanGoResponse.getAvailableMoves();
        Board board = whereIcanGoResponse.getBoard();
        //   final List<Move> availableMoves = board.getAllAvailableMoves(playerCell);
        System.out.println("Доступные ходы: ");
        for (Move m : availableMoves) {
            System.out.println(m.row + 1 + " " + (m.col + 1));
        }

        while (true) {
            System.out.print("Введите строку и столбец (например, 2 3): ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            String[] inputArray = input.trim().split("\\s+");
            if (inputArray.length == 2) {
                try {
                    int row = Integer.parseInt(inputArray[0]) - 1;
                    int col = Integer.parseInt(inputArray[1]) - 1;
                    final Move move = new Move(row, col);
                    if (availableMoves.contains(move)) {
                        return new MakeMoveRequest(move, whereIcanGoResponse.getCell());
                    } else {
                        System.out.println("Недопустимый ход! Пожалуйста, выберите из доступных ходов.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Недопустимый ввод! Пожалуйста, введите два числа через пробел.");
                }
            } else {
                System.out.println("Недопустимый ввод! Пожалуйста, введите два числа через пробел.");
            }
        }
    }*/

}
