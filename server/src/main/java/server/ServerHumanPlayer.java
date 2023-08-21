package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import logic.Board;
import logic.Cell;
import logic.Move;
import logic.Player;
import serverrequest.MakeMoveRequest;
import serverresponses.WhereIcanGoResponse;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import static server.Server.commands;

public class ServerHumanPlayer extends Player {

    String name;

    UUID uuid;

    /**
     * Конструктор создает объект игрока с указанным типом фишки (клетки) и присваивает
     * ему уникальный идентификатор.
     *
     * @param playerCell тип фишки (клетки) игрока (BLACK или WHITE).
     */
    public ServerHumanPlayer(Cell playerCell, String name, UUID uuid) {
        super(playerCell);
        this.name = name;
        this.uuid = uuid;
    }

    @Override
    public Move makeMove(Board boardAfterMove) throws IOException {
        final List<Move> availableMoves = boardAfterMove.getAllAvailableMoves(playerCell);
        Server.ClientProcessor thisPlayer = Server.clients.get(uuid);
        WhereIcanGoResponse whereIcanGoResponse = new WhereIcanGoResponse(availableMoves, boardAfterMove, playerCell);
        thisPlayer.sendReply(whereIcanGoResponse);
        //отправялем клиенту досутпные ходы и доску
       /* System.out.println("Доступные ходы: ");
        for (Move m : availableMoves) {
            System.out.println(m.row + 1 + " " + (m.col + 1));
        }

        while (true) {
            Date dateStart = new Date();
            System.out.print("Введите строку и столбец (например, 2 3): ");
            Scanner scanner = null;

            String input = scanner.nextLine(); //вот тут вместо сканера должно быть считывание в клиенте
            //тут должен прихождить реквест от клиента с координатами хода
            String[] inputArray = input.trim().split("\\s+");
            if (inputArray.length == 2) {
                try {
                    int row = Integer.parseInt(inputArray[0]) - 1;
                    int col = Integer.parseInt(inputArray[1]) - 1;*/
        String line = "";
        while (line.isEmpty()) {
            line = thisPlayer.socketBufferedReader.readLine();
        }

        JsonObject request = JsonParser.parseString(line).getAsJsonObject();
        Gson gson = new Gson();
        MakeMoveRequest makeMoveRequest = gson.fromJson(request, MakeMoveRequest.class);

        //    String commandName = request.get("command").getAsString().toUpperCase();

        // Server.Command command = (Server.Command) commands.get(commandName);
        //   sendReply(command.process(request, uuid));

        //   Server.Command command = (Server.Command) commands.get("MAKEMOVE");
        //  sendReply(command.process(request, uuid));
        final Move move = makeMoveRequest.getMove();
        boardAfterMove.placePiece(move.row, move.col, playerCell); // Размещаем фишку на доске
        //      Date dateEnd = new Date();
        //    long finalTime = dateEnd.getTime() - dateStart.getTime();
        //        move.setTimeOnMove(finalTime);
        return move;

    }
}
