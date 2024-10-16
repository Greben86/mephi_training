package mephi.exercise;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public abstract class ChessPiece {

    private final String color;
    @Setter
    private boolean check = true;

    abstract boolean canMoveToPosition(ChessBoard chessBoard, int startLine, int startColumn, int endLine, int endColumn);

    // Проверка по умолчанию, ни одна фигура не может выйти за пределы доски и остаться на своем месте при ходе
    protected final boolean defaultCheckPosition(ChessBoard chessBoard, int startLine, int startColumn, int endLine, int endColumn) {
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

        return true;
    }

    abstract String getSymbol();
}
