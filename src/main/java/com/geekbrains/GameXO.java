package com.geekbrains;

import java.util.*;

public class GameXO {
    public static int SIZE = 4;
    public static int DOTS_TO_WIN = 4;
    public static final char DOT_EMPTY = '•';
    public static final char DOT_X = 'X';
    public static final char DOT_O = 'O';
    public static char[][] map;
    public static Scanner sc = new Scanner(System.in);
    public static Random rand = new Random();

    public static void main(String[] args) {
        initMap();
        printMap();
        while (true) {
            humanTurn();
            printMap();
            if (checkWin(DOT_X)) {
                System.out.println("Победил человек");
                break;
            }
            if (isMapFull()) {
                System.out.println("Ничья");
                break;
            }
            aiTurn();
            printMap();
            if (checkWin(DOT_O)) {
                System.out.println("Победил Искуственный Интеллект");
                break;
            }
            if (isMapFull()) {
                System.out.println("Ничья");
                break;
            }
        }
        System.out.println("Игра закончена");
    }

    private static PairLineCount maxMatchHorizontalCount = new PairLineCount(LineEnum.horizontal);
    private static PairLineCount maxMatchVerticalCount = new PairLineCount(LineEnum.vertical);
    private static PairLineCount maxMatchRightDiagonalCount = new PairLineCount(LineEnum.rightDiagonal);
    private static PairLineCount maxMatchLeftDiagonalCount = new PairLineCount(LineEnum.leftDiagonal);

    public static boolean checkWin(char symb) {
        int matchHorizontalCount, matchVerticalCount, matchRightDiagonalCount, matchLeftDiagonalCount;
        matchRightDiagonalCount = 0;
        matchLeftDiagonalCount = 0;
        for (int i = 0; i < map.length; i++) {
            matchHorizontalCount = 0;
            matchVerticalCount = 0;

            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == symb)
                    matchHorizontalCount++;
                else if (matchHorizontalCount < DOTS_TO_WIN) {
                    if (maxMatchHorizontalCount.count < matchHorizontalCount) {
                        maxMatchHorizontalCount.count = matchHorizontalCount;
                        maxMatchHorizontalCount.line = i;
                    }
                    matchHorizontalCount = 0;
                }


                if (map[j][i] == symb)
                    matchVerticalCount++;
                else if (matchVerticalCount < DOTS_TO_WIN) {
                    if (maxMatchVerticalCount.count < matchVerticalCount) {
                        maxMatchVerticalCount.count = matchVerticalCount;
                        maxMatchVerticalCount.line = i;
                    }

                    matchVerticalCount = 0;
                }


                if (i == j)
                    if (map[i][j] == symb)
                        matchRightDiagonalCount++;
                    else if (matchRightDiagonalCount < DOTS_TO_WIN) {
                        if (maxMatchRightDiagonalCount.count < matchRightDiagonalCount) {
                            maxMatchRightDiagonalCount.count = matchRightDiagonalCount;
                            maxMatchRightDiagonalCount.line = i;
                        }
                        matchRightDiagonalCount = 0;
                    }


                if (i == map.length - 1 - j)
                    if (map[i][j] == symb)
                        matchLeftDiagonalCount++;
                    else if (matchLeftDiagonalCount < DOTS_TO_WIN) {
                        if (maxMatchLeftDiagonalCount.count < matchLeftDiagonalCount) {
                            maxMatchLeftDiagonalCount.count = matchLeftDiagonalCount;
                            maxMatchLeftDiagonalCount.line = i;
                        }

                        matchLeftDiagonalCount = 0;
                    }


                if ((j == map.length - 1) && ((matchHorizontalCount >= DOTS_TO_WIN) || (matchVerticalCount >= DOTS_TO_WIN))) {
                    return true;
                }

            }
            if ((i == map.length - 1) && ((matchRightDiagonalCount >= DOTS_TO_WIN) || (matchLeftDiagonalCount >= DOTS_TO_WIN))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMapFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == DOT_EMPTY) return false;
            }
        }
        return true;
    }

    private static List<PairLineCount> aiChooseLine() {

        List<PairLineCount> pairLineCountList = new ArrayList<>();
        pairLineCountList.add(maxMatchHorizontalCount);
        pairLineCountList.add(maxMatchVerticalCount);
        pairLineCountList.add(maxMatchRightDiagonalCount);
        pairLineCountList.add(maxMatchLeftDiagonalCount);
        Collections.sort(pairLineCountList, Collections.reverseOrder());

        return pairLineCountList;

    }

    private static void aiGetXY(List<PairLineCount> pairLineCountList) {

        for (PairLineCount pairLineCount : pairLineCountList) {
            if (pairLineCount.lineDirection == LineEnum.horizontal) {
                for (int i = 0; i < map.length; i++) {
                    if (isCellValid(i, pairLineCount.line)) {
                        aiPrintStep(i, pairLineCount.line);
                        return;
                    }
                }
            } else if (pairLineCount.lineDirection == LineEnum.vertical) {
                for (int i = 0; i < map.length; i++) {
                    if (isCellValid(pairLineCount.line, i)) {
                        aiPrintStep(pairLineCount.line, i);
                        return;
                    }
                }
            } else if (pairLineCount.lineDirection == LineEnum.rightDiagonal) {

                for (int i = 0; i < map.length; i++) {
                    for (int j = 0; j < map.length; j++) {
                        if (i == j) {
                            if (isCellValid(i, j)) {
                                aiPrintStep(i, j);
                                return;

                            }
                        }

                    }
                }

            } else if (pairLineCount.lineDirection == LineEnum.leftDiagonal) {

                for (int i = 0; i < map.length; i++) {
                    for (int j = 0; j < map.length; j++) {
                        if (i == map.length - 1 - j) {
                            if (isCellValid(i, map.length - 1 - j)) {
                                aiPrintStep(i, map.length - 1 - j);
                                return;
                            }
                        }

                    }
                }

            }
        }
        int x, y;
        do {
            x = rand.nextInt(SIZE);
            y = rand.nextInt(SIZE);
        } while (!isCellValid(x, y));
        aiPrintStep(x, y);


    }

    private static void aiPrintStep(int x, int y) {

        System.out.println("Компьютер походил в точку " + (x + 1) + " " + (y + 1));
        map[y][x] = DOT_O;
    }

    public static void aiTurn() {
        aiGetXY(aiChooseLine());
    }

    public static void humanTurn() {
        int x, y;
        do {
            System.out.println("Введите координаты в формате X Y");
            x = sc.nextInt() - 1;
            y = sc.nextInt() - 1;
        } while (!isCellValid(x, y)); // while(isCellValid(x, y) == false)
        map[y][x] = DOT_X;
    }

    public static boolean isCellValid(int x, int y) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) return false;
        if (map[y][x] == DOT_EMPTY)
            return true;
        return false;
    }

    public static void initMap() {
        map = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                map[i][j] = DOT_EMPTY;
            }
        }
    }

    public static void printMap() {
        for (int i = 0; i <= SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
