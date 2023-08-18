package client;



import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MockServer {

    public static final int PORT = 6070;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String nextResponse;
    private String lastReceived;

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            clientSocket = serverSocket.accept();
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            writer.close();
            reader.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNextResponse(String response) {
        nextResponse = response;
    }

    public String getLastReceived() {
        return lastReceived;
    }

    public void waitForRequest() {
        try {
            lastReceived = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendResponse() {
        try {
            writer.write(nextResponse);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
