package server;

import com.google.gson.*;
import gamelogging.GameLogger;
import io.deeplay.Game;
import logic.Board;
import logic.Cell;
import logic.Move;
import serverrequest.*;
import serverresponses.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.deeplay.Game.displayResultOnClient;


class Server {


    private static ConcurrentMap<UUID, ClientProcessor> clients = new ConcurrentHashMap<>();
    private static ConcurrentMap<UUID, String> onlineUsers = new ConcurrentHashMap<>();
    private static ConcurrentMap<UUID, String> registratedUsers = new ConcurrentHashMap<>();
    private static LinkedList<ClientProcessor> serverList = new LinkedList<>();


    static List<Room> roomList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        int port;
        Properties appProps = new Properties();
        File file = new File("server/file.properties");

        try (FileInputStream propertiesInput = new FileInputStream(file)) {
            appProps.load(propertiesInput);
            port = Integer.parseInt(appProps.getProperty("port"));
        }

        try (ServerSocket server = new ServerSocket(port)) {

            List<Command> commands = new ArrayList<>();

            commands.add(Command.newCommand("REGISTRATION", (jsonRequest, uuid) -> {

                Gson gson = new Gson();
                RegistrationRequest request = gson.fromJson(jsonRequest, RegistrationRequest.class);

                String nickname = request.nickname;
                if (nickname == null || !nickname.matches("^[a-zA-Z0-9_]+$")) {
                    return new ResponseRegistration("fail", "incorrect username");
                }
                if (registratedUsers.containsValue(nickname)) {
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
                        return new ResponseAutorization("success", "You have successfully logged in");
                    } else {
                        return new ResponseAutorization("fail", "You are already logged in");
                    }
                } else {
                    return new ResponseAutorization("fail", "You are not registered");
                }
            }));

            commands.add(Command.newCommand("VIEWROOMS", (jsonRequest, uuid) -> {
                int roomId = jsonRequest.get("roomId").getAsInt();
                Room room = roomList.stream()
                        .filter(r -> r.getRoomId() == roomId)
                        .findFirst()
                        .orElse(null);

                if (room != null) {
                    if (room.hasNoPlayers()) {
                        return new ViewCreatedRoomsResponse("success", "Room with ID " + roomId + " exists and has no players", roomId);
                    } else if (!room.isFull()) {
                        return new ViewCreatedRoomsResponse("success", "Room with ID " + roomId + " exists but is not full", roomId);
                    } else {
                        return new ViewCreatedRoomsResponse("fail", "Room with ID " + roomId + " is already occupied", roomId);
                    }
                } else {
                    return new ViewCreatedRoomsResponse("fail", "Room with ID " + roomId + " not found", roomId);
                }
            }));

            commands.add(Command.newCommand("CREATEROOM", (jsonRequest, uuid) -> {
                if (onlineUsers.containsKey(uuid)) {
                    Room room = new Room();

                    room.setBlackPlayerUUID(uuid);
                    room.blackPlayer = onlineUsers.get(uuid);
                    roomList.add(room);
                    return new CreateRoomResponse("success", "Room was successfully registered", room.getRoomId());
                }
                return new CreateRoomResponse("fail", "you are not logged in", null);
            }));

            commands.add(Command.newCommand("CONNECTTOROOM", (jsonRequest, uuid) -> {
                if (onlineUsers.containsKey(uuid)) {
                    int roomId = jsonRequest.get("roomId").getAsInt();
                    Room room = roomList.stream()
                            .filter(r -> r.getRoomId() == roomId)
                            .findFirst()
                            .orElse(null);

                    if (room != null && room.hasNoPlayers()) {
                        room.setBlackPlayerUUID(uuid);
                        room.blackPlayer = onlineUsers.get(uuid);

                        return new ConnectToRoomResponse("success", "Connected to room as BlackPlayer");
                    } else if (room != null && !room.isFull()) {
                        room.setWhitePlayerUUID(uuid);
                        room.whitePlayer = onlineUsers.get(uuid);
                        UUID opponent = room.getOpponentUUID(uuid);
                        ClientProcessor thisPlayer = Server.clients.get(opponent);

                        thisPlayer.sendReply(new ConnectToRoomResponse("success", "White player connected"));

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
                    JsonElement roomIdElement = jsonRequest.get("roomId");
                    if (roomIdElement != null && roomIdElement.isJsonPrimitive() && roomIdElement.getAsJsonPrimitive().isNumber()) {
                        int roomId = roomIdElement.getAsInt();
                        for (Room room : roomList) {
                            if (room.getRoomId() == roomId && room.hasPlayer(uuid)) {
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
                    for (Room room : roomList) {
                        if (room.getBlackPlayerUUID().equals(uuid) || room.getWhitePlayerUUID().equals(uuid)) {

                            break;
                        }
                    }
                    return new GameoverResponse("success", "game is over. You are not in room anymore", 50, true);
                }
                return new GameoverResponse("fail", "you are not logged in", 50, true);
            }));

            commands.add(Command.newCommand("STARTGAME", (jsonRequest, uuid) -> {
                int roomId = jsonRequest.get("roomId").getAsInt();
                Room room = new Room();
                for (Room r : roomList) {
                    if (r.roomId == roomId) {
                        room = r;
                        break;
                    }
                }

                UUID blackPlayerUUID = room.getBlackPlayerUUID();

                ClientProcessor blackPlayer = Server.clients.get(blackPlayerUUID);
                room.board = new Board();
                room.game = new Game();
                ClientProcessor whitePlayer = Server.clients.get(room.getWhitePlayerUUID());


                List<Move> availableMoves = room.board.getAllAvailableMoves(Cell.BLACK);

                String availableMovesString = availableMoves.toString();
                String boardString = Board.displayBoardOnClient(room.board);
                String boardStringWON = Board.displayBoardOnClientWithoutNumbers(room.board);
                final String nonStableId = new SimpleDateFormat("MMddHHmmss").format(Calendar.getInstance().getTime());
                GameLogger.logStart(Integer.parseInt(nonStableId), "fileForHuman", "systemFile");
                if (uuid == room.getBlackPlayerUUID()) {
                    blackPlayer.sendReply(new StartGameResponse("success", "game started"));
                    whitePlayer.sendReply(new StartGameResponse("success", "game started"));
                    return new WhereIcanGoResponse(availableMovesString, boardString, boardStringWON, "black");
                } else {
                    blackPlayer.sendReply(new StartGameResponse("success", "game started"));
                    blackPlayer.sendReply(new WhereIcanGoResponse(availableMovesString, boardString, boardStringWON, "black"));
                    return new StartGameResponse("success", "game started");
                }
            }));

            commands.add(Command.newCommand("SURRENDER", (jsonRequest, uuid) -> {
                ClientProcessor opponent = null;
                Room thisRoom = null;
                for (Room r : roomList) {
                    if (r.getBlackPlayerUUID() == uuid) {
                        opponent = clients.get(r.getWhitePlayerUUID());
                        thisRoom = r;
                    }
                    if (r.getWhitePlayerUUID() == uuid) {
                        opponent = clients.get(r.getBlackPlayerUUID());
                        thisRoom = r;
                        break;
                    }
                }
                assert opponent != null;
                final int blackCount = thisRoom.board.getQuantityOfBlack();
                final int whiteCount = thisRoom.board.getQuantityOfWhite();
                String stringReply = String.format("Number of Black pieces: %d %n Number of White pieces: %d ", blackCount, whiteCount);
                opponent.sendReply(new SurrenderResponse("Your opponent has surrendered \n" + stringReply));

                return new SurrenderResponse("You surrendered %n" + stringReply);
            }));

            commands.add(Command.newCommand("MAKEMOVE", (jsonRequest, uuid) -> {
                int row = jsonRequest.get("row").getAsInt() - 1;
                int col = jsonRequest.get("col").getAsInt() - 1;
                Room room = new Room();
                for (Room r : roomList) {
                    if (r.getBlackPlayerUUID() == uuid || r.getWhitePlayerUUID() == uuid) {
                        room = r;
                        break;
                    }
                }
                UUID opponent;

                if (uuid == room.getBlackPlayerUUID()) {
                    if (room.game.nextTurnOfPlayerColor.equals(Cell.WHITE)) {
                        return new MakeMoveResponse("fail", "It's not your turn");
                    }
                    opponent = room.getOpponentUUID(uuid);
                    final ClientProcessor thisPlayer = Server.clients.get(opponent);
                    final List<Move> availableMoves = room.board.getAllAvailableMoves(Cell.BLACK);

                    if (!availableMoves.contains(new Move(row, col))) {
                        return new MakeMoveResponse("fail", "Wrong move");
                    }

                    room.board.placePiece(row, col, Cell.BLACK);


                    GameLogger.logMove(room.board, row, col, uuid, "BLACK", "fileForHuman", "systemFile");

                    final List<Move> opponentAvailableMoves = room.board.getAllAvailableMoves(Cell.WHITE);
                    final String availableMovesString = opponentAvailableMoves.toString();
                    final String boardString = Board.displayBoardOnClient(room.board);
                    final String boardStringWON = Board.displayBoardOnClientWithoutNumbers(room.board);

                    if (room.board.getAllAvailableMoves(Cell.BLACK).isEmpty() && room.board.getAllAvailableMoves(Cell.WHITE).isEmpty()) {
                        final String gameOverMsg = displayResultOnClient(room.board);
                        Server.clients.get(room.getOpponentUUID(uuid)).sendReply(new GameoverResponse("success", gameOverMsg, 50, false));
                        GameLogger.logEnd(room.board, "fileForHuman", "systemFile");
                        return new GameoverResponse("success", gameOverMsg, 50, true);
                    }
                    if (!opponentAvailableMoves.isEmpty()) {
                        room.game.nextTurnOfPlayerColor = Cell.WHITE;
                        thisPlayer.sendReply(new WhereIcanGoResponse(availableMovesString, boardString, boardStringWON, "white"));
                    } else {
                        return new WhereIcanGoResponse(availableMovesString, boardString, boardStringWON, "black");
                    }

                } else if (uuid == room.getWhitePlayerUUID()) {
                    if (room.game.nextTurnOfPlayerColor.equals(Cell.BLACK)) {
                        return new MakeMoveResponse("fail", "It's not your turn");
                    }

                    opponent = room.getOpponentUUID(uuid);
                    final ClientProcessor thisPlayer = Server.clients.get(opponent);
                    final List<Move> availableMoves = room.board.getAllAvailableMoves(Cell.WHITE);

                    if (!availableMoves.contains(new Move(row, col))) {
                        return new MakeMoveResponse("fail", "Wrong move");
                    }
                    room.board.placePiece(row, col, Cell.WHITE);
                    GameLogger.logMove(room.board, row, col, uuid, "WHITE", "fileForHuman", "systemFile");

                    final List<Move> opponentAvailableMoves = room.board.getAllAvailableMoves(Cell.BLACK);
                    final String availableMovesString = opponentAvailableMoves.toString();
                    final String boardString = Board.displayBoardOnClient(room.board);
                    final String boardStringWON = Board.displayBoardOnClientWithoutNumbers(room.board);

                    if (room.board.getAllAvailableMoves(Cell.BLACK).isEmpty() && room.board.getAllAvailableMoves(Cell.WHITE).isEmpty()) {
                        final String gameOverMsg = displayResultOnClient(room.board);
                        Server.clients.get(room.getOpponentUUID(uuid)).sendReply(new GameoverResponse("success", gameOverMsg, 50, false));
                        GameLogger.logEnd(room.board, "fileForHuman", "systemFile");
                        return new GameoverResponse("success", gameOverMsg, 50, true);
                    }

                    if (!opponentAvailableMoves.isEmpty()) {
                        room.game.nextTurnOfPlayerColor = Cell.BLACK;
                        thisPlayer.sendReply(new WhereIcanGoResponse(availableMovesString, boardString, boardStringWON, "black"));
                    } else {
                        return new WhereIcanGoResponse(availableMovesString, boardString, boardStringWON, "white");
                    }
                }
                return new MakeMoveResponse("success", "You did your turn.");
            }));

            commands.add(Command.newCommand("WHEREICANGORESPONSE", (jsonRequest, uuid) -> {

                Room room = new Room();
                for (Room r : roomList) {
                    if (r.getBlackPlayerUUID() == uuid || r.getWhitePlayerUUID() == uuid) {
                        room = r;
                        break;
                    }
                }
                UUID opponent;
                if (uuid == room.getBlackPlayerUUID()) {
                    if (room.game.nextTurnOfPlayerColor.equals(Cell.WHITE)) {
                        return new MakeMoveResponse("fail", "It's not your turn");
                    }

                    List<Move> availableMoves = room.board.getAllAvailableMoves(Cell.BLACK);
                    String availableMovesString = availableMoves.toString();
                    String boardString = Board.displayBoardOnClient(room.board);
                    String boardStringWON = Board.displayBoardOnClientWithoutNumbers(room.board);

                    return new WhereIcanGoResponse(availableMovesString, boardString, boardStringWON, "black");
                } else {
                    if (room.game.nextTurnOfPlayerColor.equals(Cell.BLACK)) {
                        return new MakeMoveResponse("fail", "It's not your turn");
                    }
                    List<Move> availableMoves = room.board.getAllAvailableMoves(Cell.WHITE);
                    String availableMovesString = availableMoves.toString();
                    String boardString = Board.displayBoardOnClient(room.board);
                    String boardStringWON = Board.displayBoardOnClientWithoutNumbers(room.board);

                    return new WhereIcanGoResponse(availableMovesString, boardString, boardStringWON, "white");
                }

            }));

            commands.add(Command.newCommand("GUI", (jsonRequest, uuid) -> {
                return new GUIResponse("success", "Run gui....");
            }));


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


        }
    }


}

class ClientProcessor extends Thread {
    public Map commands;
    public BufferedReader socketBufferedReader;
    public BufferedWriter socketBufferedWriter;
    public UUID uuid;

    ClientProcessor(Socket socket, List<Command> commands) throws IOException {
        this.commands = commands.stream().collect(Collectors.toMap(Command::getName, Function.identity()));
        this.uuid = UUID.randomUUID();
        this.socketBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.socketBufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                String line = socketBufferedReader.readLine();
                if (line == null) {
                    return;
                }
                System.out.println(line);
                JsonObject request = JsonParser.parseString(line).getAsJsonObject();
                String commandName = request.get("command").getAsString().toUpperCase();
                Command command = (Command) commands.get(commandName);
                sendReply(command.process(request, uuid));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Отправляет на клиент реплай в виде json строки.
     *
     * @param response - строка.
     */
    synchronized void sendReply(Response response) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            String jsonRequest = gson.toJson(response);
            socketBufferedWriter.write(jsonRequest);
            socketBufferedWriter.newLine();
            socketBufferedWriter.flush();
        } catch (IOException var5) {
            throw new IllegalStateException(var5);
        }
    }

}

interface Command {
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



