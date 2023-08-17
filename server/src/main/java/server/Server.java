package server;

import com.google.gson.*;
import serverrequest.*;
import serverresponses.*;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Server {

    private static final int PORT = 6070;
    public static ConcurrentMap<UUID, String> onlineUsers = new ConcurrentHashMap<>();
    public static ConcurrentMap<UUID, String> registratedUsers = new ConcurrentHashMap<>();
    private static LinkedList<ClientProcessor> serverList = new LinkedList<>();
    private static List<Room> roomList = new ArrayList<>();

    private static boolean roomExists(int roomId) {
        for (Room room : roomList) {
            if (room.getId() == roomId) {
                return true;
            }
        }
        return false;
    }
    private static boolean roomExistsAndHasPlace(int roomId) {
        for (Room room : roomList) {
            if (room.getId() == roomId && room.checkHavePlace()) {
                return true;
            }
        }
        return false;
    }
    private static boolean roomExistsAndHasPlayer(int roomId, UUID uuid) {
        for (Room room : roomList) {
            if (room.getId() == roomId && room.hasPlayer(uuid)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {

        try (ServerSocket server = new ServerSocket(PORT)) {

            List<Command> commands = new ArrayList<>();

            commands.add(Command.newCommand("REGISTRATION", (jsonRequest, uuid) -> {

                Gson gson = new Gson();
                RegistrationRequest request = gson.fromJson(jsonRequest, RegistrationRequest.class);

                String nickname = request.nickname;
                if (nickname == null || !nickname.matches("^[a-zA-Z0-9_]+$")) {
                    return new ResponseRegistration("fail", "incorrect username");
                }
                if (registratedUsers.containsKey(uuid)) {
                    return new ResponseRegistration("fail", "user with this nickname already registrated");
                }
                registratedUsers.put(uuid, nickname);

                return new ResponseRegistration("success", "You was successfully registered");
            }));

            commands.add(Command.newCommand("AUTHORIZATION", (jsonRequest, uuid) -> {
                Gson gson = new Gson();
                AuthorizationRequest request = gson.fromJson(jsonRequest, AuthorizationRequest.class);

                String nickname = request.nickname;

                if (registratedUsers.containsKey(uuid)) {
                    if (onlineUsers.containsKey(uuid) && onlineUsers.get(uuid).equals(nickname)) {
                        return new ResponseAutorization("fail", "user with this nickname already online");
                    }
                    else if(!onlineUsers.containsKey(uuid)){
                        onlineUsers.put(uuid, nickname);
                        return new ResponseAutorization("success", "you have successfully logged in");
                    }
                }
                else{
                    return new ResponseAutorization("fail", "you are not registered");
                }
                return new ResponseAutorization("fail", "Server issue");
            }));

            commands.add(Command.newCommand("CREATEROOM", (jsonRequest, uuid) -> {
                Room room = new Room();
                roomList.add(room);
                room.setBlackPlayer(uuid);
                return new CreateRoomResponse("success", "Room was successfully registered", room.getId());
            }));

            commands.add(Command.newCommand("CONNECTTOROOM", (jsonRequest, uuid) -> {
                int roomId = jsonRequest.get("roomId").getAsInt();
                Room room = roomList.stream()
                        .filter(r -> r.getId() == roomId)
                        .findFirst()
                        .orElse(null);

                if (room != null && room.hasNoPlayers()) {
                    room.setBlackPlayer(uuid);
                    return new ConnectToRoomResponse("success", "Connected to room as BlackPlayer");
                } else if (room != null && !room.isFull()) {
                    room.setWhitePlayer(uuid);
                    return new ConnectToRoomResponse("success", "Connected to room as WhitePlayer");
                } else if (room == null) {
                    return new ConnectToRoomResponse("fail", "Room with ID " + roomId + " not found");
                } else {
                    return new ConnectToRoomResponse("fail", "Room with ID " + roomId + " is already occupied");
                }
            }));


            commands.add(Command.newCommand("LEAVEROOM", (jsonRequest, uuid) -> {
                JsonElement roomIdElement = jsonRequest.get("roomId");
                if (roomIdElement != null && roomIdElement.isJsonPrimitive() && roomIdElement.getAsJsonPrimitive().isNumber()) {
                    int roomId = roomIdElement.getAsInt();
                    for (Room room : roomList) {
                        if (room.getId() == roomId && room.hasPlayer(uuid)) {
                            room.removePlayer(uuid);
                            break;
                        }
                    }
                }
                return new LeaveRoomResponse("success");
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

    public static class ClientProcessor extends Thread {
        public  final Map commands;
        public final BufferedReader socketBufferedReader;
        public final BufferedWriter socketBufferedWriter;
        public final UUID uuid;

        public ClientProcessor(Socket socket, List<Command> commands) throws IOException {
            this.commands = commands.stream().collect(Collectors.toMap(Command::getName, Function.identity()));
            this.uuid = UUID.randomUUID();
            this.socketBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.socketBufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.start();
        }

        public void run() {
            try {
                while(true) {
                    String line = this.socketBufferedReader.readLine();
                    if (line == null) {
                        return;
                    }

                    JsonObject request = JsonParser.parseString(line).getAsJsonObject();
                    String commandName = request.get("command").getAsString().toUpperCase();
                    Command command = (Command)this.commands.get(commandName);
                    this.sendReply(command.process(request, this.uuid));
                }
            } catch (IOException var5) {
                var5.printStackTrace();
            }
        }

        public synchronized void sendReply(Response response) {
            try {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                String jsonRequest = gson.toJson(response);
                this.socketBufferedWriter.write(jsonRequest);
                this.socketBufferedWriter.newLine();
                this.socketBufferedWriter.flush();
            } catch (IOException var5) {
                throw new IllegalStateException(var5);
            }
        }
    }

    public interface Command {
        String getName();

        Response process(JsonObject var1, UUID var2);

        static Command newCommand(final String name, final BiFunction<JsonObject, UUID, Response> process) {
            return new Command() {
                public String getName() {
                    return name;
                }

                public Response process(JsonObject request, UUID uuid) {
                    return process.apply(request, uuid);
                }
            };
        }
    }
}

