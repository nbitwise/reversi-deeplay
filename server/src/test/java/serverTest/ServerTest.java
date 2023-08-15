package serverTest;


import java.io.*;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.deeplay.Board;
import org.junit.jupiter.api.*;
import request.*;

import java.io.IOException;


class ServerTest {

    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;
    private static String requestString;
    private static JsonObject convertedObject;
    static Gson gson = new Gson();

    @BeforeAll
    public void setup() throws IOException {
        clientSocket = new Socket("localhost", 6070);

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

    }

    @AfterAll
    public void close() throws IOException {
        clientSocket.close();
        in.close();
        out.close();
    }

    @Test
    public void testRequestRegistration() throws IOException {
        RegistrationRequest request = new RegistrationRequest("andrew");
        requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("You was successfully registered", convertedObject.get("message").getAsString());

    }

    @Test
    public void testRequestAuthorization() throws IOException {
        AuthorizationRequest request = new AuthorizationRequest("andrew");
        requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("You was successfully authorization", convertedObject.get("message").getAsString());

    }

    @Test
    public void testRequestChooseDifficulty() throws IOException {
        ChooseDifficultyRequest request = new ChooseDifficultyRequest("easy");
        requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("You choose easy", convertedObject.get("message").getAsString());

    }

    @Test
    public void testRequestChooseConnectToRoom() throws IOException {

        GameVsHumanConnectToRoomRequest request = new GameVsHumanConnectToRoomRequest("1");
        requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("You connected successfully", convertedObject.get("message").getAsString());

    }

    @Test
    public void testRequestCreateRoom() throws IOException {

        GameVsHumanCreateRoomRequest request = new GameVsHumanCreateRoomRequest("white", "20");
        requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("You create room", convertedObject.get("message").getAsString());
        Assertions.assertEquals("1", convertedObject.get("roomID").getAsString());

    }

    @Test
    public void testRequestFindRoom() throws IOException {

        GameVsHumanFindRoomRequest request = new GameVsHumanFindRoomRequest("white");
        requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("here is your room", convertedObject.get("message").getAsString());

    }


    @Test
    public void testRequestGameVsHuman() throws IOException {

        GameVsHumanCreateRoomRequest request = new GameVsHumanCreateRoomRequest("white", "20");
        requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("room created", convertedObject.get("message").getAsString());
        Assertions.assertEquals("1", convertedObject.get("roomID").getAsString());

    }

    @Test
    public void testRequestMoveInGame() throws IOException {
        GameMoveRequest request = new GameMoveRequest("dss", "20", 3, 4);
        requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();
        Board board = new Board();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("move has been made", convertedObject.get("message").getAsString());
        Assertions.assertEquals(board, convertedObject.get("board").getAsString());

    }

    @Test
    public void testRequestPlayVsBot() throws IOException {

        PlayVsBotRequest request = new PlayVsBotRequest();
        requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("you choose play vs bot", convertedObject.get("message").getAsString());
    }

    @Test
    public void testRequestViewAvailableRooms() throws IOException {

        ViewAvailableRoomsRequest request = new ViewAvailableRoomsRequest();
        requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("Список ID свободных комнат", convertedObject.get("message").getAsString());

    }

}


