package mephi.exercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static mephi.exercise.ColorPlayer.BLACK;
import static mephi.exercise.ColorPlayer.WHITE;
import static org.junit.jupiter.api.Assertions.*;

class PawnTest {

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
    @CsvSource({"1,0,1,0,false", "1,0,1,10,false", "1,0,2,0,false", "1,0,3,0,false", "1,0,2,1,false"})
    void moveTest1(int startLine, int startColumn, int endLine, int endColumn, boolean canMove) {
        board.getBoard()[0][0] = new Pawn(WHITE);
        board.getBoard()[1][0] = new Pawn(WHITE);
        board.getBoard()[2][0] = new Pawn(WHITE);
        board.getBoard()[0][1] = new Pawn(WHITE);
        board.getBoard()[1][1] = new Pawn(WHITE);
        board.getBoard()[2][1] = new Pawn(WHITE);

        assertEquals(canMove, board.moveToPosition(startLine, startColumn, endLine, endColumn));
    }

    @ParameterizedTest
    @CsvSource({"1,3,1,3,false", "1,3,10,3,false", "1,3,2,3,true", "1,3,3,3,true", "1,3,2,2,false"})
    void moveTest2(int startLine, int startColumn, int endLine, int endColumn, boolean canMove) {
        board.getBoard()[1][3] = new Pawn(WHITE);
        board.getBoard()[1][4] = new Pawn(WHITE);

        assertEquals(canMove, board.moveToPosition(startLine, startColumn, endLine, endColumn));
    }

    @ParameterizedTest
    @CsvSource({"1,0,1,0,false", "1,0,1,10,false", "1,0,2,0,false", "1,0,3,0,false", "1,0,2,1,false"})
    void attackTest1(int startLine, int startColumn, int endLine, int endColumn, boolean canAttack) {
        board.getBoard()[0][0] = new Pawn(WHITE);
        board.getBoard()[1][0] = new Pawn(WHITE);
        board.getBoard()[2][0] = new Pawn(WHITE);
        board.getBoard()[0][1] = new Pawn(WHITE);
        board.getBoard()[1][1] = new Pawn(WHITE);
        board.getBoard()[2][1] = new Pawn(WHITE);

        assertEquals(canAttack, board.attackToPosition(startLine, startColumn, endLine, endColumn));
    }

    @ParameterizedTest
    @CsvSource({"1,3,1,3,false", "1,3,2,4,false", "1,3,2,2,true", "1,3,2,3,false", "1,7,2,6,false"})
    void attackTest2(int startLine, int startColumn, int endLine, int endColumn, boolean canMove) {
        board.getBoard()[1][3] = new Pawn(WHITE);
        board.getBoard()[2][4] = new Pawn(WHITE);
        board.getBoard()[2][2] = new Pawn(BLACK);
        board.getBoard()[1][7] = new Pawn(WHITE);

        assertEquals(canMove, board.attackToPosition(startLine, startColumn, endLine, endColumn));
    }

    @ParameterizedTest
    @CsvSource({"6,3,6,3,false", "6,3,5,4,true", "6,3,5,2,false", "6,3,5,3,false"})
    void attackTest3(int startLine, int startColumn, int endLine, int endColumn, boolean canMove) {
        board.getBoard()[6][3] = new Pawn(BLACK);
        board.getBoard()[5][4] = new Pawn(WHITE);
        board.getBoard()[5][2] = new Pawn(BLACK);
        board.getBoard()[1][1] = new Pawn(WHITE);
        board.moveToPosition(1, 1, 2, 1);

        assertEquals(canMove, board.attackToPosition(startLine, startColumn, endLine, endColumn));
    }

}