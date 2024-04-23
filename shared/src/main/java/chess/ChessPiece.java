package chess;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
public class ChessPiece implements Cloneable {
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
        Collection<ChessMove> potMov = new HashSet<>();
        switch (this.pieceType) {
            case ROOK:
                potMov = rMoves(board, myPosition);
                break;
            case QUEEN:
                potMov = qMoves(board, myPosition);
                break;
            case KING:
                potMov = kMoves(board, myPosition);
                break;
            case BISHOP:
                potMov = bMoves(board, myPosition);
                break;
            case KNIGHT:
                potMov = knightMoves(board, myPosition);
                break;
            case PAWN:
                potMov = pMoves(board, myPosition);
                break;
            default:
                return null;
        }
        return potMov;
    }
    private Collection<ChessMove> kMoves(ChessBoard currBoard, ChessPosition thisPos) {
        int thisRow= thisPos.getRow();
        int thisCol = thisPos.getColumn();
        Collection<ChessMove> posMoves= new HashSet<>();
        Collection<ChessPosition> posPostns= new HashSet<>();
        posPostns.add(new ChessPosition(thisRow + 1, thisCol - 1));
        posPostns.add(new ChessPosition(thisRow + 1, thisCol));
        posPostns.add(new ChessPosition(thisRow + 1, thisCol + 1));
        posPostns.add(new ChessPosition(thisRow, thisCol - 1));
        posPostns.add(new ChessPosition(thisRow, thisCol + 1));
        posPostns.add(new ChessPosition(thisRow - 1, thisCol - 1));
        posPostns.add(new ChessPosition(thisRow - 1, thisCol));
        posPostns.add(new ChessPosition(thisRow - 1, thisCol + 1));
        for (ChessPosition pos : posPostns) {
            if (obCheck(pos, currBoard)) {
                continue;
            } else {
                for (int i = pos.getRow() - 2; i < pos.getRow() + 2; i++) {
                    ChessPosition checkPos = new ChessPosition(i, pos.getColumn() - 2);
                    if (outOfRange(currBoard, thisPos, posMoves, pos, checkPos)) continue;
                    checkPos = new ChessPosition(i, pos.getColumn() - 1);
                    if (outOfRange(currBoard, thisPos, posMoves, pos, checkPos)) continue;
                    checkPos = new ChessPosition(i, pos.getColumn() + 1);
                    if (outOfRange(currBoard, thisPos, posMoves, pos, checkPos)) continue;
                    checkPos = new ChessPosition(i, pos.getColumn() + 2);
                    if (outOfRange(currBoard, thisPos, posMoves, pos, checkPos)) continue;
                }
                for (int j = pos.getColumn() - 2; j < pos.getColumn() + 2; j++) {
                    ChessPosition checkPos = new ChessPosition(pos.getRow() + 2, j);
                    if (outOfRange(currBoard, thisPos, posMoves, pos, checkPos)) continue;
                    checkPos = new ChessPosition(pos.getRow() + 1, j);
                    if (outOfRange(currBoard, thisPos, posMoves, pos, checkPos)) continue;
                    checkPos = new ChessPosition(pos.getRow() - 1, j);
                    if (outOfRange(currBoard, thisPos, posMoves, pos, checkPos)) continue;
                    checkPos = new ChessPosition(pos.getRow() - 2, j);
                    if (outOfRange(currBoard, thisPos, posMoves, pos, checkPos)) continue;
                }
            }
        }

        return posMoves;
    }
    public boolean outOfRange(ChessBoard thisBoard, ChessPosition thisPos, Collection<ChessMove> posMoves, ChessPosition position, ChessPosition chckPos) {
        if (chckPos.getRow() < 1 || chckPos.getRow() > 8 || chckPos.getColumn() < 1 || chckPos.getColumn() > 8) {
            return true;
        }
        if (thisBoard.getPiece(chckPos) == null || (thisBoard.getPiece(chckPos) != null && thisBoard.getPiece(chckPos).pieceType != PieceType.KING)) {
            posMoves.add(new ChessMove(thisPos, position, null));
        }
        return false;
    }
    private Collection<ChessMove> qMoves(ChessBoard currBoard, ChessPosition thisPos) {
        int currRow = thisPos.getRow();
        int currCol= thisPos.getColumn();
        Collection<ChessMove> posMoves= new HashSet<>();
        Collection<ChessMove> posDMoves= new HashSet<>();
        posMoves= checkRow(currBoard, thisPos, currRow, currCol);
        posDMoves= checkDiagonal(currBoard, thisPos, currRow, currCol);
        posMoves.addAll(posDMoves);
        return posMoves;
    }
    private Collection<ChessMove> bMoves(ChessBoard currBoard, ChessPosition thisPos) {
        int currentRow = thisPos.getRow();
        int currentCol = thisPos.getColumn();
        return checkDiagonal(currBoard, thisPos, currentRow, currentCol);
    }
    private Collection<ChessMove> knightMoves(ChessBoard currBoard, ChessPosition thisPos) {
        int thisRow= thisPos.getRow();
        int thisCol = thisPos.getColumn();
        Collection<ChessMove> posMoves= new HashSet<>();
        Collection<ChessPosition> posPostns= new HashSet<>();
        posPostns.add(new ChessPosition(thisRow + 1, thisCol - 2));
        posPostns.add(new ChessPosition(thisRow + 1, thisCol + 2));
        posPostns.add(new ChessPosition(thisRow + 2, thisCol - 1));
        posPostns.add(new ChessPosition(thisRow + 2, thisCol + 1));
        posPostns.add(new ChessPosition(thisRow - 1, thisCol - 2));
        posPostns.add(new ChessPosition(thisRow - 1, thisCol + 2));
        posPostns.add(new ChessPosition(thisRow - 2, thisCol - 1));
        posPostns.add(new ChessPosition(thisRow - 2, thisCol + 1));
        for (ChessPosition pos : posPostns) {
            if (obCheck(pos, currBoard)) {
                continue;
            } else {
                posMoves.add(new ChessMove(thisPos, pos, null));
            }
        }
        return posMoves;
    }
    private Collection<ChessMove> pMoves(ChessBoard currBoard, ChessPosition thisPos) {
        int thisRow= thisPos.getRow();
        int thisCol= thisPos.getColumn();
        Collection<ChessPosition> posPostns= new HashSet<>();
        Collection<ChessMove> posMoves= new HashSet<>();
        if (currBoard.getPiece(thisPos).pieceColor == ChessGame.TeamColor.WHITE) {
            if (thisRow < 8) {
                ChessPosition onTop = new ChessPosition(thisRow + 1, thisCol);
                if (currBoard.getPiece(onTop) == null) {
                    posPostns.add(onTop);
                    ChessPosition twoUp= new ChessPosition(thisRow + 2, thisCol);
                    if (thisRow == 2 && currBoard.getPiece(twoUp) == null) {
                        posPostns.add(twoUp);
                    }
                }
            }
            if (thisRow < 8 && thisCol > 1) {
                ChessPosition leftUp= new ChessPosition(thisRow + 1, thisCol - 1);
                if (currBoard.getPiece(leftUp) != null && currBoard.getPiece(leftUp).pieceColor != this.pieceColor) {
                    posPostns.add(leftUp);
                }
            }
            if (thisRow < 8 && thisCol < 8) {
                ChessPosition arribaDer= new ChessPosition(thisRow + 1, thisCol + 1);
                if (currBoard.getPiece(arribaDer) != null && currBoard.getPiece(rightUp).pieceColor != this.pieceColor) {
                    posPostns.add(arribaDer);
                }
            }
        }
        else if (currBoard.getPiece(thisPos).pieceColor == ChessGame.TeamColor.BLACK) {
            if (thisRow > 1) {
                ChessPosition bot= new ChessPosition(thisRow - 1, thisCol);
                if (currBoard.getPiece(bot) == null) {
                    posPostns.add(bot);
                    ChessPosition twoBelow = new ChessPosition(thisRow - 2, thisCol);
                    if (thisRow == 7 && currBoard.getPiece(twoBelow) == null) {
                        posPostns.add(twoBelow);
                    }
                }
            }
            if (thisRow > 1 && thisCol > 1) {
                ChessPosition abajoIz = new ChessPosition(thisRow - 1, thisCol - 1);
                if (currBoard.getPiece(abajoIz) != null && currBoard.getPiece(leftDown).pieceColor != this.pieceColor) {
                    posPostns.add(abajoIz);
                }
            }
            if (thisRow > 1 && thisCol < 8) {
                ChessPosition downRight = new ChessPosition(thisRow - 1, thisCol + 1);
                if (currBoard.getPiece(downRight) != null && currBoard.getPiece(downRight).pieceColor != this.pieceColor) {
                    posPostns.add(downRight);
                }
            }
        }
        for (ChessPosition pos : posPostns) {
            if (obCheck(pos, currBoard)) {
                continue;
            } else {
                if ((thisPos.getRow() == 7 && pos.getRow() == 8) || (thisPos.getRow() == 2 && pos.getRow() == 1)) {
                    posMoves.add(new ChessMove(thisPos, pos, PieceType.QUEEN));
                    posMoves.add(new ChessMove(thisPos, pos, PieceType.BISHOP));
                    posMoves.add(new ChessMove(thisPos, pos, PieceType.KNIGHT));
                    posMoves.add(new ChessMove(thisPos, pos, PieceType.ROOK));
                } else {
                    posMoves.add(new ChessMove(thisPos, pos, null));
                }
            }
        }
        return posMoves;
    }
    private Collection<ChessMove> rMoves(ChessBoard currBoard, ChessPosition thisPos) {
        int thisRow = thisPos.getRow();
        int thisCol = thisPos.getColumn();
        return checkRow(currBoard, thisPos, thisRow, thisCol);
    }
    private Collection<ChessMove> checkRow(ChessBoard myBoard, ChessPosition thisPos, int thisRow, int thisCol) {
        Collection<ChessMove> posMoves= new HashSet<>();
        for (int i = thisRow + 1; i <= 8; i++) {
            ChessPosition posPosition = new ChessPosition(i, thisCol);
            procPotMove(myBoard, thisPos, posPosition, posMoves);
            if (myBoard.getPiece(posPosition) != null) break;
        }
        for (int i = thisRow - 1; i > 0; i--) {
            ChessPosition posPosition = new ChessPosition(i, thisCol);
            procPotMove(myBoard, thisPos, posPosition, posMoves);
            if (myBoard.getPiece(posPosition) != null) break;
        }
        for (int j = thisCol - 1; j > 0; j--) {
            ChessPosition posPosition = new ChessPosition(thisRow, j);
            procPotMove(myBoard, thisPos, posPosition, posMoves);
            if (myBoard.getPiece(posPosition) != null) break;
        }
        for (int j = thisCol + 1; j <= 8; j++) {
            ChessPosition posPosition = new ChessPosition(thisRow, j);
            procPotMove(myBoard, thisPos, posPosition, posMoves);
            if (myBoard.getPiece(posPosition) != null) break;
        }
        return posMoves;
    }
    private Collection<ChessMove> checkDiagonal(ChessBoard currBoard, ChessPosition thisPos, int thisRow, int thisCol) {
        Collection<ChessMove> posMoves= new HashSet<>();
        for (int i=thisRow + 1, j=thisCol - 1; i <= 8 && j > 0; i++, j--) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
            procPotMove(currBoard, thisPos, potentialPosition, posMoves);
            if (currBoard.getPiece(potentialPosition) != null) break;
        }
        for (int i=thisRow + 1, j=thisCol + 1; i <= 8 && j <= 8; i++, j++) {
            ChessPosition posPosition = new ChessPosition(i, j);
            procPotMove(currBoard, thisPos, posPosition, posMoves);
            if (currBoard.getPiece(posPosition) != null) break;
        }
        for (int i=thisRow - 1, j=thisCol - 1; i > 0 && j > 0; i--, j--) {
            ChessPosition posPosition = new ChessPosition(i, j);
            procPotMove(currBoard, thisPos, posPosition, posMoves);
            if (currBoard.getPiece(posPosition) != null) break;
        }
        for (int i=thisRow - 1, j=thisCol + 1; i > 0 && j <= 8; i--, j++) {
            ChessPosition posPosition = new ChessPosition(i, j);
            procPotMove(currBoard, thisPos, posPosition, posMoves);
            if (currBoard.getPiece(posPosition) != null) break;
        }
        return posMoves;}
    public void procPotMove(ChessBoard thisBoard, ChessPosition thisPos, ChessPosition pPos, Collection<ChessMove> potentialMoves) {
        ChessPiece potPiece = thisBoard.getPiece(pPos);
        if (potPiece != null) {
            if (potPiece.pieceColor != this.pieceColor) {
                ChessMove potMove = new ChessMove(thisPos, pPos, null);
                potentialMoves.add(potMove);}
            return;}
        ChessMove pMove = new ChessMove(thisPos, pPos, null);
        potentialMoves.add(pMove);}
    private boolean obCheck(ChessPosition pos, ChessBoard thisBoard) {
        boolean ob = false;
        if (pos.getRow() < 1 || pos.getRow() > 8 || pos.getColumn() < 1 || pos.getColumn() > 8) {
            ob = true;}
        else if (thisBoard.getPiece(pos) != null && thisBoard.getPiece(pos).pieceColor == this.pieceColor) {
            ob = true;}
        return ob;}
    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceType=" + pieceType +
                ", pieceColor=" + pieceColor +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceType == that.pieceType && pieceColor == that.pieceColor;
    }
    @Override
    public ChessPiece clone() {
        try {
            return (ChessPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
    @Override
    public int hashCode() {
        return Objects.hash(pieceType, pieceColor);
    }
}
