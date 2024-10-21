package mephi.exercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static mephi.exercise.ColorPlayer.BLACK;
import static mephi.exercise.ColorPlayer.WHITE;
import static org.junit.jupiter.api.Assertions.*;

class HorseTest {

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
    @CsvSource({"7,7,5,8,false", "0,0,0,0,false", "0,0,5,5,false", "0,0,2,1,true", "0,0,1,2,true", "7,7,5,6,true", "7,7,6,5,true"})
    void moveTest1(int startLine, int startColumn, int endLine, int endColumn, boolean canMove) {
        board.getBoard()[0][0] = new Horse(WHITE);
        board.getBoard()[0][1] = new Horse(WHITE);
        board.getBoard()[1][0] = new Horse(WHITE);
        board.getBoard()[1][1] = new Horse(WHITE);
        board.getBoard()[7][7] = new Horse(WHITE);

        assertEquals(canMove, board.moveToPosition(startLine, startColumn, endLine, endColumn));
    }

    @ParameterizedTest
    @CsvSource({"0,0,2,1,true", "0,0,1,2,true", "0,0,5,5,false", "0,0,1,1,false", "7,7,5,6,true", "7,7,6,5,true"})
    void attackTest1(int startLine, int startColumn, int endLine, int endColumn, boolean canAttack) {
        board.getBoard()[0][0] = new Horse(WHITE);
        board.getBoard()[2][1] = new Horse(BLACK);
        board.getBoard()[1][2] = new Horse(BLACK);
        board.getBoard()[7][7] = new Horse(WHITE);
        board.getBoard()[5][6] = new Horse(BLACK);
        board.getBoard()[6][5] = new Horse(BLACK);
        board.getBoard()[5][5] = new Horse(BLACK);

        assertEquals(canAttack, board.attackToPosition(startLine, startColumn, endLine, endColumn));
    }
}