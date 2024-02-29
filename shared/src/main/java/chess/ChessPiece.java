package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class ChessPiece implements Cloneable{

    private PieceType pieceType;
    private ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceType = type;
        this.pieceColor = pieceColor;
    }

    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }


    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }


    public PieceType getPieceType() {
        return this.pieceType;
    }


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

    public boolean outOfRange(ChessBoard myBoard, ChessPosition currentPos, Collection<ChessMove> potentialMoves, ChessPosition pos, ChessPosition checkPos) {
        if (checkPos.getRow() < 1 || checkPos.getRow() > 8 || checkPos.getColumn() < 1 || checkPos.getColumn() > 8) {
            return true;
        }
        if (myBoard.getPiece(checkPos) == null || (myBoard.getPiece(checkPos) != null && myBoard.getPiece(checkPos).pieceType != PieceType.KING)) {
            potentialMoves.add(new ChessMove(currentPos, pos, null));
        }
        return false;
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

        for (ChessPosition pos: possiblePositions) {
            if (obCheck(pos, currentBoard)) {
                continue;
            } else {
                for (int i = pos.getRow() - 2; i < pos.getRow() + 2; i++) {
                    ChessPosition checkPos = new ChessPosition(i, pos.getColumn() - 2);
                    if (outOfRange(currentBoard, currentPosition, possibleMoves, pos, checkPos)) continue;
                    checkPos = new ChessPosition(i, pos.getColumn() - 1);
                    if (outOfRange(currentBoard, currentPosition, possibleMoves, pos, checkPos)) continue;
                    checkPos = new ChessPosition(i, pos.getColumn() + 1);
                    if (outOfRange(currentBoard, currentPosition, possibleMoves, pos, checkPos)) continue;
                    checkPos = new ChessPosition(i, pos.getColumn() + 2);
                    if (outOfRange(currentBoard, currentPosition, possibleMoves, pos, checkPos)) continue;
                }
                for (int j = pos.getColumn() - 2; j < pos.getColumn() + 2; j++) {
                    ChessPosition checkPos = new ChessPosition(pos.getRow() + 2, j);
                    if (outOfRange(currentBoard, currentPosition, possibleMoves, pos, checkPos)) continue;
                    checkPos = new ChessPosition(pos.getRow() + 1, j);
                    if (outOfRange(currentBoard, currentPosition, possibleMoves, pos, checkPos)) continue;
                    checkPos = new ChessPosition(pos.getRow() - 1, j);
                    if (outOfRange(currentBoard, currentPosition, possibleMoves, pos, checkPos)) continue;
                    checkPos = new ChessPosition(pos.getRow() - 2, j);
                    if (outOfRange(currentBoard, currentPosition, possibleMoves, pos, checkPos)) continue;
                }
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
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();

        Collection<ChessMove> possibleMoves=new HashSet<>();

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

    private Collection<ChessMove> pMoves(ChessBoard myBoard, ChessPosition currentPosition) {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getColumn();
        Collection<ChessMove> possibleMoves = new HashSet<>();
        Collection<ChessPosition> possiblePositions = new HashSet<>();
                if (myBoard.getPiece(currentPosition).pieceColor == ChessGame.TeamColor.WHITE) {
            if (currentRow < 8) {
                ChessPosition above = new ChessPosition(currentRow + 1, currentCol);
                if (myBoard.getPiece(above) == null) {
                    possiblePositions.add(above);
                    ChessPosition twoAbove = new ChessPosition(currentRow + 2, currentCol);
                    if (currentRow == 2 && myBoard.getPiece(twoAbove) == null) {
                        possiblePositions.add(twoAbove);
                    }
                }
            }
            if (currentRow < 8 && currentCol > 1){
                ChessPosition upLeft = new ChessPosition(currentRow + 1, currentCol - 1);
                if (myBoard.getPiece(upLeft) != null && myBoard.getPiece(upLeft).pieceColor != this.pieceColor) {
                    possiblePositions.add(upLeft);
                }
            }
            if (currentRow < 8 && currentCol < 8) {
                ChessPosition upRight = new ChessPosition(currentRow + 1, currentCol + 1);
                if (myBoard.getPiece(upRight) != null && myBoard.getPiece(upRight).pieceColor != this.pieceColor) {
                    possiblePositions.add(upRight);
                }
            }
        }
        else if (myBoard.getPiece(currentPosition).pieceColor == ChessGame.TeamColor.BLACK) {
            if (currentRow > 1) {
                ChessPosition below=new ChessPosition(currentRow - 1, currentCol);
                if (myBoard.getPiece(below) == null) {
                    possiblePositions.add(below);
                    ChessPosition twoBelow=new ChessPosition(currentRow - 2, currentCol);
                    if (currentRow == 7 && myBoard.getPiece(twoBelow) == null) {
                        possiblePositions.add(twoBelow);
                    }
                }
            }
            if (currentRow > 1 && currentCol > 1) {
                ChessPosition downLeft = new ChessPosition(currentRow - 1, currentCol - 1);
                if (myBoard.getPiece(downLeft) != null && myBoard.getPiece(downLeft).pieceColor != this.pieceColor) {
                    possiblePositions.add(downLeft);
                }
            }
            if (currentRow > 1 && currentCol < 8) {
                ChessPosition downRight = new ChessPosition(currentRow - 1, currentCol + 1);
                if (myBoard.getPiece(downRight) != null && myBoard.getPiece(downRight).pieceColor != this.pieceColor) {
                    possiblePositions.add(downRight);
                }
            }
        }
        for (ChessPosition pos: possiblePositions) {
            if (pos.getRow() < 1 || pos.getRow() > 8 || pos.getColumn() < 1 || pos.getColumn() > 8) {
                continue;
            }
            if (myBoard.getPiece(pos) != null && myBoard.getPiece(pos).pieceColor == this.pieceColor) {
                continue;
            }
            else{
                if ((currentPosition.getRow() == 7 && pos.getRow() == 8) || (currentPosition.getRow() == 2 && pos.getRow() == 1)) {
                    possibleMoves.add(new ChessMove(currentPosition, pos, PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(currentPosition, pos, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(currentPosition, pos, PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(currentPosition, pos, PieceType.ROOK));
                }
                else{
                    possibleMoves.add(new ChessMove(currentPosition, pos, null));
                }
            }
        }

        return possibleMoves;
    }

    @Override
    public ChessPiece clone() {
        try {
            return (ChessPiece) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new InternalError(ex);
        }
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
