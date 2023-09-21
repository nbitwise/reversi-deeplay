package clientbotfarm;

import clientbotfarmresponse.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class GetResponsesMethodsHuman {

    private static final Logger logger = LogManager.getLogger(GetResponsesMethodsHuman.class);

    private GetResponsesMethodsHuman() {
    }

    static <T extends Response> T getResponse(@NotNull final Class<T> responseType, @NotNull final String jsonResponse, @NotNull final ClientBotFarm clientBotFarm) {
        return clientBotFarm.gson.fromJson(jsonResponse, responseType);
    }
    /**
     * Метод слушает все входящие сообщения, пока сокет не разорвал соединение с сервером
     *
     * @param clientBotFarm - клиент
     */
    static void getMessageHuman(@NotNull final ClientBotFarm clientBotFarm) {
        new Thread(() -> {
            while (clientBotFarm.socket.isConnected()) {
                try {
                    final String line = clientBotFarm.bufferedReader.readLine();
                    if (line != null) {
                        GetResponsesMethodsHuman.viewOnInComeMessageHuman(clientBotFarm, line);
                    }
                } catch (IOException e) {
                    logger.log(Level.ERROR, "Reader Error");
                }
            }
        }).start();
    }
    /**
     * Метод обрабатывает все входящие сообщения, в режиме клиента для человека
     *
     * @param clientBotFarm - клиент
     * @param input - строка, котор
     */
    static void viewOnInComeMessageHuman(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) throws IOException {
        final JsonObject request = JsonParser.parseString(input).getAsJsonObject();
        final String commandName = request.get("command").getAsString().toUpperCase();

        switch (commandName) {
            case "REGISTRATION" -> viewRegistration(clientBotFarm, input);

            case "AUTHORIZATION" -> viewAuthorization(clientBotFarm, input);

            case "CREATEROOM" -> viewCreateRoom(clientBotFarm, input);

            case "CONNECTTOROOM" -> viewConnectToRoom(clientBotFarm, input);

            case "LEAVEROOM" -> viewLeaveRoom(clientBotFarm, input);

            case "WHEREICANGORESPONSE" -> viewWhereICanGo(clientBotFarm, input);

            case "GAMEOVER" -> viewGameOver(clientBotFarm, input);

            case "MAKEMOVE" -> viewMakeMove(clientBotFarm, input);

            case "STARTGAME" -> viewStartGame(clientBotFarm, input);

            case "EXIT" -> viewExit(clientBotFarm);

            case "SURRENDER" -> viewSurrender(clientBotFarm, input);

            default -> viewDefault(commandName);
        }
    }
    private static void viewRegistration(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final RegistrationResponse registrationResponse = getResponse(RegistrationResponse.class, input, clientBotFarm);
        logger.log(Level.INFO, () -> "Registration response: " + registrationResponse.message);

    }
    private static void viewAuthorization(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final AuthorizationResponse authorizationResponse = getResponse(AuthorizationResponse.class, input, clientBotFarm);
        logger.log(Level.INFO, () -> "Authorization response: " + authorizationResponse.message);
    }
    private static void viewCreateRoom(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final CreateRoomResponse createRoomResponse = getResponse(CreateRoomResponse.class, input, clientBotFarm);
        if (createRoomResponse.status.equals("fail")) {
            logger.log(Level.INFO, () -> "Create room response: " + createRoomResponse.message);
        }
        clientBotFarm.roomId = createRoomResponse.roomId;
        logger.log(Level.INFO, () -> "Create room response: " + createRoomResponse.message + ", Room ID: " + createRoomResponse.roomId);

    }
    private static void viewConnectToRoom(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final ConnectToRoomResponse connectToRoomResponse = getResponse(ConnectToRoomResponse.class, input, clientBotFarm);
        logger.log(Level.INFO, () -> "Connect to room response: " + connectToRoomResponse.message);
    }
    private static void viewLeaveRoom(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final LeaveRoomResponse leaveRoomResponse = getResponse(LeaveRoomResponse.class, input, clientBotFarm);
        logger.log(Level.INFO, () -> "Leave room response: " + leaveRoomResponse.message);
    }
    private static void viewWhereICanGo(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final WhereIcanGoResponse whereIcanGoResponse = getResponse(WhereIcanGoResponse.class, input, clientBotFarm);
        System.out.println(whereIcanGoResponse.board);
        logger.log(Level.INFO, () -> "Your available moves " + whereIcanGoResponse.availableMoves);
    }
    private static void viewGameOver(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final GameOverResponse gameoverResponse = getResponse(GameOverResponse.class, input, clientBotFarm);
        logger.log(Level.INFO, () -> "Game over response " + gameoverResponse.message);
    }
    private static void viewMakeMove(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final MakeMoveResponse makeMoveResponse = getResponse(MakeMoveResponse.class, input, clientBotFarm);
        logger.log(Level.INFO, () -> "MakeMove response " + makeMoveResponse.message);
    }
    private static void viewStartGame(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final StartGameResponse startGameResponse = getResponse(StartGameResponse.class, input, clientBotFarm);
        logger.log(Level.INFO, () -> "StartGame response " + startGameResponse.message);
    }
    private static void viewSurrender(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final SurrenderResponse surrenderResponse = getResponse(SurrenderResponse.class, input, clientBotFarm);
        logger.log(Level.INFO, surrenderResponse.message);
    }
    private static void viewExit(@NotNull final ClientBotFarm clientBotFarm) throws IOException {
        clientBotFarm.close();
        System.exit(0);
    }
    private static void viewDefault(@NotNull final String commandName) {
        logger.log(Level.INFO, () -> "Unknown command: " + commandName);
    }
}
