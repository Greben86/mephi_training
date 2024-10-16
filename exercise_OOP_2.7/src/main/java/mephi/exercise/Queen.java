package mephi.exercise;

// Ферзь
public class Queen extends ChessPiece {

    public Queen(String color) {
        super(color);
    }

    @Override
    boolean canMoveToPosition(ChessBoard chessBoard, int startLine, int startColumn, int endLine, int endColumn) {
        // Начальная и конечная точки совпадают или какая-то из точек за пределами доски
        if (!defaultCheckPosition(chessBoard, startLine, startColumn, endLine, endColumn)) {
            return false;
        }

        // Ферзь ходит и как слон, и как ладья,
        // по этому разница между точками по горизонтали должна быть равна разнице точек по вертикали
        if (Math.abs(startLine - endLine) == Math.abs(startColumn - endColumn)) {
            return true;
        }
        // или этому одна координата не меняется
        if (startLine == endLine || startColumn == endColumn) {
            return true;
        }

        return false;
    }

    @Override
    String getSymbol() {
        return "Q";
    }
}
