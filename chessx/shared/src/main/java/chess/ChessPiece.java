package chess;

import java.util.Collection;
import java.util.ArrayList;
import static java.lang.Math.abs;


public class ChessPiece {
    private final PieceType type;
    private final ChessGame.TeamColor pieceColor;
    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.type=type;
        this.pieceColor=pieceColor;
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

        return switch (type) {
            case BISHOP -> bishopMoves(board, myPosition);
            case KING -> kingMoves(board, myPosition);
            case KNIGHT -> knightMoves(board, myPosition);
            case ROOK -> rookMoves(board, myPosition);
            case QUEEN -> queenMoves(board, myPosition);
            case PAWN -> (getTeamColor() == ChessGame.TeamColor.WHITE) ? wPawnMoves(board, myPosition) : bPawnMoves(board, myPosition);

        };
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        arribaDerecha(moves, board, myPosition);
        arribaIzquierda(moves, board, myPosition);
        abajoDerecha(moves, board, myPosition);
        abajoIzquierda(moves, board, myPosition);

        return moves;
    }


    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int pRow = myPosition.getRow();
        int pCol = myPosition.getColumn();
        for (int row = pRow-1; row <= pRow+1; ++row) {
            for (int col = pCol-1; col <= pCol+1; ++col) {
                if ((row >= 1) && (row <= 8) && (col >= 1) && (col <= 8)) {
                    ChessPiece otherPiece = board.getPiece(new ChessPosition(row,col));
                    if (otherPiece==null ||
                            otherPiece.getTeamColor() != getTeamColor()) {
                        moveSet(moves, pRow, pCol, row, col);
                    }
                }
            }
        }

        return moves;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int pRow = myPosition.getRow();
        int pCol = myPosition.getColumn();
        ChessPiece otherPiece;

        for (int row = -2; row <= 2; ++row) {
            if (row != 0) {
                if ((pRow + row) >= 1 && (pRow + row) <= 8) {
                    int col = (abs(row) == 1) ? 2 : 1;
                    if ((pCol + col) >= 1 && (pCol + col) <= 8) {
                        otherPiece = board.getPiece(new ChessPosition(pRow+row,pCol+col));
                        if (otherPiece == null || otherPiece.getTeamColor() != getTeamColor()) {
                            moveSet(moves,pRow,pCol,pRow+row,pCol+col);
                        }
                    }
                    col *= -1;
                    if ((pCol + col) >= 1 && (pCol + col) <= 8) {
                        otherPiece = board.getPiece(new ChessPosition(pRow+row,pCol+col));
                        if (otherPiece == null || otherPiece.getTeamColor() != getTeamColor()) {
                            moveSet(moves,pRow,pCol,pRow+row,pCol+col);
                        }
                    }
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        arriba(moves, board, myPosition);
        derecha(moves, board, myPosition);
        abajo(moves, board, myPosition);
        izquierda(moves, board, myPosition);

        return moves;
    }
    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        arriba(moves, board, myPosition);
        abajo(moves, board, myPosition);
        derecha(moves, board, myPosition);
        izquierda(moves, board, myPosition);
        arribaDerecha(moves, board, myPosition);
        arribaIzquierda(moves, board, myPosition);
        abajoDerecha(moves, board, myPosition);
        abajoIzquierda(moves, board, myPosition);

        return moves;
    }

    private Collection<ChessMove> wPawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int pRow = myPosition.getRow();
        int pCol = myPosition.getColumn();
        ChessPiece otherPiece;

        if(pRow < 8) {
            otherPiece = board.getPiece(new ChessPosition(pRow+1,pCol));
            if (otherPiece == null) {
                if(pRow == 7) {
                    moveSet(moves, pRow, pCol, pRow+1, pCol, true);
                }
                else {
                    moveSet(moves, pRow, pCol, pRow+1, pCol);
                }
                if (pRow == 2) {
                    otherPiece = board.getPiece(new ChessPosition(pRow+2, pCol));
                    if (otherPiece == null) {
                        moveSet(moves, pRow, pCol, pRow+2, pCol);
                    }
                }
            }
            if (pCol > 1) {
                otherPiece = board.getPiece(new ChessPosition(pRow + 1, pCol - 1));
                if (otherPiece != null &&
                        otherPiece.getTeamColor() != getTeamColor()) {
                    if (pRow == 7) {
                        moveSet(moves, pRow, pCol, pRow+1, pCol-1, true);
                    } else {
                        moveSet(moves, pRow, pCol, pRow+1, pCol-1);
                    }
                }
            }
            if (pCol < 8) {
                otherPiece = board.getPiece(new ChessPosition(pRow + 1, pCol + 1));
                if (otherPiece != null &&
                        otherPiece.getTeamColor() != getTeamColor()) {
                    if (pRow == 7) {
                        moveSet(moves, pRow, pCol, pRow+1, pCol+1, true);
                    } else {
                        moveSet(moves, pRow, pCol, pRow+1, pCol+1);
                    }
                }
            }
        }

        return moves;
    }

    private Collection<ChessMove> bPawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int pRow = myPosition.getRow();
        int pCol = myPosition.getColumn();
        ChessPiece otherPiece;

        if(pRow > 1) {
            otherPiece = board.getPiece(new ChessPosition(pRow-1,pCol));
            if (otherPiece == null) {
                if(pRow == 2) {
                    moveSet(moves, pRow, pCol, pRow+1, pCol, true);
                }
                else {
                    moveSet(moves, pRow, pCol, pRow+1, pCol);
                }
                if (pRow == 7) {
                    otherPiece = board.getPiece(new ChessPosition(pRow-2, pCol));
                    if (otherPiece == null) {
                        moveSet(moves, pRow, pCol, pRow-2, pCol);
                    }
                }
            }
            if (pCol > 1) {
                otherPiece = board.getPiece(new ChessPosition(pRow - 1, pCol - 1));
                if (otherPiece != null &&
                        otherPiece.getTeamColor() != getTeamColor()) {
                    if (pRow == 2) {
                        moveSet(moves, pRow, pCol, pRow-1, pCol-1, true);
                    } else {
                        moveSet(moves, pRow, pCol, pRow-1, pCol-1);
                    }
                }
            }
            if (pCol < 8) {
                otherPiece = board.getPiece(new ChessPosition(pRow - 1, pCol + 1));
                if (otherPiece != null &&
                        otherPiece.getTeamColor() != getTeamColor()) {
                    if (pRow == 2) {
                        moveSet(moves, pRow, pCol, pRow-1, pCol+1, true);
                    } else {
                        moveSet(moves, pRow, pCol, pRow-1, pCol+1);
                    }
                }
            }
        }

        return moves;
    }

    private void arriba(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        int pRow = myPosition.getRow();
        int pCol = myPosition.getColumn();
        for (int row = pRow+1; row <= 8; ++row) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(row, pCol));
            if(otherPiece==null) {
                moveSet(moves, pRow, pCol, row, pCol);
            }
            else {
                if (otherPiece.getTeamColor() != getTeamColor()) {
                    moveSet(moves, pRow, pCol, row, pCol);
                }
                break;
            }
        }
    }






















    private void arribaDerecha(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        int pieceRow = myPosition.getRow();
        int pieceCol = myPosition.getColumn();
        //north-east
        for(int row = pieceRow+1, col = pieceCol+1; row <= 8 && col <= 8; ++row, ++col) {
            ChessPiece existingPiece = board.getPiece(new ChessPosition(row, col));
            if(existingPiece==null) {
                moveSet(moves, pieceRow, pieceCol, row, col);
            }
            else {
                if (existingPiece.getTeamColor() != getTeamColor()) {
                    moveSet(moves, pieceRow, pieceCol, row, col);
                }
                break;
            }
        }
    }

    private void derecha(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        int pRow = myPosition.getRow();
        int pCol = myPosition.getColumn();
        //east
        for (int col = pCol+1; col <= 8; ++col) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(pRow, col));
            if(otherPiece==null) {
                moveSet(moves, pRow, pCol, pRow, col);
            }
            else {
                if (otherPiece.getTeamColor() != this.getTeamColor()) {
                    moveSet(moves, pRow, pCol, pRow, col);
                }
                break;
            }
        }
    }

    private void abajoDerecha(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        int pRow = myPosition.getRow();
        int pCol = myPosition.getColumn();
        //south-east
        for(int row = pRow-1, col = pCol+1; row >= 1 && col <= 8; --row, ++col) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(row, col));
            if(otherPiece==null) {
                moveSet(moves, pRow, pCol, row, col);
            }
            else {
                if (otherPiece.getTeamColor() != getTeamColor()) {
                    moveSet(moves, pRow, pCol, row, col);
                }
                break;
            }
        }
    }

    private void abajo(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        int pieceRow = myPosition.getRow();
        int pieceCol = myPosition.getColumn();
        //south
        for (int row = pieceRow-1; row >= 1; --row) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(row, pieceCol));
            if(otherPiece==null) {
                moveSet(moves, pieceRow, pieceCol, row, pieceCol);
            }
            else {
                if (otherPiece.getTeamColor() != getTeamColor()); {
                    moveSet(moves, pieceRow, pieceCol, row, pieceCol);
                }
                break;
            }
        }
    }

    private void abajoIzquierda(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        int pRow = myPosition.getRow();
        int pCol = myPosition.getColumn();
        for(int row = pRow-1, col = pCol-1; row >= 1 && col >= 1; --row, --col) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(row, col));
            if(otherPiece==null) {
                moveSet(moves, pRow, pCol, row, col);
            }
            else {
                if (otherPiece.getTeamColor() != getTeamColor()) {
                    moveSet(moves, pRow, pCol, row, col);
                }
                break;
            }
        }
    }

    private void izquierda(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        int pRow = myPosition.getRow();
        int pCol = myPosition.getColumn();
        for (int col = pCol-1; col >= 1; --col) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(pRow, col));
            if(otherPiece==null) {
                moveSet(moves, pRow, pCol, pRow, col);
            }
            else {
                if (otherPiece.getTeamColor() != getTeamColor()) {
                    moveSet(moves, pRow, pCol, pRow, col);
                }
                break;
            }
        }
    }

    private void arribaIzquierda(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        int pRow = myPosition.getRow();
        int pCol = myPosition.getColumn();
        for(int row = pRow+1, col = pCol-1; row <= 8 && col >= 1; ++row, --col) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(row, col));
            if(otherPiece==null) {
                moveSet(moves, pRow, pCol, row, col);
            }
            else {
                if (otherPiece.getTeamColor() != this.getTeamColor()) {
                    moveSet(moves, pRow, pCol, row, col);
                }
                break;
            }
        }
    }
    private void moveSet(Collection<ChessMove> moves, int startRow, int startCol, int endRow, int endCol) {
        moves.add(new ChessMove(new ChessPosition(startRow,startCol), new ChessPosition(endRow,endCol), null));
    }
    private void moveSet(Collection<ChessMove> moves, int startRow, int startCol, int endRow, int endCol, boolean isPromoting) {
        if(isPromoting) {
            moves.add(new ChessMove(new ChessPosition(startRow, startCol),
                    new ChessPosition(endRow, endCol), PieceType.QUEEN));
            moves.add(new ChessMove(new ChessPosition(startRow, startCol),
                    new ChessPosition(endRow, endCol), PieceType.BISHOP));
            moves.add(new ChessMove(new ChessPosition(startRow, startCol),
                    new ChessPosition(endRow, endCol), PieceType.ROOK));
            moves.add(new ChessMove(new ChessPosition(startRow, startCol),
                    new ChessPosition(endRow, endCol), PieceType.KNIGHT));
        }
        else {
            moveSet(moves, startRow, startCol, endRow, endCol);
        }
    }
}

