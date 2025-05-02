package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
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
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> moves = new ArrayList<>();

        if (board == null || myPosition == null) {
            return moves;
        }

        int row = myPosition.getRow();
        int col = myPosition.getColumn();


        switch (type) {
            case PAWN:
                int direction = pieceColor == ChessGame.TeamColor.WHITE ? 1 : -1;
                int start_row = pieceColor == ChessGame.TeamColor.WHITE ? 2 : 7;

                if (row == start_row && isValidPosition(row + 2 * direction, col)) {
                    ChessPosition one_step = new ChessPosition(row + direction, col);
                    ChessPosition two_step = new ChessPosition(row + 2 * direction, col);
                    if (board.getPiece(one_step) == null && board.getPiece(two_step) == null) {
                        moves.add(new ChessMove(myPosition, two_step, null));
                    }
                }

                if (isValidPosition(row + direction, col)) {
                    ChessPosition new_pos = new ChessPosition(row + direction, col);
                    if (board.getPiece(new_pos) == null) {
                        if (row + direction == (pieceColor == ChessGame.TeamColor.WHITE ? 8 : 1)) {
                            moves.add(new ChessMove(myPosition, new_pos, ChessPiece.PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition, new_pos, ChessPiece.PieceType.ROOK));
                            moves.add(new ChessMove(myPosition, new_pos, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, new_pos, ChessPiece.PieceType.KNIGHT));
                        }
                        else {
                            moves.add(new ChessMove(myPosition, new_pos, null));
                        }
                    }

                }

                for (int col_offset : new int[]{-1, 1}) {
                    int new_row = row + direction;
                    int new_col = col + col_offset;

                    if (isValidPosition(new_row, new_col)) {
                        ChessPosition capture_pos = new ChessPosition(new_row, new_col);
                        ChessPiece targetPiece = board.getPiece(capture_pos);


                        if (targetPiece != null && targetPiece.getTeamColor() != pieceColor) {
                            if (new_row == (pieceColor == ChessGame.TeamColor.WHITE ? 8 : 1)) {
                                moves.add(new ChessMove(myPosition, capture_pos, PieceType.QUEEN));
                                moves.add(new ChessMove(myPosition, capture_pos, PieceType.ROOK));
                                moves.add(new ChessMove(myPosition, capture_pos, PieceType.BISHOP));
                                moves.add(new ChessMove(myPosition, capture_pos, PieceType.KNIGHT));
                            } else {
                                moves.add(new ChessMove(myPosition, capture_pos, null));
                            }
                        }
                    }
                }

                break;


            case KING:
                for (int row_offset = -1; row_offset <= 1; row_offset++) {
                    for (int col_offset = -1; col_offset <= 1; col_offset++) {

                        if (row_offset == 0 && col_offset == 0) continue;

                        int new_row = row + row_offset;
                        int new_col = col + col_offset;

                        if (isValidPosition(new_row, new_col)) {
                            ChessPosition new_pos = new ChessPosition(new_row, new_col);
                            ChessPiece targetPiece = board.getPiece(new_pos);
                            if (targetPiece == null || targetPiece.getTeamColor() != pieceColor) {
                                moves.add(new ChessMove(myPosition, new_pos, null));
                            }
                        }


                    }
                }
                break;


            case KNIGHT:
                int [][] knight_moves = {
                        {2, 1},
                        {2, -1},
                        {-2, 1},
                        {-2, -1},
                        {1, 2},
                        {1, -2},
                        {-1, 2},
                        {-1, -2}
                };

                for (int[] move : knight_moves) {
                    int new_row = row + move[0];
                    int new_col = col + move[1];

                    if (isValidPosition(new_row, new_col)) {
                        ChessPosition new_pos = new ChessPosition(new_row, new_col);
                        ChessPiece targetPiece = board.getPiece(new_pos);
                        if (targetPiece == null || targetPiece.getTeamColor() != pieceColor) {
                            moves.add(new ChessMove(myPosition, new_pos, null));
                        }
                    }
                }
                break;

            case ROOK:



            case QUEEN:
                for (int row_offset = 1; row_offset <= 8; row_offset++) {
                    for (int col_offset = 1; col_offset <= 8; col_offset++) {
                        int new_row = row + row_offset;
                        int new_col = col + col_offset;
                    }
                }



        }


    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >=0 && col < 8;
    }

    private Collection<ChessMove> getMove(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for (int [] dir : directions )
    };

}
