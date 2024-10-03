package chess;

import java.util.Arrays;
/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    public ChessPiece[][] tablero = new ChessPiece[8][8];
    public ChessBoard() {}

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        tablero[position.getRow() - 1][position.getColumn() - 1]= piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return tablero[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int i=0; i < 8; i++) {
            tablero[6][i]=new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            tablero[1][i]=new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }

        ChessGame.TeamColor[] colors={ChessGame.TeamColor.BLACK, ChessGame.TeamColor.WHITE};
        int[] startRows={7, 0};

        for (int row : startRows) {
            ChessGame.TeamColor color=colors[row == 7 ? 0 : 1];
            tablero[row][0]=new ChessPiece(color, ChessPiece.PieceType.ROOK);
            tablero[row][1]=new ChessPiece(color, ChessPiece.PieceType.KNIGHT);
            tablero[row][2]=new ChessPiece(color, ChessPiece.PieceType.BISHOP);
            tablero[row][3]=new ChessPiece(color, ChessPiece.PieceType.QUEEN);
            tablero[row][4]=new ChessPiece(color, ChessPiece.PieceType.KING);
            tablero[row][5]=new ChessPiece(color, ChessPiece.PieceType.BISHOP);
            tablero[row][6]=new ChessPiece(color, ChessPiece.PieceType.KNIGHT);
            tablero[row][7]=new ChessPiece(color, ChessPiece.PieceType.ROOK);
        }
    }
}
