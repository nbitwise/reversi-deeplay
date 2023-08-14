package serverTest;



import java.io.*;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;


class ServerTest {

    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;

    @Test
    public void testRequestRegistration() {
        try {
            try {

                clientSocket = new Socket("localhost", 6070);

                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                String testRequest = "{\"command\":\"registration\",\"nickname\":\"Andrew\"}";
                out.write(testRequest);
                out.newLine();
                out.flush();

                String serverWord = in.readLine();

                JsonObject convertedObject = new Gson().fromJson(serverWord, JsonObject.class);

                Assertions.assertEquals("success", convertedObject.get("status").getAsString());
                Assertions.assertEquals("You was successfully registered", convertedObject.get("message").getAsString());

            } finally {

                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }
}


