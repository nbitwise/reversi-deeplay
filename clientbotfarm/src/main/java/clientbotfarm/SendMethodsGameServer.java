package clientbotfarm;

import clientbotfarmrequest.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Scanner;

public class SendMethodsGameServer {
    private static final Logger logger = LogManager.getLogger(SendMethodsGameServer.class);

    private SendMethodsGameServer() {
    }

    /**
     * Метод отправляет любое сообещение на сервер
     *
     * @param clientBotFarm - клиент
     */
    public static void sendMessageGameServer(@NotNull final ClientBotFarm clientBotFarm) throws IOException {
        final Scanner scanner = new Scanner(System.in);
        while (clientBotFarm.socket.isConnected()) {
            final String msg = scanner.nextLine();
            SendMethodsGameServer.createJsonAndSendCommandGameServer(clientBotFarm, msg);
        }
    }
    /**
     * Метод отправляет любой запрос на сервер
     *
     * @param clientBotFarm - клиент
     */
    static void sendRequestGameServer(@NotNull final Request request, @NotNull final ClientBotFarm clientBotFarm) {
        final String jsonRequest = clientBotFarm.gson.toJson(request);
        try {
            System.out.println(jsonRequest);
            clientBotFarm.bufferedWriter.write(jsonRequest);
            clientBotFarm.bufferedWriter.newLine();
            clientBotFarm.bufferedWriter.flush();
        } catch (IOException e) {
            logger.log(Level.ERROR, "Usage: register <nickname>");
        }
    }

    /**
     * Метод определяет, что за запрос ввел клиент, сериализует его, и отправляет на сервер
     *
     * @param clientBotFarm - клиент
     * @param input - запрос
     */
    public static void createJsonAndSendCommandGameServer(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String input) throws IOException {
        final String[] commandParts = input.split("\\s+");
        final String command = commandParts[0];

        switch (command) {
            case "REGISTRATION" -> commandRegistration(clientBotFarm, commandParts);

            case "AUTHORIZATION" -> commandAuthorization(clientBotFarm, commandParts);

            case "CREATEROOM" -> commandCreateRoom(clientBotFarm);

            case "CONNECTTOROOM" -> commandConnectToRoom(clientBotFarm, commandParts);

            case "LEAVEROOM" -> commandLeaveRoom(clientBotFarm);

            case "STARTGAME" -> commandStartGame(clientBotFarm);

            case "MAKEMOVE" -> commandMakeMove(clientBotFarm, commandParts);

            case "SURRENDER" -> commandSurrender(clientBotFarm);

            case "EXIT" -> commandExit(clientBotFarm);

            default -> commandDefault(command);
        }
    }

    private static void commandRegistration(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String[] commandParts) {
        if (commandParts.length > 1) {
            final String nickname = commandParts[1];
            final RegistrationRequest registrationRequest = new RegistrationRequest(nickname);
            sendRequestGameServer(registrationRequest, clientBotFarm);
        } else {
            logger.log(Level.INFO, "Usage: register <nickname>");
        }
    }

    private static void commandAuthorization(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String[] commandParts) {
        if (commandParts.length > 1) {
            final String nickname = commandParts[1];
            final AuthorizationRequest authorizationRequest = new AuthorizationRequest(nickname);
            sendRequestGameServer(authorizationRequest, clientBotFarm);
        } else {
            logger.log(Level.INFO, "Usage: login <nickname>");
        }
    }

    private static void commandCreateRoom(@NotNull final ClientBotFarm clientBotFarm) {
        if (clientBotFarm.roomId != 0) {
            logger.log(Level.INFO, "You can not create another room because you are already in room.");
        } else {
            final CreateRoomRequest createRoomRequest = new CreateRoomRequest();
            sendRequestGameServer(createRoomRequest, clientBotFarm);
        }
    }

    private static void commandConnectToRoom(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String[] commandParts) {
        if (clientBotFarm.roomId != 0) {
            logger.log(Level.INFO, "You can not connect to another room because you are already in room.");
        }
        if (commandParts.length > 1) {
            final int roomId = Integer.parseInt(commandParts[1]);
            final ConnectToRoomRequest connectToRoomRequest = new ConnectToRoomRequest(roomId);
            sendRequestGameServer(connectToRoomRequest, clientBotFarm);
            clientBotFarm.roomId = roomId;
        } else {
            logger.log(Level.INFO, "Usage: connect to room <room_id>.");
        }
    }

    private static void commandLeaveRoom(@NotNull final ClientBotFarm clientBotFarm) {
        if (clientBotFarm.roomId == 0) {
            logger.log(Level.INFO, "You can not leave room because you are not in room.");
        } else {
            final LeaveRoomRequest leaveRoomRequest = new LeaveRoomRequest();
            sendRequestGameServer(leaveRoomRequest, clientBotFarm);
            clientBotFarm.roomId = 0;
        }
    }

    private static void commandStartGame(@NotNull final ClientBotFarm clientBotFarm) {
        if (clientBotFarm.roomId == 0) {
            logger.log(Level.INFO, "You can not start game because you are not in room.");
        } else {
            final StartGameRequest startGameRequest = new StartGameRequest(clientBotFarm.roomId, false);
            sendRequestGameServer(startGameRequest, clientBotFarm);
        }
    }

    private static void commandMakeMove(@NotNull final ClientBotFarm clientBotFarm, @NotNull final String[] commandParts) {
        if (commandParts.length > 2) {
            final int row = Integer.parseInt(commandParts[1]);
            final int col = Integer.parseInt(commandParts[2]);
            final MakeMoveRequest makeMoveRequest = new MakeMoveRequest(row, col);
            sendRequestGameServer(makeMoveRequest, clientBotFarm);
        } else {
            logger.log(Level.INFO, "Wrong coordinates.");
        }
    }

    private static void commandSurrender(@NotNull final ClientBotFarm clientBotFarm) {
        final SurrenderRequest surrenderRequest = new SurrenderRequest();
        sendRequestGameServer(surrenderRequest, clientBotFarm);
    }

    private static void commandExit(@NotNull final ClientBotFarm clientBotFarm) throws IOException {
        clientBotFarm.close();
        System.exit(0);
    }

    private static void commandDefault(@NotNull final String commandName) {
        logger.log(Level.INFO, () -> "Unknown command: " + commandName);
    }
}
