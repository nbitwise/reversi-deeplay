package io.deeplay.Aplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.Gson;


class ClientTest {

    private MockServer mockServer;
    private Client client;
    private Gson gson;

    @BeforeEach
    public void setup() {
        mockServer = new MockServer();
        mockServer.start();

        try {
            client = new Client("localhost", MockServer.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        gson = new Gson();
    }

    @AfterEach
    public void tearDown() {
        try {
            client.close();
            mockServer.stop();

            Files.deleteIfExists(Paths.get("path/to/temp/file"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testRegistrationRequest() {
        RegistrationRequest request = new RegistrationRequest("user123");

        try {
            client.sendRequest((Request) request);
            String expectedJson = gson.toJson(request) + "\n";
            assertEquals(expectedJson, mockServer.getLastReceived());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testRegistrationResponseParsing() {
        String jsonResponse = "{\"status\":\"success\",\"message\":\"Registered successfully\"}";
        mockServer.setNextResponse(jsonResponse);

        try {
            Response response = client.getResponse();
            assertEquals(RegistrationResponse.class, response.getClass());

            RegistrationResponse registrationResponse = gson.fromJson(jsonResponse, RegistrationResponse.class);
            assertEquals("success", registrationResponse.status);
            assertEquals("Registered successfully", registrationResponse.message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
