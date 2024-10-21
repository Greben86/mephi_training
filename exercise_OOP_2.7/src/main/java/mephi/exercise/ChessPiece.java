package mephi.exercise;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public abstract class ChessPiece {

    private final ColorPlayer color;
    private final String symbol;
    @Setter
    private boolean check = true;

    public boolean canMoveToPosition(ChessBoard chessBoard, int startLine, int startColumn, int endLine, int endColumn) {
        // Начальная и конечная точки совпадают
        if (startLine == endLine && startColumn == endColumn) {
            return false;
        }

        // Какая-то из точек за пределами доски
        for (int pos : new int[]{startLine, startColumn, endLine, endColumn}) {
            if (!chessBoard.checkPos(pos)) {
                return false;
            }
        }

        return equals(chessBoard.getChessPieceByPosition(startLine, startColumn));
    }

    // Почти все фигуры атакуют так же как ходят
    public boolean canAttackToPosition(ChessBoard chessBoard, int startLine, int startColumn, int endLine, int endColumn) {
        if (!canMoveToPosition(chessBoard, startLine, startColumn, endLine, endColumn)) {
            return false;
        }

        return chessBoard.getChessPieceByPosition(endLine, endColumn) != null
                && !getColor().equals(chessBoard.getChessPieceByPosition(endLine, endColumn).getColor());
    }
}
