package client;

import clientrequest.*;
import clientresponse.*;
import com.google.gson.Gson;
import logic.Board;
import logic.Move;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client2 {

    private final Socket socket;
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;
    private final Gson gson;
    private final ExecutorService executorService;

    public Client2(String host, int port) throws IOException {
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
            Client2 client = new Client2("localhost", 6070);

            Scanner scanner = new Scanner(System.in);
            boolean exitRequested = false;
            handleCommand(client, "REGISTRATION p2");
            handleCommand(client, "AUTHORIZATION p2");
            handleCommand(client, "CONNECTTOROOM 1");
            handleCommand(client, "STARTGAME2");
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


    private static void handleCommand(Client2 client, String input) throws IOException {
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
            case "STARTGAME":
                startGameRequest startGamRequest = new startGameRequest();//автоматический запрос на старт игры)хотя по факту она уже стартанула, это же в руме все... мб переделать, что старт игры не в руме, а именно при запуске этой команды
                client.sendRequest(startGamRequest);
                while (true) { //пока есть ходы
                    WhereIcanGoResponse whereIcanGoResponse = client.getResponse(WhereIcanGoResponse.class);
                    //сдесь должен быть реализован вывод доски и ходы
                    System.out.println("davai hodi");
                    MakeMoveRequest makeMoveRequest = makeMove(whereIcanGoResponse);
                    client.sendRequest(makeMoveRequest);//отправляет свой ход
                    MadeMoveResponse madeMoveResponse = client.getResponse(MadeMoveResponse.class);
                    System.out.println(madeMoveResponse.message);
                }
            case "STARTGAME2":
                while (true) { //пока есть ходы
                    WhereIcanGoResponse whereIcanGoResponse = client.getResponse(WhereIcanGoResponse.class);
                    if (whereIcanGoResponse==null) continue;
                    //pдесь должен быть реализован вывод доски и ходы
                    System.out.println("davai hodi");
                    MakeMoveRequest makeMoveRequest = makeMove(whereIcanGoResponse);
                    client.sendRequest(makeMoveRequest);//отправляет свой ход
                    //  MadeMoveResponse madeMoveResponse = client.getResponse(MadeMoveResponse.class);
                    //   System.out.println(madeMoveResponse.message);
                }
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

    public static MakeMoveRequest makeMove(WhereIcanGoResponse whereIcanGoResponse) throws IOException {

        List<Move> availableMoves = whereIcanGoResponse.getAvailableMoves();
       // Board board = whereIcanGoResponse.getBoard();
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
    }

}
