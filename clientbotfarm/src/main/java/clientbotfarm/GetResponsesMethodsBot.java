package clientbotfarm;

import clientbotfarmrequest.*;
import clientbotfarmresponse.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static clientbotfarm.GetResponsesMethodsHuman.getResponse;
import static clientbotfarm.SendMethodsBotFarm.sendRequestBotFarm;

public class GetResponsesMethodsBot {
    private static final Logger logger = LogManager.getLogger(GetResponsesMethodsBot.class);

    private GetResponsesMethodsBot() {
    }

    /**
     * Метод слушает все входящие сообщения от сервера, пока сокет не разорвал соединение с сервером
     *
     * @param clientBotFarm - клиент
     */
    public static void getMessageBot(@NotNull final ClientBotFarm clientBotFarm) {
        new Thread(() -> {
            while (clientBotFarm.socket.isConnected()) {
                try {
                    final String line = clientBotFarm.bufferedReader.readLine();
                    if (line != null) {
                        System.out.println(line);
                        viewOnInComeMessageBot(clientBotFarm, line);
                    }
                } catch (IOException e) {
                    logger.log(Level.ERROR, "Обрыв канала чтения");
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /**
     * Метод обрабатывает все входящие сообщения, в режиме клиента для бота
     *
     * @param clientBotFarm - клиент
     * @param input - строка, котор
     */
    public static void viewOnInComeMessageBot(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) throws IOException {

        final JsonObject request = JsonParser.parseString(input).getAsJsonObject();
        final String commandName = request.get("command").getAsString().toUpperCase();

        switch (commandName) {

            case "REGISTRATION" -> commandRegistrationBot(clientBotFarm, input);

            case "AUTHORIZATION" -> commandAuthorizationBot(clientBotFarm, input);

            case "VIEWROOMS" -> commandViewRoomsBot(clientBotFarm, input);

            case "CREATEROOM" -> commandCreateRoomBot(clientBotFarm, input);

            case "CONNECTTOROOM" -> commandConnectToRoomBot(clientBotFarm, input);

            case "WHEREICANGORESPONSE" -> commandWhereICanGoGameBot(clientBotFarm, input);

            case "LEAVEROOM" -> commandLeaveRoomBot(clientBotFarm, input);

            case "GAMEOVER" -> commandGameOverBot(clientBotFarm, input);

            case "MAKEMOVE" -> commandMakeMoveBot(clientBotFarm, input);

            case "STARTGAME" -> commandStartGameBot(clientBotFarm, input);

            case "EXIT" -> commandExitBot(clientBotFarm);

            default -> commandDefaultBot(commandName);
        }
    }

    private static void commandRegistrationBot(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final RegistrationResponse registrationResponse = getResponse(RegistrationResponse.class, input, clientBotFarm);
        logger.log(Level.INFO, () -> "Registration response: " + registrationResponse.message);
    }

    private static void commandAuthorizationBot(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final AuthorizationResponse authorizationResponse = getResponse(AuthorizationResponse.class, input, clientBotFarm);
        logger.log(Level.INFO, () -> "Authorization response: " + authorizationResponse.message);
    }

    private static void commandViewRoomsBot(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final ViewCreatedRoomsResponse viewCreatedRoomsResponse = getResponse(ViewCreatedRoomsResponse.class, input, clientBotFarm);
        if (viewCreatedRoomsResponse.status.equals("fail")) {
            final CreateRoomRequest createRoomRequest = new CreateRoomRequest();
            SendMethodsGameServer.sendRequestGameServer(createRoomRequest, clientBotFarm);
            logger.log(Level.INFO, () -> "Room was created");
        } else {
            final ConnectToRoomRequest connectToRoomRequest = new ConnectToRoomRequest(1);
            SendMethodsGameServer.sendRequestGameServer(connectToRoomRequest, clientBotFarm);
            logger.log(Level.INFO, () -> "Connected to room");
        }
    }

    private static void commandCreateRoomBot(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final CreateRoomResponse createRoomResponse = getResponse(CreateRoomResponse.class, input, clientBotFarm);
        if (createRoomResponse.status.equals("fail")) {
            logger.log(Level.INFO, () -> "Create room response: " + createRoomResponse.message);
        }
        clientBotFarm.roomId = createRoomResponse.roomId;
        logger.log(Level.INFO, () -> "Create room response: " + createRoomResponse.message + ", Room ID: " + createRoomResponse.roomId);
    }

    private static void commandConnectToRoomBot(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final ConnectToRoomResponse connectToRoomResponse = getResponse(ConnectToRoomResponse.class, input, clientBotFarm);
        if (connectToRoomResponse.message.equals("White player connected")) {
            final StartGameRequest startGameRequest = new StartGameRequest(clientBotFarm.roomId, false);
            SendMethodsGameServer.sendRequestGameServer(startGameRequest, clientBotFarm);
            logger.log(Level.INFO, () -> "Room was created");
        }
        logger.log(Level.INFO, () -> "Connect to room response: " + connectToRoomResponse.message);
    }

    private static void commandGameOverBot(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final GameOverResponse gameoverResponse = getResponse(GameOverResponse.class, input, clientBotFarm);
        System.out.println("Game over response " + gameoverResponse.message);
    }

    private static void commandLeaveRoomBot(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final LeaveRoomResponse leaveRoomResponse = getResponse(LeaveRoomResponse.class, input, clientBotFarm);
        logger.log(Level.INFO, () -> "Leave room response: " + leaveRoomResponse.message);
    }

    private static void commandStartGameBot(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final StartGameResponse startGameResponse = getResponse(StartGameResponse.class, input, clientBotFarm);
        logger.log(Level.INFO, () -> "StartGame response " + startGameResponse.message);
    }

    private static void commandWhereICanGoGameBot(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
       /* final WhereIcanGoResponse whereIcanGoResponse = getResponse(WhereIcanGoResponse.class, input, client);
        if (whereIcanGoResponse.color.equals("black")) {
            final Player botPlayer = new AIBotIlya(Cell.BLACK, 8);
            final Move move = botPlayer.makeMove(BoardParser.parse(whereIcanGoResponse.boardStringWON, 'B', 'W', '-'));
            final MakeMoveRequest makeMoveRequest = new MakeMoveRequest(move.row + 1, move.col + 1);
            SendMethodsGameServer.sendRequestGameServer(makeMoveRequest, client);
        } else {
            final Player botPlayer = new AIBotIlya(Cell.WHITE, 8);
            final Move move = botPlayer.makeMove(BoardParser.parse(whereIcanGoResponse.boardStringWON, 'B', 'W', '-'));
            final MakeMoveRequest makeMoveRequest = new MakeMoveRequest(move.row + 1, move.col + 1);
            SendMethodsGameServer.sendRequestGameServer(makeMoveRequest, client);
        }*/
        final WhereIcanGoResponse whereIcanGoResponse = getResponse(WhereIcanGoResponse.class, input, clientBotFarm);
        WhereICanGoBotFarmRequest whereICanGoBotFarmRequest = new WhereICanGoBotFarmRequest(whereIcanGoResponse.boardStringWON);
        sendRequestBotFarm(whereICanGoBotFarmRequest, clientBotFarm);
        logger.log(Level.INFO, () -> "Available moves: " + whereIcanGoResponse.availableMoves);
    }

    private static void commandMakeMoveBot(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final MakeMoveResponse makeMoveResponse = getResponse(MakeMoveResponse.class, input, clientBotFarm);
        if (makeMoveResponse.status.equals("fail")) {
            final WhereICanGoRequest whereICanGoRequest = new WhereICanGoRequest();
            SendMethodsGameServer.sendRequestGameServer(whereICanGoRequest, clientBotFarm);
        }
    }

    private static void commandExitBot(@NotNull final ClientBotFarm clientBotFarm) throws IOException {
        clientBotFarm.close();
        System.exit(0);
    }

    private static void commandDefaultBot(@NotNull final String commandName) {
        logger.log(Level.INFO, () -> "Unknown command: " + commandName);
    }
}
