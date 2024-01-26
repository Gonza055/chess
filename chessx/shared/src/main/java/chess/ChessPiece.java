package chess;

import java.util.Collection;
import java.util.ArrayList;

public class ChessPiece {
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.type = type;
    }


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
        return this.type;
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
            ChessPosition startPosition = new ChessPosition(row, col);
            int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            for (int[] direction : directions) {
                for (int i = 1; i <= 8; i++) {
                    int newRow = row + i * direction[0];
                    int newCol = col + i * direction[1];
                    if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                        ChessPosition endPosition = new ChessPosition(newRow, newCol);
                        moves.add(new ChessMove(startPosition, endPosition, myPiece.getPieceType()));
                    }
                }
            }

    }return moves;
}}




