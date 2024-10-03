package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
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
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        switch (pieceType) {
            case KING:
                return new HashSet<>(Calculator.king(board, myPosition, pieceColor));
            case QUEEN:
                return new HashSet<>(Calculator.queen(board, myPosition, pieceColor));
            case BISHOP:
                return new HashSet<>(Calculator.bishop(board, myPosition, pieceColor));
            case KNIGHT:
                return new HashSet<>(Calculator.knight(board, myPosition, pieceColor));
            case ROOK:
                return new HashSet<>(Calculator.rook(board, myPosition, pieceColor));
            case PAWN:
                return new HashSet<>(Calculator.pawn(board, myPosition, pieceColor));
            default:
                throw new IllegalArgumentException("Invalid Piece Type: " + pieceType);
        }
    };
}
