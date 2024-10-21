package mephi.exercise;

// Ладья
public class Rook extends ChessPiece {

    public Rook(ColorPlayer color) {
        super(color, "R");
    }

    @Override
    public boolean canMoveToPosition(ChessBoard chessBoard, int startLine, int startColumn, int endLine, int endColumn) {
        // Начальная и конечная точки совпадают или какая-то из точек за пределами доски
        if (!super.canMoveToPosition(chessBoard, startLine, startColumn, endLine, endColumn)) {
            return false;
        }

        // Ладья ходит по прямой, по этому одна координата не меняется
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
            } while (line != endLine || column != endColumn);

            if (chessBoard.getBoard()[line][column] != null &&
                    this.getColor().equals(chessBoard.getBoard()[line][column].getColor())) {
                return false;
            }

            return true;
        }

        return false;
    }
}
