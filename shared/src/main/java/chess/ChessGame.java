package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame implements Cloneable{
    private ChessBoard chessBoard;
    private TeamColor currentTurn;

    public ChessGame() {
        this.chessBoard = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece currentPiece = chessBoard.getPiece(startPosition);
        Collection<ChessMove> pieceMoves = new HashSet<>();
        Collection<ChessMove> MovVal = new HashSet<>();
        if (currPiece.pieceMoves(chessBoard, startPosition).isEmpty()) {
            return null;
        } else {
            pieceMoves = currentPiece.pieceMoves(chessBoard, startPosition);
        }

        for (ChessMove move : pieceMoves) {
            ChessPosition endP = move.getEndPosition();
            ChessPosition startP = move.getStartPosition();
            if (this.chessBoard.getPiece(endP) == null || (this.chessBoard.getPiece(endP) != null && this.chessBoard.getPiece(endP).getTeamColor() != currentTurn)) {
                ChessGame alterG = this.clone();
                alterG.chessBoard.addPiece(endP, currentPiece);
                alterG.chessBoard.addPiece(startP, null);
                if (!alterG.isInCheck(currentPiece.getTeamColor())) {
                    MovVal.add(move);
                }
            }
        }

        return MovVal;
    }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.chessBoard;
    }
    @Override
    public ChessGame clone() {
        try {
            ChessGame clone = (ChessGame) super.clone();
            clone.chessBoard = this.chessBoard.clone();
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new InternalError(ex);
        }
    }


}
