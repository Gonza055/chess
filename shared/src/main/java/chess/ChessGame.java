package chess;
import java.util.HashSet;
import java.util.Collection;
import java.util.Objects;
public class ChessGame implements Cloneable {
    private ChessBoard chessBoard;
    private TeamColor currentTurn;
    private boolean gameOver;

    public ChessGame() {
        this.chessBoard=new ChessBoard();
        this.chessBoard.rstBoard();
        this.currentTurn=TeamColor.WHITE;
        this.gameOver=false;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver=gameOver;
    }

    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    public void setTeamTurn(TeamColor team) {
        currentTurn=team;
    }

    public enum TeamColor {
        WHITE,
        BLACK
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece currPiece=chessBoard.getPiece(startPosition);
        Collection<ChessMove> pieceMoves=new HashSet<>();
        Collection<ChessMove> valMoves=new HashSet<>();
        if (currPiece == null || currPiece.pieceMoves(chessBoard, startPosition).isEmpty()) {
            return null;
        } else {
            pieceMoves=currPiece.pieceMoves(chessBoard, startPosition);
        }
        for (ChessMove move : pieceMoves) {
            ChessPosition endPos=move.getEndPosition();
            ChessPosition startPos=move.getStartPosition();
            if (this.chessBoard.getPiece(endPos) == null || (this.chessBoard.getPiece(endPos) != null && this.chessBoard.getPiece(endPos).getTeamColor() != currPiece.getTeamColor())) {
                ChessGame altGame=this.clone();
                altGame.chessBoard.addPiece(endPos, currPiece);
                altGame.chessBoard.addPiece(startPos, null);
                if (!altGame.isInCheck(currPiece.getTeamColor())) {
                    valMoves.add(move);
                }
            }
        }
        return valMoves;
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPos=move.getStartPosition();
        ChessPosition endPos=move.getEndPosition();
        ChessPiece.PieceType promoPiece=move.getPromotionPiece();
        ChessPiece currPiece=this.chessBoard.getPiece(startPos);
        if (isInCheckmate(currentTurn)) {
            throw new InvalidMoveException("ERROR: IN CHECKMATE");
        }
        if (currPiece.getTeamColor() == getTeamTurn()) {
            Collection<ChessMove> valMoves=validMoves(startPos);
            if (valMoves == null) {
                throw new InvalidMoveException("ERROR: NO VALID MOVES EXIST");
            } else if (!(valMoves.contains(move))) {
                throw new InvalidMoveException("ERROR: INVALID MOVE");
            }
            if (isInCheck(getTeamTurn())) {
                if (this.chessBoard.getPiece(endPos) == null || (this.chessBoard.getPiece(endPos) != null && this.chessBoard.getPiece(endPos).getTeamColor() != currentTurn)) {
                    ChessGame altGame=this.clone();
                    altGame.chessBoard.addPiece(endPos, currPiece);
                    altGame.chessBoard.addPiece(startPos, null);
                    if (altGame.isInCheck(getTeamTurn())) {
                        throw new InvalidMoveException("ERROR: STILL IN CHECK");
                    } else {
                        isPromotingPawn(currPiece, promoPiece, startPos, endPos);
                    }
                } else {
                    throw new InvalidMoveException("ERROR: CURRENTLY IN CHECK");
                }
            } else {
                ChessGame altGame=this.clone();
                altGame.chessBoard.addPiece(endPos, currPiece);
                altGame.chessBoard.addPiece(startPos, null);
                if (altGame.isInCheck(getTeamTurn())) {
                    throw new InvalidMoveException("ERROR: MOVE WILL PUT YOU INTO CHECK");
                } else {
                    isPromotingPawn(currPiece, promoPiece, startPos, endPos);
                }
            }

        } else {
            throw new InvalidMoveException("ERROR: WRONG TURN");
        }
        currentTurn=currPiece.getTeamColor() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
    }

    public void isPromotingPawn(ChessPiece currPiece, ChessPiece.PieceType promoPiece, ChessPosition startPos, ChessPosition endPos) {
        if (promoPiece != null) {
            ChessPiece promotedPiece=new ChessPiece(currentTurn, promoPiece);
            this.chessBoard.addPiece(endPos, promotedPiece);
            this.chessBoard.addPiece(startPos, null);
        } else {
            this.chessBoard.addPiece(endPos, currPiece);
            this.chessBoard.addPiece(startPos, null);
        }
    }

    public boolean isInCheck(TeamColor teamColor) {
        TeamColor attackingTeam=teamColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        boolean inCheck=false;
        myLoop:
        for (int i=1; i < 9; i++) {
            for (int j=1; j < 9; j++) {
                ChessPosition attackingPosition=new ChessPosition(i, j);
                ChessPiece attackingPiece=this.chessBoard.getPiece(attackingPosition);
                if (attackingPiece != null && attackingPiece.getTeamColor() == attackingTeam) {
                    Collection<ChessMove> attackingMoves=attackingPiece.pieceMoves(this.chessBoard, attackingPosition);
                    for (ChessMove move : attackingMoves) {
                        if (this.chessBoard.getPiece(move.getEndPosition()) != null && this.chessBoard.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING && this.chessBoard.getPiece(move.getEndPosition()).getTeamColor() != attackingTeam) {
                            inCheck=true;
                            break myLoop;
                        }
                    }
                }
            }
        }
        return inCheck;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        boolean inCheckmate=false;
        if (!isInCheck(currentTurn)) return false;
        for (int i=1; i < 9; i++) {
            for (int j=1; j < 9; j++) {
                ChessPosition friendlyPosition=new ChessPosition(i, j);
                ChessPiece friendlyPiece=this.chessBoard.getPiece(friendlyPosition);
                if (friendlyPiece == null || friendlyPiece.getTeamColor() != teamColor) continue;
                Collection<ChessMove> friendlyMoves=friendlyPiece.pieceMoves(this.chessBoard, friendlyPosition);
                for (ChessMove move : friendlyMoves) {
                    ChessPosition endPos=move.getEndPosition();
                    ChessPosition startPos=move.getStartPosition();
                    ChessGame altGame=this.clone();
                    altGame.chessBoard.addPiece(endPos, friendlyPiece);
                    altGame.chessBoard.addPiece(startPos, null);
                    if (!altGame.isInCheck(teamColor)) {
                        return false;
                    } else {
                        inCheckmate=true;
                    }
                }
            }
        }
        return inCheckmate;
    }

    public boolean isInStalemate(TeamColor teamColor) {
        boolean inStalemate=true;
        for (int i=1; i < 9; i++) {
            for (int j=1; j < 9; j++) {
                ChessPosition friendlyPosition=new ChessPosition(i, j);
                ChessPiece friendlyPiece=this.chessBoard.getPiece(friendlyPosition);
                if (friendlyPiece != null && friendlyPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> friendlyMoves=friendlyPiece.pieceMoves(this.chessBoard, friendlyPosition);
                    for (ChessMove move : friendlyMoves) {
                        ChessPosition endPos=move.getEndPosition();
                        ChessPosition startPos=move.getStartPosition();
                        if (this.chessBoard.getPiece(endPos) == null || (this.chessBoard.getPiece(endPos) != null && this.chessBoard.getPiece(endPos).getTeamColor() != currentTurn)) {
                            ChessGame altGame=this.clone();
                            altGame.chessBoard.addPiece(endPos, friendlyPiece);
                            altGame.chessBoard.addPiece(startPos, null);
                            if (altGame.isInCheck(teamColor)) {
                                inStalemate=true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return inStalemate;
    }

    public void setBoard(ChessBoard board) {
        this.chessBoard=board;
    }

    public ChessBoard getBoard() {
        return this.chessBoard;
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "chessBoard=" + chessBoard +
                ", currentTurn=" + currentTurn +
                '}';
    }

    @Override
    public ChessGame clone() {
        try {
            ChessGame clone=(ChessGame) super.clone();
            clone.chessBoard=this.chessBoard.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame=(ChessGame) o;
        return Objects.deepEquals(chessBoard, chessGame.chessBoard) && currentTurn == chessGame.currentTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chessBoard, currentTurn);
    }
}