package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private PieceType pieceType;
    private ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceType = type;
        this.pieceColor = pieceColor;
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
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> possibleMoves = new HashSet<>();

        switch (this.pieceType) {
            case KING:
                possibleMoves = kMoves(board, myPosition);
                break;
            case QUEEN:
                possibleMoves = qMoves(board, myPosition);
                break;
            case BISHOP:
                possibleMoves = bMoves(board, myPosition);
                break;
            case KNIGHT:
                possibleMoves = knightMoves(board, myPosition);
                break;
            case ROOK:
                possibleMoves = rMoves(board, myPosition);
                break;
            case PAWN:
                possibleMoves = pMoves(board, myPosition);
                break;
            default:
                return null;
        }
        return possibleMoves;
    }
    private Collection<ChessMove> kMoves(ChessBoard currentBoard, ChessPosition currentPosition) {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();

        Collection<ChessMove> possibleMoves = new HashSet<>();
        Collection<ChessPosition> possiblePositions = new HashSet<>();

        possiblePositions.add(new ChessPosition(currentRow + 1, currentCol));
        possiblePositions.add(new ChessPosition(currentRow, currentCol +1));
        possiblePositions.add(new ChessPosition(currentRow -1, currentCol));
        possiblePositions.add(new ChessPosition(currentRow, currentCol -1));
        possiblePositions.add(new ChessPosition(currentRow + 1, currentCol -1));
        possiblePositions.add(new ChessPosition(currentRow + 1, currentCol +1));
        possiblePositions.add(new ChessPosition(currentRow - 1, currentCol -1));
        possiblePositions.add(new ChessPosition(currentRow - 1, currentCol +1));

        for (ChessPosition pos: possiblePositions){

            if (pos.getRow() < 1 || pos.getRow() > 8 || pos.getColumn() > 8 || pos.getColumn() < 1) {
                continue;
            }
            if (currentBoard.getPiece(pos) != null && currentBoard.getPiece(pos).pieceColor == this.pieceColor){
                continue;
            }
            else {
                possibleMoves.add(new ChessMove(currentPosition, pos, null));
            }
        }
        return possibleMoves;
    }

    private Collection<ChessMove> qMoves(ChessBoard currentBoard, ChessPosition currentPosition) {
        int currentRow=currentPosition.getRow();
        int currentCol=currentPosition.getColumn();

        Collection<ChessMove> possibleMoves=new HashSet<>();
        Collection<ChessPosition> possiblePositions=new HashSet<>();

        for (int i = currentRow + 1; i <= 8; i++) {
            ChessPosition possiblePosition = new ChessPosition(i, currentCol);
            if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor != this.pieceColor) {
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
                break;
            } else if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor == this.pieceColor) {
                break;
            } else if (currentBoard.getPiece(possiblePosition) == null){
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
            }
        }

        for (int i = currentRow - 1; i > 0; i--) {
            ChessPosition possiblePosition = new ChessPosition(i, currentCol);
            if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor != this.pieceColor) {
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
                break;
            } else if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor == this.pieceColor) {
                break;
            } else if (currentBoard.getPiece(possiblePosition) == null){
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
            }
        }


        for (int j = currentCol - 1; j > 0; j--) {
            ChessPosition possiblePosition = new ChessPosition(currentRow, j);
            if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor != this.pieceColor){
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
                break;
            } else if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor == this.pieceColor){
                break;
            } else if (currentBoard.getPiece(possiblePosition) == null) {
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
            }
        }

        for (int j = currentCol + 1; j <= 8; j++) {
            ChessPosition possiblePosition = new ChessPosition(currentRow, j);
            if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor != this.pieceColor){
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
                break;
            } else if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor == this.pieceColor){
                break;
            } else if (currentBoard.getPiece(possiblePosition) == null) {
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
            }
        }

        for (int i = currentRow + 1, j = currentCol - 1; i <= 8 && j > 0 ; i++, j--) {
            ChessPosition possiblePosition = new ChessPosition(i,j);
            if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor != pieceColor) {
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
                break;
            } else if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor == this.pieceColor) {
                break;
            } else if (currentBoard.getPiece(possiblePosition) == null){
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
            }
        }
        for (int i = currentRow + 1, j = currentCol + 1; i <= 8 && j <= 8 ; i++, j++) {
            ChessPosition possiblePosition = new ChessPosition(i,j);
            if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor != pieceColor) {
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
                break;
            } else if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor == this.pieceColor) {
                break;
            } else if (currentBoard.getPiece(possiblePosition) == null){
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
            }
        }
        for (int i = currentRow - 1, j = currentCol - 1; i > 0 && j > 0 ; i--, j--) {
            ChessPosition possiblePosition = new ChessPosition(i,j);
            if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor != pieceColor) {
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
                break;
            } else if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor == this.pieceColor) {
                break;
            } else if (currentBoard.getPiece(possiblePosition) == null){
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
            }
        }
        for (int i = currentRow - 1, j = currentCol + 1; i > 0 && j <= 8 ; i--, j++) {
            ChessPosition possiblePosition = new ChessPosition(i,j);
            if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor != pieceColor) {
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
                break;
            } else if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor == this.pieceColor) {
                break;
            } else if (currentBoard.getPiece(possiblePosition) == null){
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
            }
        }
        return possibleMoves;

    }

    private Collection<ChessMove> bMoves(ChessBoard currentBoard, ChessPosition currentPosition) {
        int currentRow=currentPosition.getRow();
        int currentCol=currentPosition.getColumn();

        Collection<ChessMove> possibleMoves=new HashSet<>();
        Collection<ChessPosition> possiblePositions=new HashSet<>();

        for (int i = currentRow + 1, j = currentCol - 1; i <= 8 && j > 0 ; i++, j--) {
            ChessPosition possiblePosition = new ChessPosition(i,j);
            if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor != pieceColor) {
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
                break;
            } else if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor == this.pieceColor) {
                break;
            } else if (currentBoard.getPiece(possiblePosition) == null){
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
            }
        }
        for (int i = currentRow + 1, j = currentCol + 1; i <= 8 && j <= 8 ; i++, j++) {
            ChessPosition possiblePosition = new ChessPosition(i,j);
            if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor != pieceColor) {
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
                break;
            } else if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor == this.pieceColor) {
                break;
            } else if (currentBoard.getPiece(possiblePosition) == null){
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
            }
        }
        for (int i = currentRow - 1, j = currentCol - 1; i > 0 && j > 0 ; i--, j--) {
            ChessPosition possiblePosition = new ChessPosition(i,j);
            if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor != pieceColor) {
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
                break;
            } else if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor == this.pieceColor) {
                break;
            } else if (currentBoard.getPiece(possiblePosition) == null){
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
            }
        }
        for (int i = currentRow - 1, j = currentCol + 1; i > 0 && j <= 8 ; i--, j++) {
            ChessPosition possiblePosition = new ChessPosition(i,j);
            if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor != pieceColor) {
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
                break;
            } else if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor == this.pieceColor) {
                break;
            } else if (currentBoard.getPiece(possiblePosition) == null){
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
            }
        }
        return possibleMoves;
    }

    private Collection<ChessMove> knightMoves(ChessBoard currentBoard, ChessPosition currentPosition) {
        int currentRow=currentPosition.getRow();
        int currentCol=currentPosition.getColumn();

        Collection<ChessMove> possibleMoves=new HashSet<>();
        Collection<ChessPosition> possiblePositions=new HashSet<>();

        possiblePositions.add(new ChessPosition(currentRow + 1, currentCol -2));
        possiblePositions.add(new ChessPosition(currentRow +2, currentCol -1));
        possiblePositions.add(new ChessPosition(currentRow +1, currentCol +2));
        possiblePositions.add(new ChessPosition(currentRow +2, currentCol +1));
        possiblePositions.add(new ChessPosition(currentRow -1, currentCol -2));
        possiblePositions.add(new ChessPosition(currentRow -2, currentCol -1));
        possiblePositions.add(new ChessPosition(currentRow -1, currentCol +2));
        possiblePositions.add(new ChessPosition(currentRow -2, currentCol +1));

        for (ChessPosition pos: possiblePositions) {
            if (pos.getRow() < 1 || pos.getRow() > 8 || pos.getColumn() < 1 || pos.getColumn() > 8) {
                continue;
            }
            if (currentBoard.getPiece(pos) != null && currentBoard.getPiece(pos).pieceColor == this.pieceColor){
                continue;
            }
            else{
                possibleMoves.add(new ChessMove(currentPosition, pos, null));
            }
        }
        return possibleMoves;
    }

    private Collection<ChessMove> rMoves(ChessBoard currentBoard, ChessPosition currentPosition) {
        int currentRow=currentPosition.getRow();
        int currentCol=currentPosition.getColumn();

        Collection<ChessMove> possibleMoves=new HashSet<>();
        Collection<ChessPosition> possiblePositions=new HashSet<>();

        for (int i = currentRow + 1; i <= 8; i++) {
            ChessPosition possiblePosition = new ChessPosition(i, currentCol);
            if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor != this.pieceColor) {
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
                break;
            } else if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor == this.pieceColor) {
                break;
            } else if (currentBoard.getPiece(possiblePosition) == null){
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
            }
        }
        for (int i = currentRow - 1; i > 0; i--) {
            ChessPosition possiblePosition = new ChessPosition(i, currentCol);
            if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor != this.pieceColor) {
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
                break;
            } else if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor == this.pieceColor) {
                break;
            } else if (currentBoard.getPiece(possiblePosition) == null){
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
            }
        }
        for (int j = currentCol - 1; j > 0; j--) {
            ChessPosition possiblePosition = new ChessPosition(currentRow, j);
            if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor != this.pieceColor){
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
                break;
            } else if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor == this.pieceColor){
                break;
            } else if (currentBoard.getPiece(possiblePosition) == null) {
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
            }
        }

        for (int j = currentCol + 1; j <= 8; j++) {
            ChessPosition possiblePosition = new ChessPosition(currentRow, j);
            if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor != this.pieceColor){
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
                break;
            } else if (currentBoard.getPiece(possiblePosition) != null && currentBoard.getPiece(possiblePosition).pieceColor == this.pieceColor){
                break;
            } else if (currentBoard.getPiece(possiblePosition) == null) {
                ChessMove possibleMove = new ChessMove(currentPosition, possiblePosition, null);
                possibleMoves.add(possibleMove);
            }
        }

        return possibleMoves;

    }

    private Collection<ChessMove> pMoves(ChessBoard currentBoard, ChessPosition currentPosition) {
        int currentRow= currentPosition.getRow();
        int currentCol= currentPosition.getColumn();

        Collection<ChessMove> possibleMoves=new HashSet<>();
        Collection<ChessPosition> possiblePositions=new HashSet<>();

        if (currentBoard.getPiece(currentPosition).pieceColor == ChessGame.TeamColor.WHITE){
            ChessPosition arriba = new ChessPosition(currentRow + 1, currentCol);
            if (currentBoard.getPiece(arriba) == null){
                possiblePositions.add(arriba);
                ChessPosition dosArriba = new ChessPosition(currentRow + 2, currentCol);
                if (currentRow == 2 && currentBoard.getPiece(dosArriba) == null){
                    possiblePositions.add(dosArriba);
                }
            }
            ChessPosition arribaIzq = new ChessPosition(currentRow + 1, currentCol - 1);
            ChessPosition arribaDer = new ChessPosition(currentRow + 1, currentCol + 1);
            if (currentBoard.getPiece(arribaIzq) != null && currentBoard.getPiece(arribaIzq).pieceColor != this.pieceColor){
                possiblePositions.add(arribaIzq);
            }
            if (currentBoard.getPiece(arribaDer) != null && currentBoard.getPiece(arribaDer).pieceColor != this.pieceColor){
                possiblePositions.add(arribaDer);
            }
        }

        else if (currentBoard.getPiece(currentPosition).pieceColor == ChessGame.TeamColor.BLACK){
            ChessPosition abajo = new ChessPosition(currentRow - 1, currentCol);
            if (currentBoard.getPiece(abajo) == null){
                possiblePositions.add(abajo);
                ChessPosition dosAbajo = new ChessPosition(currentRow - 2, currentCol);
                if (currentRow == 7 && currentBoard.getPiece(dosAbajo) == null){
                    possiblePositions.add(dosAbajo);
                }
            }
            ChessPosition abajoIzq = new ChessPosition(currentRow - 1, currentCol - 1);
            ChessPosition abajoDer = new ChessPosition(currentRow - 1, currentCol + 1);
            if (currentBoard.getPiece(abajoIzq) != null && currentBoard.getPiece(abajoIzq).pieceColor != this.pieceColor){
                possiblePositions.add(abajoIzq);
            }
            if (currentBoard.getPiece(abajoDer) != null && currentBoard.getPiece(abajoDer).pieceColor != this.pieceColor){
                possiblePositions.add(abajoDer);
            }
        }
        for(ChessPosition pos: possiblePositions) {
            if (pos.getRow() < 1 || pos.getRow() > 8 || pos.getColumn() < 1 || pos.getColumn() > 8){
                continue;
            }
            if (currentBoard.getPiece(pos) != null && currentBoard.getPiece(pos).pieceColor == this.pieceColor){
                continue;
            }
            else{
                if(currentPosition.getRow() == 7  && pos.getRow() == 8 || (currentPosition.getRow() == 2 && pos.getRow() == 1)) {
                    possibleMoves.add(new ChessMove(currentPosition, pos, PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(currentPosition, pos, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(currentPosition, pos, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(currentPosition, pos, PieceType.KNIGHT));
                }
                else{
                    possibleMoves.add(new ChessMove(currentPosition, pos, null));
                }
            }
        }

        return possibleMoves;
    }

    @Override
    public boolean equals(Object objetito){
        if (this == objetito) return true;
        if(objetito == null || getClass() != objetito.getClass()) return false;
        ChessPiece ese = (ChessPiece) objetito;
        return pieceType == ese.pieceType && pieceColor == ese.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceType, pieceColor);
    }

    @Override
    public String toString() {
        return "ChessPiece{" + "pieceType=" + pieceType + ", pieceColor=" + pieceColor + "}";
    }
}
