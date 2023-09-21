package clientbotfarm;

import clientbotfarmrequest.RegistrationRequest;
import clientbotfarmrequest.Request;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SendMethodsBotFarm {
    private static final Logger logger = LogManager.getLogger(SendMethodsBotFarm.class);

    private SendMethodsBotFarm() {
    }
    /**
     * Метод отправляет любой запрос на сервер бот фермы
     *
     * @param clientBotFarm - клиент
     */
    static void sendRequestBotFarm(@NotNull final Request request, @NotNull final ClientBotFarm clientBotFarm) {
        final String jsonRequest = clientBotFarm.gson.toJson(request);
        try {
            clientBotFarm.bufferedWriterBot.write(jsonRequest);
            clientBotFarm.bufferedWriterBot.newLine();
            clientBotFarm.bufferedWriterBot.flush();
        } catch (IOException e) {
            logger.log(Level.ERROR, "Usage: register <nickname>");
        }
    }

    /**
     * Метод определяет, что за запрос ввел клиент, сериализует его, и отправляет на сервер бот фермы
     *
     * @param clientBotFarm - клиент
     * @param input - запрос
     */
    public static void createJsonAndSendCommandBotFarm(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) throws IOException {
        final String[] commandParts = input.split("\\s+");
        final String command = commandParts[0];

        switch (command) {
            case "REGISTRATION" -> commandRegistration(clientBotFarm, commandParts);


            case "EXIT" -> commandExit(clientBotFarm);

            default -> commandDefault(command);
        }
    }

    private static void commandRegistration(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String[] commandParts) {
        if (commandParts.length > 1) {
            final String nickname = commandParts[1];
            final RegistrationRequest registrationRequest = new RegistrationRequest(nickname);
       //     sendRequest(registrationRequest, client);
        } else {
            logger.log(Level.INFO, "Usage: register <nickname>");
        }
    }

    private static void commandExit(@NotNull final ClientBotFarm clientBotFarm) throws IOException {
        clientBotFarm.close();
        System.exit(0);
    }

    private static void commandDefault(@NotNull final String commandName) {
        logger.log(Level.INFO, () -> "Unknown command: " + commandName);
    }
}
