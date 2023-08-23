package server;

import com.google.gson.*;
import io.deeplay.Game;
import logic.Board;
import logic.Cell;
import logic.Move;
import logic.Player;
import serverrequest.*;
import serverresponses.*;
import ui.UI;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.deeplay.Game.makeMoveOnBoardWithOutLog;
import static server.Authorization.checkAuth;

public class Server {

    private static final int PORT = 6070;
    public static ConcurrentMap<UUID, ClientProcessor> clients = new ConcurrentHashMap<>();
    public static ConcurrentMap<UUID, String> onlineUsers = new ConcurrentHashMap<>();
    public static ConcurrentMap<UUID, String> registratedUsers = new ConcurrentHashMap<>();
    public static LinkedList<ClientProcessor> serverList = new LinkedList<>();

    static List<Room> roomList = new ArrayList<>();

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
                if (registratedUsers.values().contains(nickname)) {
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
                    } else if (!onlineUsers.containsKey(uuid)) {
                        onlineUsers.put(uuid, nickname);
                        return new ResponseAutorization("success", "you have successfully logged in");
                    }
                    else{
                        return new ResponseAutorization("fail", "You are already logged in");
                    }
                } else {
                    return new ResponseAutorization("fail", "you are not registered");
                }
            }));

            commands.add(Command.newCommand("CREATEROOM", (jsonRequest, uuid) -> {
                if (onlineUsers.containsKey(uuid)) {
                    if (!checkAuth(uuid, onlineUsers.get(uuid), onlineUsers)) {
                        return new ConnectToRoomResponse("fail", "you are not logged in");
                    }

                    Room room = new Room();
                    roomList.add(room);
                    room.setBlackPlayerUUID(uuid);
                    return new CreateRoomResponse("success", "Room was successfully registered", room.getId());
                }
                return new CreateRoomResponse("fail", "you are not logged in", null);
            }));

            commands.add(Command.newCommand("CONNECTTOROOM", (jsonRequest, uuid) -> {
                if (onlineUsers.containsKey(uuid)) {
                    if (!checkAuth(uuid, onlineUsers.get(uuid), onlineUsers)) {
                        return new ConnectToRoomResponse("fail", "you are not logged in");
                    }
                    int roomId = jsonRequest.get("roomId").getAsInt();
                    Room room = roomList.stream()
                            .filter(r -> r.getId() == roomId)
                            .findFirst()
                            .orElse(null);

                    if (room != null && room.hasNoPlayers()) {
                        room.setBlackPlayerUUID(uuid);
                        return new ConnectToRoomResponse("success", "Connected to room as BlackPlayer");
                    } else if (room != null && !room.isFull()) {
                        room.setWhitePlayerUUID(uuid);
                        return new ConnectToRoomResponse("success", "Connected to room as WhitePlayer");
                    } else if (room == null) {
                        return new ConnectToRoomResponse("fail", "Room with ID " + roomId + " not found");
                    } else {
                        return new ConnectToRoomResponse("fail", "Room with ID " + roomId + " is already occupied");
                    }
                }
                return new ConnectToRoomResponse("fail", "you are not logged in");
            }));


            commands.add(Command.newCommand("LEAVEROOM", (jsonRequest, uuid) -> {
                if (onlineUsers.containsKey(uuid)) {
                    if (!checkAuth(uuid, onlineUsers.get(uuid), onlineUsers)) {
                        return new LeaveRoomResponse("fail", "you are not logged in");
                    }
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
                    return new LeaveRoomResponse("success", "you successfully get out");
                }
                return new LeaveRoomResponse("fail", "you are not logged in");
            }));

            commands.add(Command.newCommand("GAMEOVER", (jsonRequest, uuid) -> {
                if (onlineUsers.containsKey(uuid)) {
                    if (!checkAuth(uuid, onlineUsers.get(uuid), onlineUsers)) {
                        return new GameoverResponse("fail", "you are not logged in");
                    }
                    for (Room room : roomList) {
                        if (room.getBlackPlayerUUID().equals(uuid) || room.getWhitePlayerUUID().equals(uuid)) {
                            roomList.remove(room);
                            break;
                        }
                    }
                    return new GameoverResponse("success", "game is over. You are not in room anymore");
                }
                return new GameoverResponse("fail", "you are not logged in");
            }));

            commands.add(Command.newCommand("STARTGAME", (jsonRequest, uuid) -> {
                int roomId = jsonRequest.get("roomId").getAsInt();
                Room room = new Room();
                for (Room r : roomList) {
                    if (r.id == roomId) {
                        room = r;
                        break;
                    }
                }
                room.game = new Game();
                return new StartGameResponse("success", "game started");
            }));

            commands.add(Command.newCommand("MAKEMOVE", (jsonRequest, uuid) -> {
                int row = jsonRequest.get("row").getAsInt();
                int col = jsonRequest.get("col").getAsInt();
                Room room = new Room();
                for (Room r : roomList) {
                    if (r.getBlackPlayerUUID() == uuid || r.getWhitePlayerUUID() == uuid) {
                        room = r;
                        break;
                    }
                }

                if (uuid == room.getBlackPlayerUUID()) {
                    if (room.game.nextTurnOfPlayerColor.equals(Cell.WHITE)) {
                        return new MakeMoveResponse("fail", "it's not your turn");
                    }
                    Board copyBoard = room.board.getBoardCopy();
                    serverPlayer player = new serverPlayer(Cell.BLACK, row, col, uuid);
                    try {
                        room.moveNumber = makeMoveOnBoardWithOutLog(room.board, player, room.moveNumber, copyBoard);
                        if (!room.board.getAllAvailableMoves(Cell.WHITE).isEmpty()) {
                            room.game.nextTurnOfPlayerColor = Cell.WHITE;
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (uuid == room.getWhitePlayerUUID()) {
                    if (room.game.nextTurnOfPlayerColor.equals(Cell.BLACK)) {
                        return new MakeMoveResponse("fail", "it's not your turn");
                    }
                    Board copyBoard = room.board.getBoardCopy();
                    serverPlayer player = new serverPlayer(Cell.WHITE, row, col, uuid);
                    try {
                        room.moveNumber = makeMoveOnBoardWithOutLog(room.board, player, room.moveNumber, copyBoard);
                        if (!room.board.getAllAvailableMoves(Cell.BLACK).isEmpty()) {
                            room.game.nextTurnOfPlayerColor = Cell.BLACK;
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                return new MakeMoveResponse("success", "you did your turn");
            }));


            try {
                while (!server.isClosed()) {
                    Socket socket = server.accept();
                    try {
                        ClientProcessor thisClient = new ClientProcessor(socket, commands);
                        serverList.add(thisClient);
                        clients.put(thisClient.uuid, thisClient);
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
        public final Map commands;
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
                while (true) {
                    String line = this.socketBufferedReader.readLine();
                    if (line == null) {
                        return;
                    }
                    System.out.println(line);
                    JsonObject request = JsonParser.parseString(line).getAsJsonObject();
                    String commandName = request.get("command").getAsString().toUpperCase();
                    Command command = (Command) this.commands.get(commandName);
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

        public synchronized void sendReply(WhereIcanGoResponse response) {
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

