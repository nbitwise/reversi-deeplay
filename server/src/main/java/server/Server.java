package server;

import com.google.gson.*;
import logic.Board;
import logic.Cell;
import logic.Move;
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

    public static ConcurrentMap<UUID, ClientProcessor> clients = new ConcurrentHashMap<>();
    public static ConcurrentMap<UUID, String> onlineUsers = new ConcurrentHashMap<>();
    public static ConcurrentMap<UUID, String> registratedUsers = new ConcurrentHashMap<>();
    private static LinkedList<ClientProcessor> serverList = new LinkedList<>();

    public static List<Command> commands = new ArrayList<>();

    static List<Room> roomList = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        try (ServerSocket server = new ServerSocket(PORT)) {

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
                    } else if (!onlineUsers.containsKey(uuid)) {
                        onlineUsers.put(uuid, nickname);
                        return new ResponseAutorization("success", "you have successfully logged in");
                    }
                } else {
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

            commands.add(Command.newCommand("GAMEOVER", (jsonRequest, uuid) -> {
                for (Room room : roomList) {
                    if (room.getBlackPlayer().equals(uuid) || room.getWhitePlayer().equals(uuid)) {
                        roomList.remove(room);
                        break;
                    }
                }
                return new GameoverResponse("success", "game is over. You are not in room anymore");
            }));

            commands.add(Command.newCommand("STARTGAME", (jsonRequest, uuid) -> {
                //логирование здесь надо и чет еще такое всякое
                //     Gson gson = new Gson();
                //    StartGameRequest request = gson.fromJson(jsonRequest, StartGameRequest.class);

                //     int roomId = jsonRequest.get("roomId").getAsInt();
                //    Room thisRoom = roomList.get(roomId);

                //  Board thisBoard = thisRoom.getBoard();
                /*List<Move> moves;
                if (uuid.equals(thisRoom.getBlackPlayer())) {
                    moves = thisBoard.getAllAvailableMoves(Cell.BLACK);
                } else {
                    thisBoard.getAllAvailableMoves(Cell.WHITE);
                }
                moves = thisBoard.getAllAvailableMoves(Cell.WHITE);*/
               /* Room thisRoom = roomList.get(0); //IZMENIT!!!!!!!
                Board board = thisRoom.getBoard();
                List<Move> moves = board.getAllAvailableMoves(Cell.BLACK);
                WhereIcanGoResponse whereIcanGoResponse = new WhereIcanGoResponse(moves, board, Cell.BLACK);
                return whereIcanGoResponse;*/
                Room thisRoom = roomList.get(0); //IZMENIT!!!!!!!
                thisRoom.startGame1();
                return new ResponseStartGame("success","zaebis");

            }));


            commands.add(Command.newCommand("MAKEMOVE", (jsonRequest, uuid) -> {
                Gson gson = new Gson();
                MakeMoveRequest makeMoveRequest = gson.fromJson(jsonRequest, MakeMoveRequest.class);
             //   int roomId = jsonRequest.get("roomId").getAsInt();
                Room thisRoom = roomList.get(0); //IZMENIT!!!!!!!
                Move move = makeMoveRequest.getMove();
                List<Move> availableMoves;
                Board boardAfterMove;
                ClientProcessor opponent;
                if (thisRoom.getBlackPlayer().equals(uuid)) {
                    //не уверен, что доска меняется в комнате
                    boardAfterMove = thisRoom.getBoard();
                    boardAfterMove.placePiece(move.row, move.col, Cell.BLACK);
                    thisRoom.setBoard(boardAfterMove);
                    opponent = clients.get(thisRoom.getWhitePlayer());
                    availableMoves = boardAfterMove.getAllAvailableMoves(Cell.WHITE);
             //       opponent.sendReply(new WhereIcanGoResponse(availableMoves, boardAfterMove, Cell.WHITE));

                } else {
                    boardAfterMove = thisRoom.getBoard();
                    boardAfterMove.placePiece(move.row, move.col, Cell.WHITE);
                    thisRoom.setBoard(boardAfterMove);
                    opponent = clients.get(thisRoom.getBlackPlayer());
                    availableMoves = boardAfterMove.getAllAvailableMoves(Cell.BLACK);
             //       opponent.sendReply(new WhereIcanGoResponse(availableMoves, boardAfterMove, Cell.BLACK));
                }


                //делает всяко с доской и присылает доску обратно? но уже другому челику
                return new MadeMoveResponse("success", "hod sdelan, ozidaite hoda protivnika", thisRoom.getBoard());
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

                    JsonObject request = JsonParser.parseString(line).getAsJsonObject();
                    String commandName = request.get("command").getAsString().toUpperCase();
                    Command command = (Command) commands.get(commandName);
                    sendReply(command.process(request, uuid));
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

