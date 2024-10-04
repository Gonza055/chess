package chess;

import java.util.List;

public class Calculator {

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
