package mephi.exercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static mephi.exercise.ColorPlayer.BLACK;
import static mephi.exercise.ColorPlayer.WHITE;
import static org.junit.jupiter.api.Assertions.*;

class QueenTest {

    private ChessBoard board = new ChessBoard();

    @BeforeEach
    void init() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.getBoard()[i][j] = null;
            }
        }
    }

    @ParameterizedTest
    @CsvSource({"0,0,0,0,false", "10,0,0,0,false", "0,0,7,0,false", "0,0,0,7,false", "0,0,5,5,false"})
    void moveTest1(int startLine, int startColumn, int endLine, int endColumn, boolean canMove) {
        board.getBoard()[0][0] = new Queen(WHITE);
        board.getBoard()[0][1] = new Queen(WHITE);
        board.getBoard()[1][0] = new Queen(WHITE);
        board.getBoard()[1][1] = new Queen(WHITE);

        assertEquals(canMove, board.moveToPosition(startLine, startColumn, endLine, endColumn));
    }

    @ParameterizedTest
    @CsvSource({"5,5,0,0,true", "10,0,0,0,false", "5,5,7,7,true", "5,5,0,7,false", "5,5,5,5,false",
            "0,0,0,0,false", "10,0,0,0,false", "0,0,7,0,true", "0,0,0,7,true", "0,0,5,5,true"})
    void moveTest2(int startLine, int startColumn, int endLine, int endColumn, boolean canMove) {
        board.getBoard()[0][0] = new Queen(WHITE);
        board.getBoard()[5][5] = new Queen(WHITE);
        board.getBoard()[7][7] = new Queen(WHITE);

        assertEquals(canMove, board.moveToPosition(startLine, startColumn, endLine, endColumn));
    }

    @ParameterizedTest
    @CsvSource({"0,0,0,0,false", "10,0,0,0,false", "0,0,7,0,false", "0,0,0,7,false", "0,0,5,5,false"})
    void attackTest1(int startLine, int startColumn, int endLine, int endColumn, boolean canAttack) {
        board.getBoard()[0][0] = new Queen(WHITE);
        board.getBoard()[0][1] = new Queen(WHITE);
        board.getBoard()[1][0] = new Queen(WHITE);
        board.getBoard()[1][1] = new Queen(WHITE);

        assertEquals(canAttack, board.attackToPosition(startLine, startColumn, endLine, endColumn));
    }

    @ParameterizedTest
    @CsvSource({"5,5,0,0,false", "10,0,0,0,false", "5,5,7,7,true", "5,5,0,7,false", "5,5,5,5,false",
            "0,0,0,0,false", "10,0,0,0,false", "0,0,7,7,true", "0,0,0,7,false", "0,0,5,5,false"})
    void attackTest2(int startLine, int startColumn, int endLine, int endColumn, boolean canMove) {
        board.getBoard()[0][0] = new Queen(WHITE);
        board.getBoard()[5][5] = new Queen(WHITE);
        board.getBoard()[7][7] = new Queen(BLACK);

        assertEquals(canMove, board.attackToPosition(startLine, startColumn, endLine, endColumn));
    }

}