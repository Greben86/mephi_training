package mephi.exercise;

// Слон
public class Bishop extends ChessPiece {

    public Bishop(String color) {
        super(color);
    }

    @Override
    boolean canMoveToPosition(ChessBoard chessBoard, int startLine, int startColumn, int endLine, int endColumn) {
        // Начальная и конечная точки совпадают или какая-то из точек за пределами доски
        if (!defaultCheckPosition(chessBoard, startLine, startColumn, endLine, endColumn)) {
            return false;
        }

        // Слон ходит по диагонали, по этому разница между точками по горизонтали должна быть равна разнице точек по вертикали
        if (Math.abs(startLine - endLine) == Math.abs(startColumn - endColumn)) {
            return true;
        }

        return false;
    }

    @Override
    String getSymbol() {
        return "B";
    }
}
