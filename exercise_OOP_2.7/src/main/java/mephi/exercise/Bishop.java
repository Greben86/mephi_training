package mephi.exercise;

// Слон
public class Bishop extends ChessPiece {

    public Bishop(ColorPlayer color) {
        super(color, "B");
    }

    @Override
    public boolean canMoveToPosition(ChessBoard chessBoard, int startLine, int startColumn, int endLine, int endColumn) {
        // Начальная и конечная точки совпадают или какая-то из точек за пределами доски
        if (!super.canMoveToPosition(chessBoard, startLine, startColumn, endLine, endColumn)) {
            return false;
        }

        // Слон ходит по диагонали, по этому разница между точками по горизонтали должна быть равна разнице точек по вертикали
        if (Math.abs(startLine - endLine) == Math.abs(startColumn - endColumn)) {
            int line = startLine;
            int column = startColumn;
            do {
                if (chessBoard.getBoard()[line][column] != null &&
                        !this.equals(chessBoard.getBoard()[line][column])) {
                    return false;
                }
                line = endLine > startLine ? line+1 : line-1;
                column = endColumn > startColumn ? column+1 : column-1;
            } while (line == endLine && column == endColumn);

            if (chessBoard.getBoard()[line][column] != null &&
                    this.getColor().equals(chessBoard.getBoard()[line][column].getColor())) {
                return false;
            }

            return true;
        }

        return false;
    }
}
