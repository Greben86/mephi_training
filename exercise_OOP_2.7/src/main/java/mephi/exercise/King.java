package mephi.exercise;

// Король
public class King extends ChessPiece {

    public King(ColorPlayer color) {
        super(color, "K");
    }

    @Override
    public boolean canMoveToPosition(ChessBoard chessBoard, int startLine, int startColumn, int endLine, int endColumn) {
        // Начальная и конечная точки совпадают или какая-то из точек за пределами доски
        if (!super.canMoveToPosition(chessBoard, startLine, startColumn, endLine, endColumn)) {
            return false;
        }

        // Король ходит в любое поле вокруг себя, разница по любому направлению не может быть больше одной клетки
        if (Math.abs(startLine - endLine) <= 1 && Math.abs(startColumn - endColumn) <= 1) {
            return chessBoard.getChessPieceByPosition(endLine, endColumn) != null
                    && !getColor().equals(chessBoard.getChessPieceByPosition(endLine, endColumn).getColor());
        }

        return false;
    }
}
