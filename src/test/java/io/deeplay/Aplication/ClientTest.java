package io.deeplay.Aplication;


import static io.deeplay.Aplication.Client.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


class ClientTest {

    private MockServer mockServer;
    private Client client;

    @BeforeEach
    public void setup() {
        mockServer = new MockServer();
        mockServer.start();

        try {
            client = new Client("localhost", MockServer.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        Client.RegistrationRequest request = new Client.RegistrationRequest("user123");

        try {
            client.sendRequest(request);
            String expectedJson = "{\"command\":\"registration\",\"nickname\":\"user123\"}\n";
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
            Client.Response response = client.getResponse();
            assertEquals(RegistrationResponse.class, response.getClass());

            RegistrationResponse registrationResponse = (RegistrationResponse) response;
            assertEquals("success", registrationResponse.status);
            assertEquals("Registered successfully", registrationResponse.message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
