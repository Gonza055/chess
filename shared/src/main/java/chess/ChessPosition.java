package chess;
import java.util.Objects;
public class ChessPosition {
    private final int row;
    private final int col;
    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.col;
    }
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "(" + row +
                ", " + col +
                ")";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition position = (ChessPosition) o;
        return row == position.row && col == position.col;
    }


}
