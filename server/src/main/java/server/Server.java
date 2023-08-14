package server;

import responses.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Server {

    private static final int PORT = 6070;
    private static LinkedList<ClientProcessor> serverList = new LinkedList<>();

    public static void main(String[] args) throws IOException {

        try (ServerSocket server = new ServerSocket(PORT)) {

            List<Command> commands = new ArrayList<>();

            commands.add(Command.newCommand("REGISTRATION", (jsonRequest) -> {
                String nickname = jsonRequest.get("nickname").getAsString();
                if (nickname == null || !nickname.matches("^[a-zA-Z0-9_]+$")) {
                    return new ResponseRegistration("fail", "incorrect username");
                }
                return new ResponseRegistration("success", "You was successfully registered");
            }));

            commands.add(Command.newCommand("STOP", (jsonRequest) -> {
                try {
                    server.close();
                    return new ResponseRegistration("success", "server stopped");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));

            commands.add(Command.newCommand("ChooseColor", (jsonRequest) -> {
                return new ResponseChooseColor("status", "message");
            }));

            commands.add(Command.newCommand("ChooseDifficulty", (jsonRequest) -> {
                return new ResponseChooseDifficulty("status", "message");
            }));

            commands.add(Command.newCommand("ConnectToRoom", (jsonRequest) -> {
                return new ResponseConnectToRoom("status", "message");
            }));

            commands.add(Command.newCommand("CreateRoom", (jsonRequest) -> {
                int roomId = 1;
                return new ResponseCreateRoom("status", "message", roomId);
            }));

            commands.add(Command.newCommand("Disconnection", (jsonRequest) -> {
                return new ResponseDisconnection("status", "message");
            }));

            commands.add(Command.newCommand("FindRoom", (jsonRequest) -> {
                return new ResponseFindRoom("status", "message");
            }));

            commands.add(Command.newCommand("GameOver", (jsonRequest) -> {
                return new ResponseGameOver("status", "message");
            }));

            commands.add(Command.newCommand("GameVsHuman", (jsonRequest) -> {
                int roomId = 1;
                return new ResponseGameVsHuman("status", "message", roomId);
            }));

            commands.add(Command.newCommand("LeaveRoom", (jsonRequest) -> {
                return new ResponseLeaveRoom("status", "message");
            }));

            commands.add(Command.newCommand("MoveInGame", (jsonRequest) -> {
                Board board = new Board();
                return new ResponseMoveInGame("status", "message", board);
            }));

            commands.add(Command.newCommand("PlayVsBot", (jsonRequest) -> {
                return new ResponsePlayVsBot("status", "message");
            }));

            commands.add(Command.newCommand("ViewAvailableRooms", (jsonRequest) -> {
                ArrayList<Integer> listOfRooms = new ArrayList<>();
                return new ResponseViewAvailableRooms("status", "message", listOfRooms);
            }));


            try {
                while (!server.isClosed()) {
                    Socket socket = server.accept();
                    try {
                        serverList.add(new ClientProcessor(socket, commands));
                    } catch (IOException e) {

                        socket.close();
                    }
                }
            } finally {
                server.close();
            }
        }
    }

}

class ClientProcessor extends Thread {

    private final Map<String, Command> commands;
    private final BufferedReader socketBufferedReader;
    private final BufferedWriter socketBufferedWriter;

    public ClientProcessor(Socket socket, List<Command> commands) throws IOException {
        this.commands = commands.stream().collect(Collectors.toMap(Command::getName, Function.identity()));

        socketBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        socketBufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        String line;
        try {
            while (true) {
                line = socketBufferedReader.readLine();
                if (line == null) {
                    return;
                }
                JsonObject request = JsonParser.parseString(line).getAsJsonObject();
                final String commandName = request.get("command").getAsString().toUpperCase();
                final Command command = commands.get(commandName);
                sendReply(command.process(request));
            }
        } catch (IOException ignored) {
        }

    }

    public synchronized void sendReply(final Response response) {
        try {
            final GsonBuilder builder = new GsonBuilder();
            final Gson gson = builder.create();
            final String jsonRequest = gson.toJson(response);
            socketBufferedWriter.write(jsonRequest);
            socketBufferedWriter.newLine();
            socketBufferedWriter.flush();
        } catch (final IOException ioException) {
            throw new IllegalStateException(ioException);
        }
    }

}

interface Command {
    String getName();

    Response process(JsonObject request);

    static Command newCommand(String name, Function<JsonObject, Response> process) {

        return new Command() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public Response process(JsonObject request) {
                return process.apply(request);

            }
        };
    }
}