import chess.*;
import ui.Client;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        try {
            Client.main(new String[]{"8080"});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
