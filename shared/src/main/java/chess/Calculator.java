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

  public static List<ChessMove> knight(ChessBoard board, ChessPosition currentPosition, ChessGame.TeamColor teamColor) {
    List<ChessMove> possibleMoves = new ArrayList<>();
    int[][] knightJumps = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};

    for (int[] jump : knightJumps) {
      addIfValidMove(board, currentPosition, teamColor, possibleMoves, jump[0], jump[1]);
    }

    return possibleMoves;
  }

  public static List<ChessMove> pawn(ChessBoard board, ChessPosition currentPosition, ChessGame.TeamColor teamColor) {
    List<ChessMove> possibleMoves = new ArrayList<>();
    int rowChange = (teamColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
    boolean initialAdvance = (teamColor == ChessGame.TeamColor.WHITE && currentPosition.getRow() == 2)
            || (teamColor == ChessGame.TeamColor.BLACK && currentPosition.getRow() == 7);
    moveForward(board, currentPosition, teamColor, possibleMoves, rowChange, initialAdvance);
    captureDiagonals(board, currentPosition, teamColor, possibleMoves, rowChange);
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

  private static void moveForward(ChessBoard board, ChessPosition currentPosition, ChessGame.TeamColor teamColor, List<ChessMove> moves, int rowChange, boolean initialAdvance) {
    int newRow = currentPosition.getRow() + rowChange;
    if (newRow >= 1 && newRow <= 8) {
      ChessPosition oneStep = new ChessPosition(newRow, currentPosition.getColumn());
      if (board.getPiece(oneStep) == null) {
        if (oneStep.getRow() == 1 || oneStep.getRow() == 8) {
          promotePawn(currentPosition, moves, oneStep);
        } else {
          moves.add(new ChessMove(currentPosition, oneStep, null));
          if (initialAdvance) {
            int twoStepRow = oneStep.getRow() + rowChange;
            if (twoStepRow >= 1 && twoStepRow <= 8) {
              ChessPosition twoStep = new ChessPosition(twoStepRow, oneStep.getColumn());
              if (board.getPiece(twoStep) == null) {
                moves.add(new ChessMove(currentPosition, twoStep, null));
              }
            }
          }
        }
      }
    }
  }

  private static void captureDiagonals(ChessBoard board, ChessPosition currentPosition, ChessGame.TeamColor teamColor, List<ChessMove> moves, int rowChange) {
    ChessPosition[] attacks = {
            new ChessPosition(currentPosition.getRow() + rowChange, currentPosition.getColumn() - 1),
            new ChessPosition(currentPosition.getRow() + rowChange, currentPosition.getColumn() + 1)
    };

    for (ChessPosition attackPos : attacks) {
      if (attackPos.getRow() >= 1 && attackPos.getRow() <= 8 && attackPos.getColumn() >= 1 && attackPos.getColumn() <= 8) {
        ChessPiece piece = board.getPiece(attackPos);
        if (piece != null && piece.getTeamColor() != teamColor) {
          if (attackPos.getRow() == 1 || attackPos.getRow() == 8) {
            promotePawn(currentPosition, moves, attackPos);
          } else {
            moves.add(new ChessMove(currentPosition, attackPos, null));
          }
        }
      }
    }
  }

  private static void promotePawn(ChessPosition currentPosition, List<ChessMove> moves, ChessPosition newPosition) {
    moves.add(new ChessMove(currentPosition, newPosition, ChessPiece.PieceType.QUEEN));
    moves.add(new ChessMove(currentPosition, newPosition, ChessPiece.PieceType.ROOK));
    moves.add(new ChessMove(currentPosition, newPosition, ChessPiece.PieceType.BISHOP));
    moves.add(new ChessMove(currentPosition, newPosition, ChessPiece.PieceType.KNIGHT));
  }
}
