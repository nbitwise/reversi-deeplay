package serverTest;


import java.io.*;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import logic.Board;
import org.junit.jupiter.api.*;
import serverrequest.*;
import serverresponses.ConnectToRoomResponse;
import serverresponses.CreateRoomResponse;
import serverresponses.LeaveRoomResponse;

import java.io.IOException;


class ServerTest {

    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;
    private static String requestString;
    private static JsonObject convertedObject;
    static Gson gson = new Gson();

    @BeforeAll
    public static void setup() throws IOException {
        clientSocket = new Socket("localhost", 6070);

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

    }

    @AfterAll
    public static void close() throws IOException {
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

        request = new RegistrationRequest("andrew");
        requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("fail", convertedObject.get("status").getAsString());

        request = new RegistrationRequest("ilya");
        requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("You was successfully registered", convertedObject.get("message").getAsString());

    }

    @Test
    public void testRequestAuthorization() throws IOException {
        RegistrationRequest request1 = new RegistrationRequest("andrew");
        requestString = gson.toJson(request1);
        out.write(requestString);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        AuthorizationRequest request = new AuthorizationRequest("andrew");
        String requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        String serverWord1 = in.readLine();

        JsonObject convertedObject = new Gson().fromJson(serverWord1, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("you have successfully logged in", convertedObject.get("message").getAsString());

    }
    @Test
    public void testRequestCreateRoom() throws IOException {
        CreateRoomRequest request = new CreateRoomRequest();
        requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);
        System.out.println(convertedObject.get("message").getAsString());
        System.out.println(convertedObject.get("status").getAsString());
        System.out.println(convertedObject.get("roomId").getAsString());

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("1", convertedObject.get("roomId").getAsString());

    }
    @Test
    public void testConnectToRoom() throws IOException {
        CreateRoomRequest request1 = new CreateRoomRequest();
        requestString = gson.toJson(request1);
        out.write(requestString);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        ConnectToRoomRequest request = new ConnectToRoomRequest(1);
        String requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        String serverWord1 = in.readLine();

        ConnectToRoomResponse response = gson.fromJson(serverWord1, ConnectToRoomResponse.class);

        Assertions.assertEquals("success", response.getStatus());
        Assertions.assertEquals("Connected to room as WhitePlayer", response.getMessage());
    }
    @Test
    public void testConnectToRoomRoomNotFound() throws IOException {
        ConnectToRoomRequest request = new ConnectToRoomRequest(999);
        String requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();
        ConnectToRoomResponse response = gson.fromJson(serverWord, ConnectToRoomResponse.class);

        Assertions.assertEquals("fail", response.getStatus());
        Assertions.assertEquals("Room with ID 999 not found", response.getMessage());
    }

    @Test
    public void testConnectToRoomAlreadyOccupied() throws IOException {
        CreateRoomRequest createRequest = new CreateRoomRequest();
        String createRequestString = gson.toJson(createRequest);
        out.write(createRequestString);
        out.newLine();
        out.flush();
        String createResponseString = in.readLine();
        CreateRoomResponse createResponse = gson.fromJson(createResponseString, CreateRoomResponse.class);

        ConnectToRoomRequest connectRequest = new ConnectToRoomRequest(createResponse.getRoomId());
        String connectRequestString = gson.toJson(connectRequest);
        out.write(connectRequestString);
        out.newLine();
        out.flush();
        String connectResponseString = in.readLine();
        ConnectToRoomResponse connectResponse = gson.fromJson(connectResponseString, ConnectToRoomResponse.class);


        ConnectToRoomRequest connectRequest2 = new ConnectToRoomRequest(createResponse.getRoomId());
        String connectRequestString2 = gson.toJson(connectRequest2);
        out.write(connectRequestString2);
        out.newLine();
        out.flush();
        String connectResponseString2 = in.readLine();
        ConnectToRoomResponse connectResponse2 = gson.fromJson(connectResponseString2, ConnectToRoomResponse.class);

        Assertions.assertEquals("fail", connectResponse2.getStatus());
        Assertions.assertEquals("Room with ID " + createResponse.getRoomId() + " is already occupied", connectResponse2.getMessage());
    }

    @Test
    public void testLeaveRoom() throws IOException {
        CreateRoomRequest request1 = new CreateRoomRequest();
        requestString = gson.toJson(request1);
        out.write(requestString);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        LeaveRoomRequest request = new LeaveRoomRequest();
        String requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        // Читаем ответ от сервера
        String serverWord1 = in.readLine();

        LeaveRoomResponse response = gson.fromJson(serverWord1, LeaveRoomResponse.class);

        Assertions.assertEquals("success", response.getStatus());
    }
    @Test
    public void testRequestGameover() throws IOException {
        RegistrationRequest request1 = new RegistrationRequest("andrew");
        requestString = gson.toJson(request1);
        out.write(requestString);
        out.newLine();
        out.flush();

        String serverWord = in.readLine();

        AuthorizationRequest request = new AuthorizationRequest("andrew");
        String requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        String serverWord1 = in.readLine();

        CreateRoomRequest request2 = new CreateRoomRequest();

        requestString = gson.toJson(request2);
        out.write(requestString);
        out.newLine();
        out.flush();

        serverWord1 = in.readLine();

        GameoverRequest requestgo = new GameoverRequest();

        String requestStringgo = gson.toJson(requestgo);
        out.write(requestStringgo);
        out.newLine();
        out.flush();

        String serverWordgo = in.readLine();

        JsonObject convertedObject = new Gson().fromJson(serverWordgo, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("game is over. You are not in room anymore", convertedObject.get("message").getAsString());
    }
}




