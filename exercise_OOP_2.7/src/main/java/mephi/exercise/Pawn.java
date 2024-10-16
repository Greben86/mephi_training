package mephi.exercise;

// Пешка
public class Pawn extends ChessPiece {

    public Pawn(String color) {
        super(color);
    }

    @Override
    boolean canMoveToPosition(ChessBoard chessBoard, int startLine, int startColumn, int endLine, int endColumn) {
        // Начальная и конечная точки совпадают или какая-то из точек за пределами доски
        if (!defaultCheckPosition(chessBoard, startLine, startColumn, endLine, endColumn)) {
            return false;
        }

        // Пешка не может шагнуть в бок более чем на одну клетку
        if (Math.abs(startColumn - endColumn) > 1) {
            return false;
        }

        // Пешка при первом ходе может шагнуть на две клетки, но только прямо
        if (("White".equals(getColor()) && startLine == 1 && endLine == 3)
                || ("Black".equals(getColor()) && startLine == 6 && endLine == 4)) {
            if (startColumn == endColumn) {
                return true;
            }
        }

        // Если ход не первый или пешка атакует, то можно уйти только на одну клетку вперед
        if (("White".equals(getColor()) && endLine - startLine == 1)
                || ("Black".equals(getColor()) && startLine - endLine == 1)) {
            return true;
        }

        return false;
    }

    @Override
    String getSymbol() {
        return "P";
    }
}
