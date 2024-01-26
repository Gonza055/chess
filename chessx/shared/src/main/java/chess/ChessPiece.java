package chess;

import java.util.Collection;
import java.util.ArrayList;

public class ChessPiece {
    private final PieceType type;
    private final ChessGame.TeamColor pieceColor;
    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.type = type;
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
        return this.type;
    }



    public boolean isOccupied(ChessPosition position) {
        int row = position.getRow() - 1; // Adjust for 0-indexed array if needed
        int col = position.getColumn() - 1; // Adjust for 0-indexed array if needed
        return board[row][col] == null; // Returns true if there's a piece at the position
    }




    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList moves;
        moves=new ArrayList();
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
            ChessPosition startPosition=new ChessPosition(row, col);
            int[][] directions={{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            for (int[] direction : directions) {
                for (int i=1; i <= 8; i++) {
                    int newRow=row + i * direction[0];
                    int newCol=col + i * direction[1];
                    ChessPosition endPosition=new ChessPosition(newRow, newCol);
                    if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                        //if (board.isOccupied(endPosition)) {
                           // if (endPosition.isEnemy(endPosition, myPiece.getTeamColor())) {
                                moves.add(new ChessMove(startPosition, endPosition, myPiece.getPieceType()));
                            }
                            break;
                        } //else {
                            moves.add(new ChessMove(startPosition, endPosition, myPiece.getPieceType()));
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    return moves;
}}




