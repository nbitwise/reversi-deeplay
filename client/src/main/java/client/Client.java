package client;


import com.google.gson.Gson;
import clientrequest.*;
import clientresponse.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Objects;


public class Client {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(socket, client.socket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket);
    }

    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 6070);

            RegistrationRequest registrationRequest = new RegistrationRequest("user123");
            client.sendRequest(registrationRequest);

            RegistrationResponse response = (RegistrationResponse) client.getResponse();
            System.out.println("Registration Status: " + response.status);
            System.out.println("Registration Message: " + response.message);



            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


