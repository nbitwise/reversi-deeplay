package serverTest;


import java.io.*;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import logic.Board;
import org.junit.jupiter.api.*;
import serverrequest.*;

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

        System.out.println(convertedObject.get("message").getAsString());

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("You was successfully registered", convertedObject.get("message").getAsString());

        request = new RegistrationRequest("andrew");
        requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        System.out.println(convertedObject.get("message").getAsString());

        Assertions.assertEquals("fail", convertedObject.get("status").getAsString());


        request = new RegistrationRequest("ilya");
        requestString = gson.toJson(request);
        out.write(requestString);
        out.newLine();
        out.flush();

        serverWord = in.readLine();

        convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

        System.out.println(convertedObject.get("message").getAsString());

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

        String serverWord1 = in.readLine();

        convertedObject = new Gson().fromJson(serverWord1, JsonObject.class);

        Assertions.assertEquals("success", convertedObject.get("status").getAsString());
        Assertions.assertEquals("You was successfully authorization", convertedObject.get("message").getAsString());

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
        //Assertions.assertEquals("You was successfully authorization", convertedObject.get("message").getAsString());

    }

}


