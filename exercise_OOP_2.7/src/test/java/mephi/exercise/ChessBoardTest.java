package mephi.exercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static mephi.exercise.ColorPlayer.BLACK;
import static mephi.exercise.ColorPlayer.WHITE;
import static org.junit.jupiter.api.Assertions.*;

class ChessBoardTest {

    private ChessBoard board = new ChessBoard();

    @BeforeEach
    void init() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.getBoard()[i][j] = null;
            }
        }
    }

    @Test
    void testCastling0White() {
        board.getBoard()[0][0] = new Rook(WHITE);
        board.getBoard()[0][1] = null;
        board.getBoard()[0][2] = null;
        board.getBoard()[0][3] = null;
        board.getBoard()[0][4] = new King(WHITE);
        board.getBoard()[0][5] = new Bishop(WHITE);
        board.getBoard()[0][6] = new Horse(WHITE);
        board.getBoard()[0][7] = new Rook(WHITE);
        board.getBoard()[1][0] = new Pawn(WHITE);
        board.getBoard()[1][1] = new Pawn(WHITE);
        board.getBoard()[1][2] = new Pawn(WHITE);
        board.getBoard()[1][3] = new Pawn(WHITE);
        board.getBoard()[1][4] = new Pawn(WHITE);
        board.getBoard()[1][5] = new Pawn(WHITE);
        board.getBoard()[1][6] = new Pawn(WHITE);
        board.getBoard()[1][7] = new Pawn(WHITE);

        board.getBoard()[7][0] = new Rook(BLACK);
        board.getBoard()[7][1] = new Horse(BLACK);
        board.getBoard()[7][2] = new Bishop(BLACK);
        board.getBoard()[7][3] = new Queen(BLACK);
        board.getBoard()[7][4] = new King(BLACK);
        board.getBoard()[7][5] = new Bishop(BLACK);
        board.getBoard()[7][6] = new Horse(BLACK);
        board.getBoard()[7][7] = new Rook(BLACK);
        board.getBoard()[6][0] = new Pawn(BLACK);
        board.getBoard()[6][1] = new Pawn(BLACK);
        board.getBoard()[6][2] = new Pawn(BLACK);
        board.getBoard()[6][3] = new Pawn(BLACK);
        board.getBoard()[6][4] = new Pawn(BLACK);
        board.getBoard()[6][5] = new Pawn(BLACK);
        board.getBoard()[6][6] = new Pawn(BLACK);
        board.getBoard()[6][7] = new Pawn(BLACK);

        assertTrue(board.castling0());
    }

    @Test
    void testCastling0Black() {
        board.getBoard()[0][0] = new Rook(WHITE);
        board.getBoard()[0][1] = new Horse(WHITE);
        board.getBoard()[0][2] = new Bishop(WHITE);
        board.getBoard()[0][3] = new Queen(WHITE);
        board.getBoard()[0][4] = new King(WHITE);
        board.getBoard()[0][5] = new Bishop(WHITE);
        board.getBoard()[0][6] = new Horse(WHITE);
        board.getBoard()[0][7] = new Rook(WHITE);
        board.getBoard()[1][0] = new Pawn(WHITE);
        board.getBoard()[1][1] = new Pawn(WHITE);
        board.getBoard()[1][2] = new Pawn(WHITE);
        board.getBoard()[1][3] = new Pawn(WHITE);
        board.getBoard()[1][4] = new Pawn(WHITE);
        board.getBoard()[1][5] = new Pawn(WHITE);
        board.getBoard()[1][6] = new Pawn(WHITE);
        board.getBoard()[1][7] = new Pawn(WHITE);

        board.getBoard()[7][0] = new Rook(BLACK);
        board.getBoard()[7][1] = null;
        board.getBoard()[7][2] = null;
        board.getBoard()[7][3] = null;
        board.getBoard()[7][4] = new King(BLACK);
        board.getBoard()[7][5] = new Bishop(BLACK);
        board.getBoard()[7][6] = new Horse(BLACK);
        board.getBoard()[7][7] = new Rook(BLACK);
        board.getBoard()[6][0] = new Pawn(BLACK);
        board.getBoard()[6][1] = new Pawn(BLACK);
        board.getBoard()[6][2] = new Pawn(BLACK);
        board.getBoard()[6][3] = new Pawn(BLACK);
        board.getBoard()[6][4] = new Pawn(BLACK);
        board.getBoard()[6][5] = new Pawn(BLACK);
        board.getBoard()[6][6] = new Pawn(BLACK);
        board.getBoard()[6][7] = new Pawn(BLACK);

        board.moveToPosition(1, 1, 2, 1);

        assertTrue(board.castling0());
    }

    @Test
    void testNoCastling0White() {
        board.getBoard()[0][0] = new Rook(WHITE);
        board.getBoard()[0][1] = new Horse(WHITE);
        board.getBoard()[0][2] = new Bishop(WHITE);
        board.getBoard()[0][3] = new Queen(WHITE);
        board.getBoard()[0][4] = new King(WHITE);
        board.getBoard()[0][5] = new Bishop(WHITE);
        board.getBoard()[0][6] = new Horse(WHITE);
        board.getBoard()[0][7] = new Rook(WHITE);
        board.getBoard()[1][0] = new Pawn(WHITE);
        board.getBoard()[1][1] = new Pawn(WHITE);
        board.getBoard()[1][2] = new Pawn(WHITE);
        board.getBoard()[1][3] = new Pawn(WHITE);
        board.getBoard()[1][4] = new Pawn(WHITE);
        board.getBoard()[1][5] = new Pawn(WHITE);
        board.getBoard()[1][6] = new Pawn(WHITE);
        board.getBoard()[1][7] = new Pawn(WHITE);

        board.getBoard()[7][0] = new Rook(BLACK);
        board.getBoard()[7][1] = new Horse(BLACK);
        board.getBoard()[7][2] = new Bishop(BLACK);
        board.getBoard()[7][3] = new Queen(BLACK);
        board.getBoard()[7][4] = new King(BLACK);
        board.getBoard()[7][5] = new Bishop(BLACK);
        board.getBoard()[7][6] = new Horse(BLACK);
        board.getBoard()[7][7] = new Rook(BLACK);
        board.getBoard()[6][0] = new Pawn(BLACK);
        board.getBoard()[6][1] = new Pawn(BLACK);
        board.getBoard()[6][2] = new Pawn(BLACK);
        board.getBoard()[6][3] = new Pawn(BLACK);
        board.getBoard()[6][4] = new Pawn(BLACK);
        board.getBoard()[6][5] = new Pawn(BLACK);
        board.getBoard()[6][6] = new Pawn(BLACK);
        board.getBoard()[6][7] = new Pawn(BLACK);

        assertFalse(board.castling0());
    }

    @Test
    void testNoCastling0Black() {
        board.getBoard()[0][0] = new Rook(WHITE);
        board.getBoard()[0][1] = new Horse(WHITE);
        board.getBoard()[0][2] = new Bishop(WHITE);
        board.getBoard()[0][3] = new Queen(WHITE);
        board.getBoard()[0][4] = new King(WHITE);
        board.getBoard()[0][5] = new Bishop(WHITE);
        board.getBoard()[0][6] = new Horse(WHITE);
        board.getBoard()[0][7] = new Rook(WHITE);
        board.getBoard()[1][0] = new Pawn(WHITE);
        board.getBoard()[1][1] = new Pawn(WHITE);
        board.getBoard()[1][2] = new Pawn(WHITE);
        board.getBoard()[1][3] = new Pawn(WHITE);
        board.getBoard()[1][4] = new Pawn(WHITE);
        board.getBoard()[1][5] = new Pawn(WHITE);
        board.getBoard()[1][6] = new Pawn(WHITE);
        board.getBoard()[1][7] = new Pawn(WHITE);

        board.getBoard()[7][0] = new Rook(BLACK);
        board.getBoard()[7][1] = new Horse(BLACK);
        board.getBoard()[7][2] = new Bishop(BLACK);
        board.getBoard()[7][3] = new Queen(BLACK);
        board.getBoard()[7][4] = new King(BLACK);
        board.getBoard()[7][5] = new Bishop(BLACK);
        board.getBoard()[7][6] = new Horse(BLACK);
        board.getBoard()[7][7] = new Rook(BLACK);
        board.getBoard()[6][0] = new Pawn(BLACK);
        board.getBoard()[6][1] = new Pawn(BLACK);
        board.getBoard()[6][2] = new Pawn(BLACK);
        board.getBoard()[6][3] = new Pawn(BLACK);
        board.getBoard()[6][4] = new Pawn(BLACK);
        board.getBoard()[6][5] = new Pawn(BLACK);
        board.getBoard()[6][6] = new Pawn(BLACK);
        board.getBoard()[6][7] = new Pawn(BLACK);

        board.moveToPosition(1, 1, 2, 1);

        assertFalse(board.castling0());
    }

    @Test
    void testCastling7White() {
        board.getBoard()[0][0] = new Rook(WHITE);
        board.getBoard()[0][1] = new Horse(WHITE);
        board.getBoard()[0][2] = new Bishop(WHITE);
        board.getBoard()[0][3] = new Queen(BLACK);
        board.getBoard()[0][4] = new King(WHITE);
        board.getBoard()[0][5] = null;
        board.getBoard()[0][6] = null;
        board.getBoard()[0][7] = new Rook(WHITE);
        board.getBoard()[1][0] = new Pawn(WHITE);
        board.getBoard()[1][1] = new Pawn(WHITE);
        board.getBoard()[1][2] = new Pawn(WHITE);
        board.getBoard()[1][3] = new Pawn(WHITE);
        board.getBoard()[1][4] = new Pawn(WHITE);
        board.getBoard()[1][5] = new Pawn(WHITE);
        board.getBoard()[1][6] = new Pawn(WHITE);
        board.getBoard()[1][7] = new Pawn(WHITE);

        board.getBoard()[7][0] = new Rook(BLACK);
        board.getBoard()[7][1] = new Horse(BLACK);
        board.getBoard()[7][2] = new Bishop(BLACK);
        board.getBoard()[7][3] = new Queen(BLACK);
        board.getBoard()[7][4] = new King(BLACK);
        board.getBoard()[7][5] = new Bishop(BLACK);
        board.getBoard()[7][6] = new Horse(BLACK);
        board.getBoard()[7][7] = new Rook(BLACK);
        board.getBoard()[6][0] = new Pawn(BLACK);
        board.getBoard()[6][1] = new Pawn(BLACK);
        board.getBoard()[6][2] = new Pawn(BLACK);
        board.getBoard()[6][3] = new Pawn(BLACK);
        board.getBoard()[6][4] = new Pawn(BLACK);
        board.getBoard()[6][5] = new Pawn(BLACK);
        board.getBoard()[6][6] = new Pawn(BLACK);
        board.getBoard()[6][7] = new Pawn(BLACK);

        assertTrue(board.castling7());
    }

    @Test
    void testCastling7Black() {
        board.getBoard()[0][0] = new Rook(WHITE);
        board.getBoard()[0][1] = new Horse(WHITE);
        board.getBoard()[0][2] = new Bishop(WHITE);
        board.getBoard()[0][3] = new Queen(WHITE);
        board.getBoard()[0][4] = new King(WHITE);
        board.getBoard()[0][5] = new Bishop(WHITE);
        board.getBoard()[0][6] = new Horse(WHITE);
        board.getBoard()[0][7] = new Rook(WHITE);
        board.getBoard()[1][0] = new Pawn(WHITE);
        board.getBoard()[1][1] = new Pawn(WHITE);
        board.getBoard()[1][2] = new Pawn(WHITE);
        board.getBoard()[1][3] = new Pawn(WHITE);
        board.getBoard()[1][4] = new Pawn(WHITE);
        board.getBoard()[1][5] = new Pawn(WHITE);
        board.getBoard()[1][6] = new Pawn(WHITE);
        board.getBoard()[1][7] = new Pawn(WHITE);

        board.getBoard()[7][0] = new Rook(BLACK);
        board.getBoard()[7][1] = new Horse(BLACK);
        board.getBoard()[7][2] = new Bishop(BLACK);
        board.getBoard()[7][3] = new Queen(WHITE);
        board.getBoard()[7][4] = new King(BLACK);
        board.getBoard()[7][5] = null;
        board.getBoard()[7][6] = null;
        board.getBoard()[7][7] = new Rook(BLACK);
        board.getBoard()[6][0] = new Pawn(BLACK);
        board.getBoard()[6][1] = new Pawn(BLACK);
        board.getBoard()[6][2] = new Pawn(BLACK);
        board.getBoard()[6][3] = new Pawn(BLACK);
        board.getBoard()[6][4] = new Pawn(BLACK);
        board.getBoard()[6][5] = new Pawn(BLACK);
        board.getBoard()[6][6] = new Pawn(BLACK);
        board.getBoard()[6][7] = new Pawn(BLACK);

        board.moveToPosition(1, 1, 2, 1);

        assertTrue(board.castling7());
    }

    @Test
    void testNoCastling7White() {
        board.getBoard()[0][0] = new Rook(WHITE);
        board.getBoard()[0][1] = new Horse(WHITE);
        board.getBoard()[0][2] = new Bishop(WHITE);
        board.getBoard()[0][3] = new Queen(WHITE);
        board.getBoard()[0][4] = new King(WHITE);
        board.getBoard()[0][5] = new Bishop(WHITE);
        board.getBoard()[0][6] = new Horse(WHITE);
        board.getBoard()[0][7] = new Rook(WHITE);
        board.getBoard()[1][0] = new Pawn(WHITE);
        board.getBoard()[1][1] = new Pawn(WHITE);
        board.getBoard()[1][2] = new Pawn(WHITE);
        board.getBoard()[1][3] = new Pawn(WHITE);
        board.getBoard()[1][4] = new Pawn(WHITE);
        board.getBoard()[1][5] = new Pawn(WHITE);
        board.getBoard()[1][6] = new Pawn(WHITE);
        board.getBoard()[1][7] = new Pawn(WHITE);

        board.getBoard()[7][0] = new Rook(BLACK);
        board.getBoard()[7][1] = new Horse(BLACK);
        board.getBoard()[7][2] = new Bishop(BLACK);
        board.getBoard()[7][3] = new Queen(BLACK);
        board.getBoard()[7][4] = new King(BLACK);
        board.getBoard()[7][5] = new Bishop(BLACK);
        board.getBoard()[7][6] = new Horse(BLACK);
        board.getBoard()[7][7] = new Rook(BLACK);
        board.getBoard()[6][0] = new Pawn(BLACK);
        board.getBoard()[6][1] = new Pawn(BLACK);
        board.getBoard()[6][2] = new Pawn(BLACK);
        board.getBoard()[6][3] = new Pawn(BLACK);
        board.getBoard()[6][4] = new Pawn(BLACK);
        board.getBoard()[6][5] = new Pawn(BLACK);
        board.getBoard()[6][6] = new Pawn(BLACK);
        board.getBoard()[6][7] = new Pawn(BLACK);

        assertFalse(board.castling7());
    }

    @Test
    void testNoCastling7Black() {
        board.getBoard()[0][0] = new Rook(WHITE);
        board.getBoard()[0][1] = new Horse(WHITE);
        board.getBoard()[0][2] = new Bishop(WHITE);
        board.getBoard()[0][3] = new Queen(WHITE);
        board.getBoard()[0][4] = new King(WHITE);
        board.getBoard()[0][5] = new Bishop(WHITE);
        board.getBoard()[0][6] = new Horse(WHITE);
        board.getBoard()[0][7] = new Rook(WHITE);
        board.getBoard()[1][0] = new Pawn(WHITE);
        board.getBoard()[1][1] = new Pawn(WHITE);
        board.getBoard()[1][2] = new Pawn(WHITE);
        board.getBoard()[1][3] = new Pawn(WHITE);
        board.getBoard()[1][4] = new Pawn(WHITE);
        board.getBoard()[1][5] = new Pawn(WHITE);
        board.getBoard()[1][6] = new Pawn(WHITE);
        board.getBoard()[1][7] = new Pawn(WHITE);

        board.getBoard()[7][0] = new Rook(BLACK);
        board.getBoard()[7][1] = new Horse(BLACK);
        board.getBoard()[7][2] = new Bishop(BLACK);
        board.getBoard()[7][3] = new Queen(BLACK);
        board.getBoard()[7][4] = new King(BLACK);
        board.getBoard()[7][5] = new Bishop(BLACK);
        board.getBoard()[7][6] = new Horse(BLACK);
        board.getBoard()[7][7] = new Rook(BLACK);
        board.getBoard()[6][0] = new Pawn(BLACK);
        board.getBoard()[6][1] = new Pawn(BLACK);
        board.getBoard()[6][2] = new Pawn(BLACK);
        board.getBoard()[6][3] = new Pawn(BLACK);
        board.getBoard()[6][4] = new Pawn(BLACK);
        board.getBoard()[6][5] = new Pawn(BLACK);
        board.getBoard()[6][6] = new Pawn(BLACK);
        board.getBoard()[6][7] = new Pawn(BLACK);

        board.moveToPosition(1, 1, 2, 1);

        assertFalse(board.castling7());
    }

}