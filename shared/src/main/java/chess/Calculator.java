package chess;

import java.util.List;
import java.util.ArrayList;

public class Calculator {

  public static List<ChessMove> bishop(ChessBoard board, ChessPosition currentPosition, ChessGame.TeamColor teamColor) {
    List<ChessMove> possibleMoves = new ArrayList<>();

    addMovesInDirection(1, 1, currentPosition, board, possibleMoves, teamColor);
    addMovesInDirection(1, -1, currentPosition, board, possibleMoves, teamColor);
    addMovesInDirection(-1, -1, currentPosition, board, possibleMoves, teamColor);
    addMovesInDirection(-1, 1, currentPosition, board, possibleMoves, teamColor);

    return possibleMoves;
  }

  public static List<ChessMove> rook(ChessBoard board, ChessPosition currentPosition, ChessGame.TeamColor teamColor) {
    List<ChessMove> possibleMoves = new ArrayList<>();

    addMovesInDirection(1, 0, currentPosition, board, possibleMoves, teamColor);
    addMovesInDirection(-1, 0, currentPosition, board, possibleMoves, teamColor);
    addMovesInDirection(0, 1, currentPosition, board, possibleMoves, teamColor);
    addMovesInDirection(0, -1, currentPosition, board, possibleMoves, teamColor);

    return possibleMoves;
  }

  public static List<ChessMove> queen(ChessBoard board, ChessPosition currentPosition, ChessGame.TeamColor teamColor) {
    List<ChessMove> queenMoves = new ArrayList<>(bishop(board, currentPosition, teamColor));
    queenMoves.addAll(rook(board, currentPosition, teamColor));
    return queenMoves;
  }

  public static List<ChessMove> king(ChessBoard board, ChessPosition currentPosition, ChessGame.TeamColor teamColor) {
    List<ChessMove> possibleMoves = new ArrayList<>();
    int[] moveOffsets = {-1, 0, 1};

    for (int rowOffset : moveOffsets) {
      for (int colOffset : moveOffsets) {
        if (rowOffset != 0 || colOffset != 0) {
          addIfValidMove(board, currentPosition, teamColor, possibleMoves, rowOffset, colOffset);
        }
      }
    }

    return possibleMoves;
  }

  private static void addMovesInDirection(int rowInc, int colInc, ChessPosition currentPosition, ChessBoard board, List<ChessMove> moves, ChessGame.TeamColor teamColor) {
    int row = currentPosition.getRow() + rowInc;
    int col = currentPosition.getColumn() + colInc;

    while (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
      ChessPosition newPos = new ChessPosition(row, col);
      ChessPiece piece = board.getPiece(newPos);

      if (piece == null) {
        moves.add(new ChessMove(currentPosition, newPos, null));
      } else {
        if (piece.getTeamColor() != teamColor) {
          moves.add(new ChessMove(currentPosition, newPos, null));
        }
        break;
      }
      row += rowInc;
      col += colInc;
    }
  }

  private static void addIfValidMove(ChessBoard board, ChessPosition currentPosition, ChessGame.TeamColor teamColor, List<ChessMove> moves, int rowOffset, int colOffset) {
    int newRow = currentPosition.getRow() + rowOffset;
    int newCol = currentPosition.getColumn() + colOffset;

    if (newRow > 0 && newRow <= 8 && newCol > 0 && newCol <= 8) {
      ChessPosition newPos = new ChessPosition(newRow, newCol);
      ChessPiece piece = board.getPiece(newPos);
      if (piece == null || piece.getTeamColor() != teamColor) {
        moves.add(new ChessMove(currentPosition, newPos, null));
      }
    }
  }

}
