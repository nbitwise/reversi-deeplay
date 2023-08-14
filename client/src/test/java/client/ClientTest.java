package client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import request.*;
import response.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
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
            throw new RuntimeException(e);
        }

        gson = new Gson();
    }

    @AfterEach
    public void tearDown() {
        try {
            client.close();
            mockServer.stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRegistrationRequest() throws IOException {
        RegistrationRequest request = new RegistrationRequest("user123");

        client.sendRequest(request);
        String expectedJson = gson.toJson(request) + "\n";
        assertEquals(expectedJson, mockServer.getLastReceived());
    }

    @Test
    void testRegistrationResponseParsing() throws IOException {
        RegistrationResponse registrationResponse = new RegistrationResponse("success", "Registered successfully");
        String jsonResponse = gson.toJson(registrationResponse);

        mockServer.setNextResponse(jsonResponse);

        Response response = client.getResponse();
        assertTrue(response instanceof RegistrationResponse);

        RegistrationResponse parsedResponse = (RegistrationResponse) response;
        assertEquals("success", parsedResponse.status);
        assertEquals("Registered successfully", parsedResponse.message);
    }
}
