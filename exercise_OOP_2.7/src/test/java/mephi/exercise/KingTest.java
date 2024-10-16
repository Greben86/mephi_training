package mephi.exercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static mephi.exercise.ColorPlayer.BLACK;
import static mephi.exercise.ColorPlayer.WHITE;
import static org.junit.jupiter.api.Assertions.*;

class KingTest {

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
    @CsvSource({"0,0,0,0,false", "10,0,0,0,false", "0,0,1,0,false", "0,0,0,1,false", "0,0,1,1,false"})
    void moveTest1(int startLine, int startColumn, int endLine, int endColumn, boolean canMove) {
        board.getBoard()[0][0] = new King(WHITE);
        board.getBoard()[0][1] = new King(WHITE);
        board.getBoard()[1][0] = new King(WHITE);
        board.getBoard()[1][1] = new King(WHITE);

        assertEquals(canMove, board.moveToPosition(startLine, startColumn, endLine, endColumn));
    }

    @ParameterizedTest
    @CsvSource({"0,0,0,0,false", "10,0,0,0,false", "0,0,0,1,true", "0,0,1,1,false", "0,0,1,1,false", "0,0,2,0,false"})
    void moveTest2(int startLine, int startColumn, int endLine, int endColumn, boolean canMove) {
        board.getBoard()[0][0] = new King(WHITE);
        board.getBoard()[1][1] = new King(WHITE);
        board.getBoard()[0][1] = new King(BLACK);

        assertEquals(canMove, board.moveToPosition(startLine, startColumn, endLine, endColumn));
    }

    @ParameterizedTest
    @CsvSource({"0,0,0,0,false", "10,0,0,0,false", "0,0,1,0,false", "0,0,0,1,false", "0,0,1,1,false"})
    void attackTest1(int startLine, int startColumn, int endLine, int endColumn, boolean canAttack) {
        board.getBoard()[0][0] = new King(WHITE);
        board.getBoard()[0][1] = new King(WHITE);
        board.getBoard()[1][0] = new King(WHITE);
        board.getBoard()[1][1] = new King(WHITE);

        assertEquals(canAttack, board.attackToPosition(startLine, startColumn, endLine, endColumn));
    }

    @ParameterizedTest
    @CsvSource({"0,0,0,0,false", "10,0,0,0,false", "0,0,0,1,true", "0,0,1,1,false", "0,0,1,1,false"})
    void attackTest2(int startLine, int startColumn, int endLine, int endColumn, boolean canMove) {
        board.getBoard()[0][0] = new King(WHITE);
        board.getBoard()[1][1] = new King(WHITE);
        board.getBoard()[0][1] = new King(BLACK);

        assertEquals(canMove, board.attackToPosition(startLine, startColumn, endLine, endColumn));
    }

}