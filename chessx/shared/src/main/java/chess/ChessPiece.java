package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private char symbol;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        switch (Character.toLowerCase(symbol)) {
            case 'r':
                return PieceType.ROOK;
            case 'n':
                return PieceType.KNIGHT;
            case 'b':
                return PieceType.BISHOP;
            case 'q':
                return PieceType.QUEEN;
            case 'k':
                return PieceType.KING;
            case 'p':
                return PieceType.PAWN;
            default:
                return null;
        }
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList moves=new ArrayList();
        ChessPiece myPiece=board.getPiece(myPosition);
        int row=myPosition.getRow();
        int col=myPosition.getColumn();
        if (myPiece.getPieceType() == PieceType.KING) {
            int[][] kingMoves={{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};
            for (int[] move : kingMoves) {
                int newRow=row + move[0];
                int newCol=col + move[1];
                if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                    moves.add(new ChessPosition(newRow, newCol));
                }
            }
        } else if (myPiece.getPieceType() == PieceType.BISHOP) {
            int[][] directions={{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            for (int[] direction : directions) {
                for (int i=1; i <= 8; i++) {
                    int newRow=row + i * direction[0];
                    int newCol=col + i * direction[1];
                    moves.add(new ChessPosition(newRow, newCol));
                }



            }

        }
        return moves;
    }
}




