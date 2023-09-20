package botfarm;

import bot.BotPlayerMinMaxRuslan;
import bot.RandomBot;
import botrequests.RequestRegistrationBotFarm;
import botrequests.RequestWhereICanGoBotFarm;
import botresponses.*;
import com.google.gson.*;
import logic.Cell;
import logic.Move;
import logic.Player;
import parsing.BoardParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

class BotFarm {

    private static ConcurrentMap<UUID, Integer> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        final int port;
        final Properties appProps = new Properties();
        final File file = new File("botfarm/file.properties");

        final Player ruslanBotMinMaxBlack = new BotPlayerMinMaxRuslan(Cell.BLACK);
        final Player randomBotBlack = new RandomBot(Cell.BLACK);
        final Player ruslanBotMinMaxWhite = new BotPlayerMinMaxRuslan(Cell.WHITE);
        final Player randomBotWhite = new RandomBot(Cell.WHITE);

        Map<Integer, Player> botList = new HashMap<>();

        botList.put(1, ruslanBotMinMaxBlack);
        botList.put(2, randomBotBlack);
        botList.put(3, ruslanBotMinMaxWhite);
        botList.put(4, randomBotWhite);

        try (FileInputStream propertiesInput = new FileInputStream(file)) {
            appProps.load(propertiesInput);
            port = Integer.parseInt(appProps.getProperty("port"));
        }

        try (ServerSocket server = new ServerSocket(port)) {

            List<Command> commands = new ArrayList<>();

            commands.add(Command.newCommand("REGISTRATION_BOT_FARM", (jsonRequest, uuid) -> {
                final Gson gson = new Gson();
                final RequestRegistrationBotFarm registrationBotFarm = gson.fromJson(jsonRequest, RequestRegistrationBotFarm.class);
                final String botName = registrationBotFarm.botName;
                if (botName.equals("ruslanBotMinMaxBlack")) {
                    clients.put(uuid, 1);
                }
                if (botName.equals("randomBotBlack")) {
                    clients.put(uuid, 2);
                }
                if (botName.equals("ruslanBotMinMaxWhite")) {
                    clients.put(uuid, 3);
                }
                if (botName.equals("randomBotWhite")) {
                    clients.put(uuid, 4);
                }


                return new ResponseBotFarmRegistration("success", "bot was connected");
            }));


            commands.add(Command.newCommand("WHERE_I_CAN_GO_REQUEST", (jsonRequest, uuid) -> {
                final Gson gson = new Gson();
                final RequestWhereICanGoBotFarm requestMakeMoveBot = gson.fromJson(jsonRequest, RequestWhereICanGoBotFarm.class);
                final Player currentBot = botList.get(clients.get(uuid));
                final Move move = currentBot.makeMove(BoardParser.parse(requestMakeMoveBot.board, 'B', 'W', '-'));
                final String moveString = String.valueOf(move.row) + String.valueOf(move.col);
                return new ResponseMoveByBot("success", moveString);
            }));


            while (!server.isClosed()) {
                Socket socket = server.accept();
                try {
                    final ClientProcessor thisClient = new ClientProcessor(socket, commands);
                } catch (IOException e) {

                    socket.close();
                }
            }


        }
    }

}

class ClientProcessor extends Thread {
    public Map commands;
    public final BufferedReader socketBufferedReader;
    public final BufferedWriter socketBufferedWriter;
    public final UUID uuid;

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
                final JsonObject request = JsonParser.parseString(line).getAsJsonObject();
                final String commandName = request.get("command").getAsString().toUpperCase();
                final Command command = (Command) commands.get(commandName);
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
            final GsonBuilder builder = new GsonBuilder();
            final Gson gson = builder.create();
            final String jsonRequest = gson.toJson(response);
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