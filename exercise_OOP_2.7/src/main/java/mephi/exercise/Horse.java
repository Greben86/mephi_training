package mephi.exercise;

// Конь
public class Horse extends ChessPiece {

    public Horse(String color) {
        super(color);
    }

    @Override
    boolean canMoveToPosition(ChessBoard chessBoard, int startLine, int startColumn, int endLine, int endColumn) {
        // Начальная и конечная точки совпадают или какая-то из точек за пределами доски
        if (!defaultCheckPosition(chessBoard, startLine, startColumn, endLine, endColumn)) {
            return false;
        }

        // Конь ходит буквой Г: если по горизонтали на 1 клетку, то по вертикали на 2 или наоборот
        if ((Math.abs(startLine - endLine) == 1 && Math.abs(startColumn - endColumn) == 2)
                || (Math.abs(startLine - endLine) == 2 && Math.abs(startColumn - endColumn) == 1)) {
            return true;
        }

        return false;
    }

    @Override
    String getSymbol() {
        return "H";
    }
}
