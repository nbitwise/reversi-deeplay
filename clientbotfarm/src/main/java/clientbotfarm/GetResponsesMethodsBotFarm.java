package clientbotfarm;

import clientbotfarmrequest.MakeMoveRequest;
import clientbotfarmrequest.WhereICanGoBotFarmRequest;
import clientbotfarmresponse.BotFarmRegistrationResponse;
import clientbotfarmresponse.WhereIcanGoResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import logic.Move;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static clientbotfarm.GetResponsesMethodsHuman.getResponse;
import static clientbotfarm.SendMethodsBotFarm.sendRequestBotFarm;
import static clientbotfarm.SendMethodsGameServer.sendRequestGameServer;

public class GetResponsesMethodsBotFarm {
    private static final Logger logger = LogManager.getLogger(GetResponsesMethodsBotFarm.class);

    private GetResponsesMethodsBotFarm() {
    }

    /**
     * Метод слушает все входящие сообщения от сервера, пока сокет не разорвал соединение с сервером
     *
     * @param clientBotFarm - клиент
     */
    public static void getMessageBotFarm(@NotNull final ClientBotFarm clientBotFarm) {
        new Thread(() -> {
            while (clientBotFarm.socketBot.isConnected()) {
                try {
                    final String line = clientBotFarm.bufferedReaderBot.readLine();
                    if (line != null) {
                        System.out.println(line);
                        viewOnInComeMessageBotFarm(clientBotFarm, line);
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
     * @param input  - строка, котор
     */
    public static void viewOnInComeMessageBotFarm(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) throws IOException {

        final JsonObject request = JsonParser.parseString(input).getAsJsonObject();
        final String commandName = request.get("command").getAsString().toUpperCase();

        switch (commandName) {

            case "REGISTRATION_BOT_FARM" -> viewRegistrationBotFarm(clientBotFarm, input);

            case "SEND_MOVE_TO_SERVER_BOT" -> viewSendMoveToGameServer(clientBotFarm, input);

            case "WHEREICANGORESPONSE" -> viewWhereICanGoGameBot(clientBotFarm, input);

            default -> commandDefaultBot(commandName);
        }
    }

    private static void viewRegistrationBotFarm(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final BotFarmRegistrationResponse botFarmRegistrationResponse = getResponse(BotFarmRegistrationResponse.class, input, clientBotFarm);
        logger.log(Level.INFO, () -> "Registration response: " + botFarmRegistrationResponse.message);
    }

    private static void viewWhereICanGoGameBot(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final WhereIcanGoResponse whereIcanGoResponse = getResponse(WhereIcanGoResponse.class, input, clientBotFarm);
        WhereICanGoBotFarmRequest whereICanGoBotFarmRequest = new WhereICanGoBotFarmRequest(whereIcanGoResponse.boardStringWON);
        sendRequestBotFarm(whereICanGoBotFarmRequest, clientBotFarm);
        logger.log(Level.INFO, () -> "Available moves: " + whereIcanGoResponse.availableMoves);
    }

    private static void viewSendMoveToGameServer(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) {
        final WhereIcanGoResponse whereIcanGoResponse = getResponse(WhereIcanGoResponse.class, input, clientBotFarm);
        JsonObject request = JsonParser.parseString(input).getAsJsonObject();
        int coordinates = request.get("move").getAsInt();

        Move move = new Move(coordinates / 10, coordinates % 10);
        MakeMoveRequest makeMoveRequest = new MakeMoveRequest(move.row + 1, move.col + 1);
        sendRequestGameServer(makeMoveRequest, clientBotFarm);
        logger.log(Level.INFO, () -> "Available moves: " + whereIcanGoResponse.availableMoves);
    }

    private static void commandDefaultBot(@NotNull final String commandName) {
        logger.log(Level.INFO, () -> "Unknown command: " + commandName);
    }
}
