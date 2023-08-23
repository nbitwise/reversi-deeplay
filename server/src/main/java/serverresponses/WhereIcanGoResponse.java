package serverresponses;

import logic.Board;
import logic.Cell;
import logic.Move;


import java.util.List;

public class WhereIcanGoResponse implements Response {

        public String command = "WHEREICANGORESPONSE";
        public String availableMoves;

        public String board;

        public String cell;


        public WhereIcanGoResponse(String availableMoves, String board, String cell) {
                this.availableMoves = availableMoves;
                this.board = board;
                this.cell = cell;
        }

        public String getAvailableMoves() {
                return availableMoves;
        }

        public void setAvailableMoves(String availableMoves) {
                this.availableMoves = availableMoves;
        }



}
