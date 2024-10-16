package mephi.exercise;

// Король
public class King extends ChessPiece {

    public King(String color) {
        super(color);
    }

    @Override
    boolean canMoveToPosition(ChessBoard chessBoard, int startLine, int startColumn, int endLine, int endColumn) {
        // Начальная и конечная точки совпадают или какая-то из точек за пределами доски
        if (!defaultCheckPosition(chessBoard, startLine, startColumn, endLine, endColumn)) {
            return false;
        }

        // Король ходит в любое поле вокруг себя, разница по любому направлению не может быть больше одной клетки
        // Сумма смещений не может быть больше 2 (1 или 0 по горизонтали + 1 или 0 по вертикали)
        if (Math.abs(startLine - endLine) + Math.abs(startColumn - endColumn) <= 2) {
            return true;
        }

        return false;
    }

    @Override
    String getSymbol() {
        return "K";
    }
}
