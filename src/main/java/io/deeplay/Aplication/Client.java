package io.deeplay.Aplication;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;




public class Client {

    interface Request {
    }

    static class RegistrationRequest implements Request {
        String command = "registration";
        String nickname;

        RegistrationRequest(String nickname) {
            this.nickname = nickname;
        }
    }

    static class AuthorizationRequest implements Request {
        String command = "authorization";
        String nickname;

        AuthorizationRequest(String nickname) {
            this.nickname = nickname;
        }
    }

    static class ViewAvailableRoomsRequest implements Request {
        String command = "viewAvailableRooms";
    }

    static class ViewStartedGamesRequest implements Request {
        String command = "viewStartedGames";
    }

    static class ConnectForViewRequest implements Request {
        String command = "connectForView";
    }

    static class GameVsHumanCreateRoomRequest implements Request {
        String command = "createRoom";
        String color;
        String timeControl;

        GameVsHumanCreateRoomRequest(String color, String timeControl) {
            this.color = color;
            this.timeControl = timeControl;
        }
    }

    static class GameVsHumanFindRoomRequest implements Request {
        String command = "findRoom";
        String color;

        GameVsHumanFindRoomRequest(String color) {
            this.color = color;
        }
    }

    static class GameVsHumanConnectToRoomRequest implements Request {
        String command = "connectToRoom";
        String roomId;

        GameVsHumanConnectToRoomRequest(String roomId) {
            this.roomId = roomId;
        }
    }

    static class PlayVsBotRequest implements Request {
        String command = "playWithBot";
    }

    static class ChooseColorRequest implements Request {
        String command = "chooseColor";
        String color;

        ChooseColorRequest(String color) {
            this.color = color;
        }
    }

    static class ChooseDifficultyRequest implements Request {
        String command = "chooseDifficulty";
        String difficulty;

        ChooseDifficultyRequest(String difficulty) {
            this.difficulty = difficulty;
        }
    }

    static class GameMoveRequest implements Request {
        String command = "makeMove";
        String playerId;
        String roomId;
        int row;
        int col;

        GameMoveRequest(String playerId, String roomId, int row, int col) {
            this.playerId = playerId;
            this.roomId = roomId;
            this.row = row;
            this.col = col;
        }
    }

    static class GameOverWantReMatchRequest implements Request {
        String command = "wantReMatch";
    }

    static class GameOverDontWantReMatchRequest implements Request {
        String command = "DontWantReMatch";
    }


    interface Response {
    }

    static class RegistrationResponse implements Response {
        String status;
        String message;

        RegistrationResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }
    }

    static class AuthorizationResponse implements Response {
        String status;

        AuthorizationResponse(String status) {
            this.status = status;
        }
    }

    static class ViewAvailableRoomsResponse implements Response {
        String status;
        String message;
        List<String> roomIds;

        ViewAvailableRoomsResponse(String status, String message, List<String> roomIds) {
            this.status = status;
            this.message = message;
            this.roomIds = roomIds;
        }
    }

    static class ViewStartedGamesResponse implements Response {
        String status;
        String message;
        List<String> roomIds;

        ViewStartedGamesResponse(String status, String message, List<String> roomIds) {
            this.status = status;
            this.message = message;
            this.roomIds = roomIds;
        }
    }

    static class ConnectForViewResponse implements Response {
        String status;
        String message;

        ConnectForViewResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }
    }

    static class GameVsHumanCreateRoomResponse implements Response {
        String status;
        String roomId;

        GameVsHumanCreateRoomResponse(String status, String roomId) {
            this.status = status;
            this.roomId = roomId;
        }
    }

    static class GameVsHumanFindRoomResponse implements Response {
        String status;
        List<AvailableRoomInfo> availableRooms;

        static class AvailableRoomInfo {
            String roomId;
            String timeControl;
        }

        GameVsHumanFindRoomResponse(String status, List<AvailableRoomInfo> availableRooms) {
            this.status = status;
            this.availableRooms = availableRooms;
        }
    }

    static class GameVsHumanConnectToRoomResponse implements Response {
        String status;
        String message;

        GameVsHumanConnectToRoomResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }
    }

    static class PlayVsBotResponse implements Response {
        String status;

        PlayVsBotResponse(String status) {
            this.status = status;
        }
    }

    static class ChooseColorResponse implements Response {
        String status;
        String message;

        ChooseColorResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }
    }


    static class ChooseDifficultyResponse implements Response {
        String status;
        String message;

        ChooseDifficultyResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }
    }


    static class GameMoveResponse implements Response {
        String status;
        String board;

        GameMoveResponse(String status, String board) {
            this.status = status;
            this.board = board;
        }
    }

    static class GameOverWantReMatchResponse implements Response {
        String status;
        String message;

        GameOverWantReMatchResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }
    }

    static class GameOverDoYouWantReMatchResponse implements Response {
        String status;
        String message;

        GameOverDoYouWantReMatchResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }
    }

    private final Socket socket;
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;
    private final Gson gson;

    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        gson = new Gson();
    }

    public void sendRequest(Request request) throws IOException {
        String jsonRequest = gson.toJson(request);
        bufferedWriter.write(jsonRequest);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    public Response getResponse() throws IOException {
        String jsonResponse = bufferedReader.readLine();
        return gson.fromJson(jsonResponse, Response.class);
    }

    public void close() throws IOException {
        socket.close();
    }

    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 6070);

            // Пример использования:
            RegistrationRequest registrationRequest = new RegistrationRequest("user123");
            client.sendRequest(registrationRequest);

            Response response = client.getResponse();
            if (response instanceof RegistrationResponse) {
                RegistrationResponse registrationResponse = (RegistrationResponse) response;
                System.out.println("Registration Status: " + registrationResponse.status);
                System.out.println("Registration Message: " + registrationResponse.message);
            }



            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}