package chess;
public class ChessMove {
    private final ChessPosition startPosition;
    private final ChessPosition endPostion;
    private final ChessPiece.PieceType promotionPiece;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPostion = endPosition;
        this.promotionPiece = promotionPiece;
    }
    public ChessPosition getStartPosition() {
        return this.startPosition;
    }
    public ChessPosition getEndPosition() {
        return this.endPostion;
    }
    public ChessPiece.PieceType getPromotionPiece() {
        return this.promotionPiece;
    }
    @Override
    public boolean equals(Object o) {
        if(o == this){return true;}
        if(!(o instanceof ChessMove)){return false;}
        ChessMove c = (ChessMove) o;
        return this.startPosition.equals(c.startPosition) && this.endPostion.equals(c.endPostion) && promotionPiece == c.promotionPiece;
    }
    @Override
    public String toString() {
        return "Move{" +
                startPosition +
                ", " + endPostion +
                ", " + promotionPiece +
                "}\n";
    }
    @Override
    public int hashCode() {
        int h = 0;
        h = startPosition.getRow() * 157 + startPosition.getColumn();
        h += endPostion.getRow() * 31 + endPostion.getColumn();
        if(promotionPiece != null){
            switch (promotionPiece){
                case BISHOP:
                    h += 17;
                    break;
                case QUEEN:
                    h += 11;
                    break;
                case ROOK:
                    h += 29;
                    break;
                case KNIGHT:
                    h += 23;
                    break;
                default:
                    h += 1;
            }
        }
        return h;
    }
}
