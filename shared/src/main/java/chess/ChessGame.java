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
    private boolean gameOver;

    public ChessGame() {
        this.chessBoard = new ChessBoard();
        this.chessBoard.resetBoard();
        this.currentTurn = TeamColor.WHITE;
        this.gameOver = false;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
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
        if (currentPiece == null || currentPiece.pieceMoves(chessBoard, startPosition).isEmpty()) {
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

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startP = move.getStartPosition();
        ChessPosition endP = move.getEndPosition();
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
        ChessPiece currentPiece = this.chessBoard.getPiece(startP);

        if (isInCheckmate(currentTurn)) {
            throw new InvalidMoveException("ERROR: IN CHECKMATE");
        }
        if (currentPiece.getTeamColor() == getTeamTurn()) {
            Collection<ChessMove> MovVal = validMoves(startP);
            if (MovVal == null) {
                throw new InvalidMoveException("ERROR: NO VALID MOVES");
            } else if (!(MovVal.contains(move))) {
                throw new InvalidMoveException("ERROR: INVALID MOVE");
            }
            if (isInCheck(getTeamTurn())) {
                if (this.chessBoard.getPiece(endP) == null || (this.chessBoard.getPiece(endP) != null && this.chessBoard.getPiece(endP).getTeamColor() != currentTurn)) {
                    ChessGame alterG = this.clone();
                    alterG.chessBoard.addPiece(endP, currentPiece);
                    alterG.chessBoard.addPiece(startP, null);
                    if (alterG.isInCheck(getTeamTurn())) {
                        throw new InvalidMoveException("ERROR: STILL IN CHECK");
                    } else {
                        if (promotionPiece != null) {
                            ChessPiece promotedPiece = new ChessPiece(currentTurn, promotionPiece);
                            this.chessBoard.addPiece(endP, promotedPiece);
                            this.chessBoard.addPiece(startP, null);
                        } else {
                            this.chessBoard.addPiece(endP, currentPiece);
                            this.chessBoard.addPiece(startP, null);
                        }
                    }
                } else {
                    throw new InvalidMoveException("ERROR: IN CHECK");
                }
            } else {
                ChessGame alterG = this.clone();
                alterG.chessBoard.addPiece(endP, currentPiece);
                alterG.chessBoard.addPiece(startP, null);
                if (alterG.isInCheck(getTeamTurn())) {
                    throw new InvalidMoveException("ERROR: MOVE WILL PUT YOU IN CHECK");
                } else {
                    if (promotionPiece != null) {
                        ChessPiece promotedPiece = new ChessPiece(currentTurn, promotionPiece);
                        this.chessBoard.addPiece(endP, promotedPiece);
                        this.chessBoard.addPiece(startP, null);
                    } else {
                        this.chessBoard.addPiece(endP, currentPiece);
                        this.chessBoard.addPiece(startP, null);
                    }
                }
            }

        } else {
            throw new InvalidMoveException("ERROR: NOT YOUR TURN");
        }
        currentTurn = currentPiece.getTeamColor() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor attackingTeam = teamColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        boolean isInCheck = false;

        loop:
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition attaccPos = new ChessPosition(i, j);
                ChessPiece attaccPiece = this.chessBoard.getPiece(attaccPos);

                if (attaccPiece != null && attaccPiece.getTeamColor() == attackingTeam) {
                    Collection<ChessMove> attackingMoves = attaccPiece.pieceMoves(this.chessBoard, attaccPos);

                    for (ChessMove move : attackingMoves) {
                        if (this.chessBoard.getPiece(move.getEndPosition()) != null && this.chessBoard.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING && this.chessBoard.getPiece(move.getEndPosition()).getTeamColor() != attackingTeam) {
                            isInCheck = true;
                            break loop;
                        }
                    }
                }
            }
        }
        return isInCheck;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean Checkmate = false;
        if (!isInCheck(currentTurn)) return false;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition alliedPos = new ChessPosition(i, j);
                ChessPiece alliedPiece = this.chessBoard.getPiece(alliedPos);
                if (alliedPiece == null || alliedPiece.getTeamColor() != teamColor) continue;
                Collection<ChessMove> alliedMov = alliedPiece.pieceMoves(this.chessBoard, alliedPos);
                for (ChessMove move : alliedMov) {
                    ChessPosition endP = move.getEndPosition();
                    ChessPosition startP = move.getStartPosition();
                    ChessGame alterG = this.clone();
                    alterG.chessBoard.addPiece(endP, alliedPiece);
                    alterG.chessBoard.addPiece(startP, null);
                    if (!alterG.isInCheck(teamColor)) {
                        return false;
                    } else {
                        Checkmate = true;
                    }
                }
            }
        }
        return Checkmate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean Stalemate = true;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition alliedPos = new ChessPosition(i, j);
                ChessPiece alliedPiece = this.chessBoard.getPiece(alliedPos);
                if (alliedPiece != null && alliedPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> alliedMov = alliedPiece.pieceMoves(this.chessBoard, alliedPos);
                    for (ChessMove move : alliedMov) {
                        ChessPosition endP = move.getEndPosition();
                        ChessPosition startP = move.getStartPosition();
                        if (this.chessBoard.getPiece(endP) == null || (this.chessBoard.getPiece(endP) != null && this.chessBoard.getPiece(endP).getTeamColor() != currentTurn)) {
                            ChessGame alterG = this.clone();
                            alterG.chessBoard.addPiece(endP, alliedPiece);
                            alterG.chessBoard.addPiece(startP, null);
                            if (alterG.isInCheck(teamColor)) {
                                Stalemate = true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return Stalemate;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ChessGame chessGame=(ChessGame) object;
        return Objects.deepEquals(chessBoard, chessGame.chessBoard) && currentTurn == chessGame.currentTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chessBoard, currentTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" + "chessBoard=" + chessBoard + ", currentTurn=" + currentTurn + '}';
    }




}
