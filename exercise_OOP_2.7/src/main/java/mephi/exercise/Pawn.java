package mephi.exercise;

import static mephi.exercise.ColorPlayer.BLACK;
import static mephi.exercise.ColorPlayer.WHITE;

// Пешка
public class Pawn extends ChessPiece {

    public Pawn(ColorPlayer color) {
        super(color, "P");
    }

    @Override
    public boolean canMoveToPosition(ChessBoard chessBoard, int startLine, int startColumn, int endLine, int endColumn) {
        // Начальная и конечная точки совпадают или какая-то из точек за пределами доски
        if (!super.canMoveToPosition(chessBoard, startLine, startColumn, endLine, endColumn)) {
            return false;
        }

        // Пешка не может шагнуть в при ходе
        if (startColumn != endColumn) {
            return false;
        }

        // Пешка при первом ходе может шагнуть на две клетки, но только прямо
        if (isCheck() && Math.abs(endLine - startLine) == 2) {
            for (int i = startLine; i <= endLine; i++) {
                if (chessBoard.getBoard()[i][startColumn] != null
                        && !this.equals(chessBoard.getBoard()[i][startColumn])) {
                    return false;
                }
            }
            return true;
        }

        // Если ход не первый или пешка атакует, то можно уйти только на одну клетку вперед
        if (Math.abs(endLine - startLine) == 1) {
            if (chessBoard.getBoard()[endLine][endColumn] != null) {
                return false;
            }
            return true;
        }

        return false;
    }

    // У пешки атака отличается от обычного хода
    @Override
    public boolean canAttackToPosition(ChessBoard chessBoard, int startLine, int startColumn, int endLine, int endColumn) {
        // Начальная и конечная точки совпадают или какая-то из точек за пределами доски
        if (!super.canMoveToPosition(chessBoard, startLine, startColumn, endLine, endColumn)) {
            return false;
        }

        if (WHITE.equals(getColor()) && Math.abs(startColumn - endColumn) == 1 && endLine - startLine == 1) {
            return chessBoard.getChessPieceByPosition(endLine, endColumn) != null
                    && !getColor().equals(chessBoard.getChessPieceByPosition(endLine, endColumn).getColor());
        }

        if (BLACK.equals(getColor()) && Math.abs(startColumn - endColumn) == 1 && startLine - endLine == 1) {
            return chessBoard.getChessPieceByPosition(endLine, endColumn) != null
                    && !getColor().equals(chessBoard.getChessPieceByPosition(endLine, endColumn).getColor());
        }

        return false;
    }
}
