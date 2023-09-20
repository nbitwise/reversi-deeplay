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
    void testRequestRegistration() throws IOException {
        final RegistrationRequest registrationRequest1 = new RegistrationRequest("andrew");
        final String serverWord1 = writeAndRead(registrationRequest1);

        convertedObject = new Gson().fromJson(serverWord1, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("You was successfully registered", convertedObject.get("message").getAsString());

        final RegistrationRequest registrationRequest2 = new RegistrationRequest("andrew");
        final String serverWord2 = writeAndRead(registrationRequest2);

        convertedObject = new Gson().fromJson(serverWord2, JsonObject.class);

        Assertions.assertEquals("fail", convertedObject.get("status").getAsString());

        final RegistrationRequest registrationRequest3 = new RegistrationRequest("ilya");
        final String serverWord3 = writeAndRead(registrationRequest3);

        convertedObject = new Gson().fromJson(serverWord3, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("You was successfully registered", convertedObject.get("message").getAsString());

    }

    @Test
    void testRequestCreateRoom() throws IOException {
        final RegistrationRequest registrationRequest = new RegistrationRequest("andrew");
        writeAndRead(registrationRequest);

        final AuthorizationRequest authorizationRequest = new AuthorizationRequest("andrew");
        writeAndRead(authorizationRequest);

        final CreateRoomRequest request = new CreateRoomRequest();
        final String serverWord = writeAndRead(request);

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);
        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("1", convertedObject.get("roomId").getAsString());

    }

    @Test
    void testConnectToRoom() throws IOException {

        final RegistrationRequest registrationRequest = new RegistrationRequest("andrew");
        writeAndRead(registrationRequest);

        final AuthorizationRequest authorizationRequest = new AuthorizationRequest("andrew");
        writeAndRead(authorizationRequest);

        final LeaveRoomRequest leaveRoomRequest1 = new LeaveRoomRequest();
        writeAndRead(leaveRoomRequest1);

        final CreateRoomRequest createRoomRequest = new CreateRoomRequest();
        writeAndRead(createRoomRequest);

        final LeaveRoomRequest leaveRoomRequest2 = new LeaveRoomRequest();
        writeAndRead(leaveRoomRequest2);

        final ConnectToRoomRequest connectToRoomRequest = new ConnectToRoomRequest(1);
        final String serverWord = writeAndRead(connectToRoomRequest);

        final ConnectToRoomResponse response = gson.fromJson(serverWord, ConnectToRoomResponse.class);

        Assertions.assertEquals("success", response.status);
        Assertions.assertEquals("Connected to room as WhitePlayer", response.message);
    }

    @Test
    void testConnectToRoomRoomNotFound() throws IOException {

        final ConnectToRoomRequest request = new ConnectToRoomRequest(999);
        final String serverWord = writeAndRead(request);
        final ConnectToRoomResponse response = gson.fromJson(serverWord, ConnectToRoomResponse.class);

        Assertions.assertEquals("fail", response.status);
        Assertions.assertEquals("you are not logged in", response.message);
    }


    @Test
    void testLeaveRoom() throws IOException {

        final RegistrationRequest registrationRequest = new RegistrationRequest("andrew");
        writeAndRead(registrationRequest);

        final AuthorizationRequest authorizationRequest = new AuthorizationRequest("andrew");
        writeAndRead(authorizationRequest);

        final CreateRoomRequest createRoomRequest = new CreateRoomRequest();
        writeAndRead(createRoomRequest);

        final LeaveRoomRequest leaveRoomRequest = new LeaveRoomRequest();
        final String serverWord = writeAndRead(leaveRoomRequest);

        final LeaveRoomResponse response = gson.fromJson(serverWord, LeaveRoomResponse.class);

        Assertions.assertEquals("success", response.status);
    }

    @Test
    void testRequestGameOver() throws IOException {
        final RegistrationRequest request1 = new RegistrationRequest("andrew");
        writeAndRead(request1);

        final AuthorizationRequest request = new AuthorizationRequest("andrew");
        writeAndRead(request);

        final CreateRoomRequest request2 = new CreateRoomRequest();
        writeAndRead(request2);

        final GameoverRequest gameoverRequest = new GameoverRequest();
        final String serverWord = writeAndRead(gameoverRequest);

        final JsonObject convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("game is over. You are not in room anymore", convertedObject.get("message").getAsString());
    }

    private String writeAndRead(Request request) throws IOException {
        String requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        return in.readLine();
    }
}




