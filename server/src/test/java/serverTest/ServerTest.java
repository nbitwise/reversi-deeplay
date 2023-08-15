package serverTest;



import java.io.*;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.deeplay.Board;
import org.junit.jupiter.api.*;

import java.io.IOException;


class ServerTest {

    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;
    private static JsonObject convertedObject;

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

            final String testRequest = "{\"command\":\"registration\",\"nickname\":\"Andrew\"}";
            out.write(testRequest);
            out.newLine();
            out.flush();

            String serverWord = in.readLine();

            convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

            Assertions.assertEquals("success", convertedObject.get("status").getAsString());
            Assertions.assertEquals("You was successfully registered", convertedObject.get("message").getAsString());

        }

    @Test
    public void testRequestAuthorization() throws IOException {

        final String testRequest = "{\"command\":\"authorization\",\"nickname\":\"Andrew\"}";
        out.write(testRequest);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("You was successfully authorization", convertedObject.get("message").getAsString());

    }

    @Test
    public void testRequestChooseColor() throws IOException {

        final String testRequest = "{\"command\":\"chooseColor\",\"color\":\"black\"}";
        out.write(testRequest);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("You choose black", convertedObject.get("message").getAsString());

    }

    @Test
    public void testRequestChooseDifficulty() throws IOException {

        final String testRequest = "{\"command\":\"chooseDifficulty\",\"difficulty\":\"easy\"}";
        out.write(testRequest);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("You choose easy", convertedObject.get("message").getAsString());

    }

    @Test
    public void testRequestChooseConnectToRoom() throws IOException {

        final String testRequest = "{\"command\":\"connectToRoom\",\"roomId\":\"id\"}";
        out.write(testRequest);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("You connected successfully", convertedObject.get("message").getAsString());

    }

    @Test
    public void testRequestCreateRoom() throws IOException {

        final String testRequest = "{\"command\":\"createRoom\"}";
        out.write(testRequest);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("You create room", convertedObject.get("message").getAsString());
        Assertions.assertEquals("1", convertedObject.get("roomID").getAsString());

    }

    @Test
    public void testRequestDisconnection() throws IOException {

        final String testRequest = "{\"command\":\"leaveRoom\"}";
        out.write(testRequest);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("You disconnected successfully", convertedObject.get("message").getAsString());

    }

    @Test
    public void testRequestFindRoom() throws IOException {

        final String testRequest = "{\"command\":\"findRoom\", \"roomId\" : \"id\"}";
        out.write(testRequest);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("here is your room", convertedObject.get("message").getAsString());

    }

    @Test
    public void testRequestGameOver() throws IOException {

        final String testRequest = "{\"command\":\"wantReMatch\"}";
        out.write(testRequest);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("другой игрок отказался от новой игры", convertedObject.get("message").getAsString());

    }

    @Test
    public void testRequestGameVsHuman() throws IOException {

        final String testRequest = "{\"command\":\"createRoom\"}";
        out.write(testRequest);
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

        final String testRequest = "{\"command\":\"makeMove\"\"row\": \"row\",\"col\" : \"col\"}";
        out.write(testRequest);
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

        final String testRequest = "{\"command\":\"playWithBot\"}";
        out.write(testRequest);
        out.newLine();
        out.flush();
        Board board = new Board();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("you choose play vs bot", convertedObject.get("message").getAsString());
    }

    @Test
    public void testRequestViewAvailableRooms() throws IOException {

        final String testRequest = "{\"command\":\"viewAvailableRooms\"}";
        out.write(testRequest);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("Список ID свободных комнат", convertedObject.get("message").getAsString());

    }

}


