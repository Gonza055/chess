package chess;
import java.util.Arrays;
import java.util.Collection;
public class ChessBoard implements Cloneable{
    private ChessPiece[][] board = new ChessPiece[8][8];
    public ChessBoard() {}
    public void addPiece(ChessPosition pos, ChessPiece piece) {
        board[pos.getRow() - 1][pos.getColumn() - 1] = piece;
    }
    public ChessPiece getPiece(ChessPosition pos) {
        return board[pos.getRow() - 1][pos.getColumn() - 1];
    }
    public void rstBoard() {
        board = new ChessPiece[8][8];
        this.addPiece(new ChessPosition(1,1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(2,1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(2,2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(2,3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(2,4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        this.addPiece(new ChessPosition(2,5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(2,6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(2,7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(2,8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(8,1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(7,1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(7,2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(7,3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(7,4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        this.addPiece(new ChessPosition(7,5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(7,6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(7,7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(7,8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
    }
    String bgWhite = "\u001B[47m";
    String bgBlack = "\u001B[40m";
    String bgYellow = "\u001B[43m";
    String bgGreen = "\u001B[42m";
    String blue = "\u001B[34m";
    String red = "\u001B[31m";
    String black = "\u001B[30m";
    String reset = "\u001B[0m";
    public String toString(String tColor) {
        StringBuilder newstrin = new StringBuilder();
        newstrin = buildBoard(tColor, newstrin, reset, black, red, blue, bgWhite, bgBlack);
        return newstrin.toString();
    }
    private StringBuilder buildBoard(String teamColor, StringBuilder sb, String reset, String black, String red, String blue, String bgWhite, String bgBlack) {
        sb.append(black).append(String.format("%4s", " ")).append("a  b  c  d  e  f  g  h").append(reset).append("\n");
        if (teamColor == null || teamColor.equals("WHITE")) {
            for (int i = 7; i >= 0; i--) {
                appRow(sb, i, reset, black, red, blue, bgBlack, bgWhite);
            }
        } else {
            for (int i = 0; i < 8; i++) {
                appRow(sb, i, reset, black, red, blue, bgBlack, bgWhite);
            }
        }
        sb.append(black).append(String.format("%4s", " ")).append("a  b  c  d  e  f  g  h").append(reset).append("\n");
        return sb;
    }
    public String toStringHighL(String teamColor, Collection<ChessMove> possibleMoves, ChessPosition yellowPos) {
        StringBuilder newstrin = new StringBuilder();
        newstrin.append(black).append(String.format("%4s", " ")).append("a  b  c  d  e  f  g  h").append(reset).append("\n");
        if (teamColor == null || teamColor.equals("WHITE")) {
            for (int i = 7; i >= 0; i--) {
                newstrin = appendHighLRow(newstrin, i, reset, black, red, blue, bgWhite, bgBlack, possibleMoves, yellowPos);
            }
        } else {
            for (int i = 0; i < 8; i++) {
                newstrin = appendHighLRow(newstrin, i, reset, black, red, blue, bgWhite, bgBlack, possibleMoves, yellowPos);
            }
        }
        newstrin.append(black).append(String.format("%4s", " ")).append("a  b  c  d  e  f  g  h").append(reset).append("\n");
        return newstrin.toString();
    }
    private StringBuilder appendHighLRow(StringBuilder sb, int i, String reset, String black, String red, String blue, String bgWhite, String bgBlack, Collection<ChessMove> possibleMoves, ChessPosition yellowPos) {
        sb.append(black).append(String.format("%2d", i + 1)).append(" ");
        for (int j = 0; j < 8; j++) {
            ChessPosition currentPosition = new ChessPosition(i + 1, j + 1);
            String bg = ((i + j) % 2 == 0) ? bgWhite : bgBlack;
            sb.append(bg);
            if (isPosMove(currentPosition, possibleMoves)) {
                sb.append(bgGreen);
            }
            else if (currentPosition.equals(yellowPos)){
                sb.append(bgYellow);
            }
            if (board[i][j] == null) {
                sb.append(String.format("%3s", " ")).append(reset);
            } else {
                String color = (board[i][j].getTeamColor() == ChessGame.TeamColor.WHITE) ? red : blue;
                char pieceChar;
                switch (board[i][j].getPieceType()) {
                    case PAWN: pieceChar = 'P'; break;
                    case ROOK: pieceChar = 'R'; break;
                    case KNIGHT: pieceChar = 'N'; break;
                    case BISHOP: pieceChar = 'B'; break;
                    case KING: pieceChar = 'K'; break;
                    case QUEEN: pieceChar = 'Q'; break;
                    default: pieceChar = ' '; break;
                }
                sb.append(color).append(" ").append(pieceChar).append(" ").append(reset);}
        }
        sb.append(black).append(" ").append(String.format("%2d", i + 1)).append(reset).append("\n");
        return sb;
    }
    private boolean isPosMove(ChessPosition position, Collection<ChessMove> possibleMoves) {
        if (possibleMoves != null) {
            for (ChessMove mov : possibleMoves) {
                if (mov.getEndPosition().equals(position)) {
                    return true;
                }
            }
        }
        return false;
    }
    private void appRow(StringBuilder sb, int i, String rst, String black, String red, String blue, String bgWhite, String bgBlack) {
        sb.append(black).append(String.format("%2d", i + 1)).append(" ");
        for (int j = 0; j < 8; j++) {
            String bg = ((i + j) % 2 == 0) ? bgWhite : bgBlack;
            if (board[i][j] == null) {
                sb.append(bg).append(String.format("%3s", " ")).append(rst);
            } else {
                String color = (board[i][j].getTeamColor() == ChessGame.TeamColor.WHITE) ? red : blue;
                char pieceChar;
                switch (board[i][j].getPieceType()) {
                    case ROOK: pieceChar = 'R'; break;
                    case KING: pieceChar = 'K'; break;
                    case KNIGHT: pieceChar = 'N'; break;
                    case BISHOP: pieceChar = 'B'; break;
                    case QUEEN: pieceChar = 'Q'; break;
                    case PAWN: pieceChar = 'P'; break;
                    default: pieceChar = ' '; break;
                }
                sb.append(bg).append(color).append(" ").append(pieceChar).append(" ").append(rst);
            }
        }
        sb.append(black).append(" ").append(String.format("%2d", i + 1)).append(rst).append("\n");
    }
    @Override
    public ChessBoard clone() {
        try {
            ChessBoard clone = (ChessBoard) super.clone();
            clone.board = new ChessPiece[8][8];
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (this.board[row][col] != null) {
                        clone.board[row][col] = this.board[row][col].clone();
                    }
                }
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }
}