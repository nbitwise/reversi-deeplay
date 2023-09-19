package botfarm;

import bot.BotPlayerMinMaxRuslan;
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
        int port;
        Properties appProps = new Properties();
        File file = new File("botfarm/file.properties");

        final Player botPlayer = new BotPlayerMinMaxRuslan(Cell.BLACK); //цвет настроить

        Map<Integer, Player> botList = new HashMap<>();

        botList.put(1, botPlayer);


        try (FileInputStream propertiesInput = new FileInputStream(file)) {
            appProps.load(propertiesInput);
            port = Integer.parseInt(appProps.getProperty("port"));
        }

        try (ServerSocket server = new ServerSocket(port)) {

            List<Command> commands = new ArrayList<>();

            commands.add(Command.newCommand("REGISTRATION_BOT_FARM", (jsonRequest, uuid) -> {
                String botName = jsonRequest.get("botName").getAsString().toUpperCase();
                if (botName.equals("BotPlayerMinMaxRuslan")) {
                    clients.put(uuid, 1);
                }


                return new ResponseBotFarmRegistration("success", "bot was connected");
            }));


            commands.add(Command.newCommand("WHERE_I_CAN_GO_REQUEST", (jsonRequest, uuid) -> {
                Gson gson = new Gson();
                RequestWhereICanGoBotFarm requestMakeMoveBot = gson.fromJson(jsonRequest, RequestWhereICanGoBotFarm.class);
                Player currentBot = botList.get(clients.get(uuid));
                Move move = currentBot.makeMove(BoardParser.parse(requestMakeMoveBot.board, 'B', 'W', '-'));
                String moveString = String.valueOf(move.row) + String.valueOf(move.col);
                return new ResponseMoveByBot("success", moveString);
            }));


            while (!server.isClosed()) {
                Socket socket = server.accept();
                try {
                    ClientProcessor thisClient = new ClientProcessor(socket, commands);
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