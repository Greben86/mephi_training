package mephi.exercise;

public class ChessBoard {

    protected static final int MIN_VALUE_OF_COORDINATE = 0;
    protected static final int MAX_VALUE_OF_COORDINATE = 7;

    public ChessPiece[][] board = new ChessPiece[8][8]; // creating a field for game
    String nowPlayer;

    public ChessBoard(String nowPlayer) {
        this.nowPlayer = nowPlayer;
    }

    public String nowPlayerColor() {
        return this.nowPlayer;
    }

    public boolean moveToPosition(int startLine, int startColumn, int endLine, int endColumn) {
        if (checkPos(startLine) && checkPos(startColumn)) {

            if (!nowPlayer.equals(board[startLine][startColumn].getColor())) {
                return false;
            }

            if (board[startLine][startColumn].canMoveToPosition(this, startLine, startColumn, endLine, endColumn)) {
                board[endLine][endColumn] = board[startLine][startColumn]; // if piece can move, we moved a piece
                board[endLine][endColumn].setCheck(false);
                board[startLine][startColumn] = null; // set null to previous cell
                this.nowPlayer = "White".equals(this.nowPlayerColor()) ? "Black" : "White";

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void printBoard() {  //print board in console
        System.out.println("Turn " + nowPlayer);
        System.out.println();
        System.out.println("Player 2(Black)");
        System.out.println();
        System.out.println("\t0\t1\t2\t3\t4\t5\t6\t7");

        for (int i = 7; i > -1; i--) {
            System.out.print(i + "\t");
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) {
                    System.out.print(".." + "\t");
                } else {
                    System.out.print(board[i][j].getSymbol() + board[i][j].getColor().substring(0, 1).toLowerCase() + "\t");
                }
            }
            System.out.println();
            System.out.println();
        }
        System.out.println("Player 1(White)");
    }

    public boolean checkPos(int pos) {
        return pos >= MIN_VALUE_OF_COORDINATE && pos <= MAX_VALUE_OF_COORDINATE;
    }

    public ChessPiece getChessPieceByPosition(int line, int column) {
        if (checkPos(line) && checkPos(column)) {
            return board[line][column];
        }

        return null;
    }

    public boolean isUnderAttack(int kingLine, int kingColumn) {
        for (int indexLine = 0; indexLine < board.length; indexLine++) {
            for (int indexColumn = 0; indexColumn < board[indexLine].length; indexColumn++) {
                ChessPiece chessPiece = getChessPieceByPosition(indexLine, indexColumn);
                if (chessPiece != null && chessPiece.canMoveToPosition(this, indexLine, indexColumn, kingLine, kingColumn)) {
                    // Шах!
                    return true;
                }
            }
        }

        return false;
    }

    // Длинная рокировка
    public boolean castling0() {
        if ("White".equals(nowPlayer)) {
            ChessPiece rook = board[0][0];
            ChessPiece king = board[0][4];

            if (rook == null || king == null) {
                return false;
            }
            if ("R".equals(rook.getSymbol()) && "K".equals(king.getSymbol())
                    && "White".equals(rook.getColor()) && "White".equals(king.getColor())
                    && rook.isCheck() && king.isCheck()) {
                if (isUnderAttack(0, 2)) {
                    return false;
                }

                for (int i = 1; i < 4; i++) {
                    if (board[0][i] != null) {
                        return false;
                    }
                }

                board[0][0] = null;
                board[0][2] = king;
                board[0][3] = rook;
                board[0][4] = null;
                king.setCheck(false);
                rook.setCheck(false);
                this.nowPlayer = "Black";

                return true;
            }
        } else {
            ChessPiece rook = board[7][0];
            ChessPiece king = board[7][4];

            if (rook == null || king == null) {
                return false;
            }
            if ("R".equals(rook.getSymbol()) && "K".equals(king.getSymbol())
                    && "Black".equals(rook.getColor()) && "Black".equals(king.getColor())
                    && rook.isCheck() && king.isCheck()) {
                if (isUnderAttack(7, 2)) {
                    return false;
                }

                for (int i = 1; i < 4; i++) {
                    if (board[7][i] != null) {
                        return false;
                    }
                }

                board[7][0] = null;
                board[7][2] = king;
                board[7][3] = rook;
                board[7][4] = null;
                king.setCheck(false);
                rook.setCheck(false);
                this.nowPlayer = "White";

                return true;
            }
        }
        return false;
    }

    // Короткая рокировка
    public boolean castling7() {
        if ("White".equals(nowPlayer)) {
            ChessPiece rook = board[0][7];
            ChessPiece king = board[0][4];

            if (rook == null || king == null) {
                return false;
            }
            if ("R".equals(rook.getSymbol()) && "K".equals(king.getSymbol())
                    && "White".equals(rook.getColor()) && "White".equals(king.getColor())
                    && rook.isCheck() && king.isCheck()) {
                if (isUnderAttack(0, 6)) {
                    return false;
                }

                for (int i = 5; i < 6; i++) {
                    if (board[0][i] != null) {
                        return false;
                    }
                }

                board[0][4] = null;
                board[0][5] = rook;
                board[0][6] = king;
                board[0][7] = null;
                king.setCheck(false);
                rook.setCheck(false);
                this.nowPlayer = "Black";

                return true;
            }
        } else {
            ChessPiece rook = board[7][7];
            ChessPiece king = board[7][4];

            if (rook == null || king == null) {
                return false;
            }
            if ("R".equals(rook.getSymbol()) && "K".equals(king.getSymbol())
                    && "White".equals(rook.getColor()) && "White".equals(king.getColor())
                    && rook.isCheck() && king.isCheck()) {
                if (isUnderAttack(7, 6)) {
                    return false;
                }

                for (int i = 5; i < 6; i++) {
                    if (board[7][i] != null) {
                        return false;
                    }
                }

                board[7][4] = null;
                board[7][5] = rook;
                board[7][6] = king;
                board[7][7] = null;
                king.setCheck(false);
                rook.setCheck(false);
                this.nowPlayer = "White";

                return true;
            }
        }
        return false;
    }
}
