package mephi.exercise;

// Ферзь
public class Queen extends ChessPiece {

    public Queen(ColorPlayer color) {
        super(color, "Q");
    }

    @Override
    public boolean canMoveToPosition(ChessBoard chessBoard, int startLine, int startColumn, int endLine, int endColumn) {
        // Начальная и конечная точки совпадают или какая-то из точек за пределами доски
        if (!super.canMoveToPosition(chessBoard, startLine, startColumn, endLine, endColumn)) {
            return false;
        }

        // Ферзь ходит и как слон, и как ладья,
        // по этому разница между точками по горизонтали должна быть равна разнице точек по вертикали
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

        // или по этому одна координата не меняется
        if (startLine == endLine || startColumn == endColumn) {
            int line = startLine;
            int column = startColumn;
            do {
                if (chessBoard.getBoard()[line][column] != null &&
                        !this.equals(chessBoard.getBoard()[line][column])) {
                    return false;
                }
                line = startLine != endLine ? line+1 : line;
                column = startColumn != endColumn ? column+1 : column;
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
