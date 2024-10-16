package mephi.exercise;

// Ладья
public class Rook extends ChessPiece {

    public Rook(String color) {
        super(color);
    }

    @Override
    boolean canMoveToPosition(ChessBoard chessBoard, int startLine, int startColumn, int endLine, int endColumn) {
        // Начальная и конечная точки совпадают или какая-то из точек за пределами доски
        if (!defaultCheckPosition(chessBoard, startLine, startColumn, endLine, endColumn)) {
            return false;
        }

        // Ладья ходит по прямой, по этому одна координата не меняется
        if (startLine == endLine || startColumn == endColumn) {
            return true;
        }

        return false;
    }

    @Override
    String getSymbol() {
        return "R";
    }
}
