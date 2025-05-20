package omok;

import java.util.Arrays;

//class Board {
//    int size;
//    String[][] map;
//    char c = 65;
//
//    Board(int size) {
//        this.size = size;
//        map = new String[size][size];
//        for (int row = 0; row < size; row++) {
//            for (int col = 0; col < size; col++) {
//                map[row][col] = ".";
//            }
//        }
//    }
//    
//    // x -> 영문, y -> 숫자
//    public void print() {
//    	c = 65;
//        for (int row = 0; row < size + 1; row++) {
//        	if(row == size) {
//        		System.out.print("  ");
//        		for (int col = 0; col < size; col++) {   
//                    System.out.print(" " + c);
//                    c++;
//                }
//        	}
//        	else {
//        		for (int col = 0; col < size + 1; col++) {   
//        			if(col == 0) {
//        				if(row < 10) {
//        					System.out.print(row + " ");
//        				}
//        				else {
//        					System.out.print(row);
//        				}
//        			}else {
//        				System.out.print(" " + map[row][col-1]);
//        			}
//                }
//        	}
//            System.out.println();
//        }
//    }
//
//    // 유효성 검사
//    // 행 & 열 모두 인덱스가 음수는 아닌지 : >= 0, 보드 범위 내인지 : size
//    // 놓으려는 자리가 비어있는지 -> map
//    public boolean isValidMove(int row, int col) {
//        return row >= 0 && row < size && col >= 0 && col < size && map[row][col].equals(".");
//    }
//    
//    // 돌 놓기
//    public void placeStone(int row, int col, String stone) {
//        map[row][col] = stone;
//    }
//    
//    // 승리 판별
//    public boolean checkWin(int row, int col, String stone) {
//        return checkDirection(row, col, stone, 1, 0) // 가로 (좌우 방향)
//            || checkDirection(row, col, stone, 0, 1) // 세로 (위아래 방향)
//            || checkDirection(row, col, stone, 1, 1) // 대각  (↖ ↘ 방향 검사)
//            || checkDirection(row, col, stone, 1, -1); // 대각 (↙ ↗ 방향 검사)
//    }
//    
//    public int checkValid(int row, int col, String stone) {
//        int[] directions = {
//            checkDirection2(row, col, stone, 1, 0),   // width
//            checkDirection2(row, col, stone, 0, 1),   // height
//            checkDirection2(row, col, stone, 1, -1),  // diagonalR
//            checkDirection2(row, col, stone, 1, 1)    // diagonalL
//        };
//
//        int max = 0;
//        int check = 0;
//
//
//        for (int count : directions) {
//            if (count % 3 == 0 || count % 4 == 0) {
//                check += count;
//            }
//            if (count > max) {
//                max = count;
//            }
//        }
//        if(check == 0) return max;
//        
//        if (check % 6 == 0) {
//            max = 33;
//        }
//        if (check % 8 == 0) {
//        	max = 44;
//        }
//
//        return max;
//    }
//    
//    
//    private int checkDirection2(int row, int col, String stone, int dRow, int dCol) {
//        int count = 1;
//
//        // 한쪽 방향 체크
//        count += countStones(row, col, stone, dRow, dCol);
//        // 반대 방향 체크
//        count += countStones(row, col, stone, -dRow, -dCol);
//        
//        return count;
//
//    }
//    
//    private boolean checkDirection(int row, int col, String stone, int dRow, int dCol) {
//        int count = 1; // 현재 놓인 돌 포함
//
//        // 한쪽 방향 체크 (오른쪽)
//        count += countStones(row, col, stone, dRow, dCol);
//        // 반대 방향 체크 (왼쪽)
//        count += countStones(row, col, stone, -dRow, -dCol);
//
//        return count == 5 ? true : false;
//    }
//    
//    // 돌 개수 세기
//    private int countStones(int row, int col, String stone, int dRow, int dCol) {
//    	// 찾은 돌의 개수
//        int count = 0;
//        // 검사 시작 행
//        int currRow = row + dRow;
//        // 검사 시작 열
//        int currCol = col + dCol;
//        
//        // 보드 범위 안에 있는지, 현재 위치에 같은 돌이 있는지 확인
//        while (currRow >= 0 && currRow < size && currCol >= 0 && currCol < size && map[currRow][currCol].equals(stone)) {
//            count++; // 같은(O,X) 돌이면 count 증가
//            currRow += dRow; // 다음 칸으로 이동 (행)
//            currCol += dCol; // 다음 칸으로 이동 (열)
//        }
//
//        return count;
//    }
//}

class Board {
    String[][] map;
    int size;
    int lastRow = -1, lastCol = -1;

    public Board(int size) {
        this.size = size;
        map = new String[size][size];
        for (int i = 0; i < size; i++) Arrays.fill(map[i], ".");
    }

    public void print() {
        System.out.print("   ");
        for (int i = 0; i < size; i++)
            System.out.print((char) ('A' + i) + " ");
        System.out.println();
        for (int i = 0; i < size; i++) {
            System.out.printf("%2d ", i + 1);
            for (int j = 0; j < size; j++) {
                String cell = map[i][j];
                if (i == lastRow && j == lastCol && (cell.equals("O") || cell.equals("X")))
                    System.out.print(cell + "* ");
                else
                    System.out.print(cell + "  ");
            }
            System.out.println();
        }
    }

    public boolean isInBounds(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    public boolean isEmpty(int row, int col) {
        return map[row][col].equals(".");
    }

    public void placeStone(int row, int col, String stone) {
        map[row][col] = stone;
        lastRow = row;
        lastCol = col;
    }

    public boolean checkWin(int row, int col, String stone) {
        return count(row, col, stone, 1, 0) + count(row, col, stone, -1, 0) + 1 >= 5 ||
               count(row, col, stone, 0, 1) + count(row, col, stone, 0, -1) + 1 >= 5 ||
               count(row, col, stone, 1, 1) + count(row, col, stone, -1, -1) + 1 >= 5 ||
               count(row, col, stone, 1, -1) + count(row, col, stone, -1, 1) + 1 >= 5;
    }

    private int count(int row, int col, String stone, int dRow, int dCol) {
        int count = 0;
        int r = row + dRow;
        int c = col + dCol;
        while (isInBounds(r, c) && map[r][c].equals(stone)) {
            count++;
            r += dRow;
            c += dCol;
        }
        return count;
    }

    public boolean isForbiddenMove(int row, int col, String stone) {
        // 금수는 흑(첫 번째 플레이어 O)만 해당
        if (!stone.equals("O")) return false;

        map[row][col] = stone; // 임시 착수
        int openThrees = countOpenThree(row, col, stone);
        int openFours = countOpenFour(row, col, stone);
        map[row][col] = "."; // 복원

        return openThrees >= 2 || openFours >= 2 || isOverline(row, col, stone);
    }

    private boolean isOverline(int row, int col, String stone) {
        return count(row, col, stone, 1, 0) + count(row, col, stone, -1, 0) + 1 > 5 ||
               count(row, col, stone, 0, 1) + count(row, col, stone, 0, -1) + 1 > 5 ||
               count(row, col, stone, 1, 1) + count(row, col, stone, -1, -1) + 1 > 5 ||
               count(row, col, stone, 1, -1) + count(row, col, stone, -1, 1) + 1 > 5;
    }

    private int countOpenThree(int row, int col, String stone) {
        return countOpenPattern(row, col, stone, 3);
    }

    private int countOpenFour(int row, int col, String stone) {
        return countOpenPattern(row, col, stone, 4);
    }

    private int countOpenPattern(int row, int col, String stone, int length) {
        int openCount = 0;
        int[][] dirs = { {1,0}, {0,1}, {1,1}, {1,-1} };
        for (int[] dir : dirs) {
            int count = 1 + count(row, col, stone, dir[0], dir[1]) + count(row, col, stone, -dir[0], -dir[1]);
            if (count == length) {
                int r1 = row + (count * dir[0]);
                int c1 = col + (count * dir[1]);
                int r2 = row - (count * dir[0]);
                int c2 = col - (count * dir[1]);
                if ((isInBounds(r1, c1) && isEmpty(r1, c1)) && (isInBounds(r2, c2) && isEmpty(r2, c2))) {
                    openCount++;
                }
            }
        }
        return openCount;
    }
}

