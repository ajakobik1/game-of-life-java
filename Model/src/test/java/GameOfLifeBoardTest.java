import org.example.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class GameOfLifeBoardTest {
    @Test
    public void testDifferentInitialization() { // test sprawdza, czy dwie rozne intancje planszy sa inicjowane inaczej
        Random random = new Random();

        // Losowanie wymiarów planszy
        int randomHeight = 2 + random.nextInt(50); // minimalny wymiar to 2, maksymalny to 50
        int randomWidth = 2 + random.nextInt(50); // minimalny wymiar to 2, maksymalny to 50
        GameOfLifeSimulator simulator = new PlainGameOfLifeSimulator();
        //
        GameOfLifeBoard board1=new GameOfLifeBoard(randomHeight,randomWidth, simulator);
        GameOfLifeBoard board2=new GameOfLifeBoard(randomHeight,randomWidth, simulator);

        int howManyNotRepeat = randomHeight*randomWidth;
        for (int i = 0; i < randomHeight; i++) {
            for (int j = 0; j < randomWidth; j++) {
                if (board1.get(j, i) == board2.get(j, i)) {
                    howManyNotRepeat--;
                }
            }
        }
        assertNotEquals(howManyNotRepeat, 0);
    }
    @Test
    void testToString() {
        Random random = new Random();

        //DLA CELLA
        // Losowanie wymiarów planszy
        int randomHeight = 2 + random.nextInt(50); // minimalny wymiar to 2, maksymalny to 50
        int randomWidth = 2 + random.nextInt(50); // minimalny wymiar to 2, maksymalny to 50
        int n = random.nextInt(randomHeight);
        int m = random.nextInt(randomWidth);
        GameOfLifeSimulator simulator = new PlainGameOfLifeSimulator();
        //
        GameOfLifeBoard board1=new GameOfLifeBoard(randomHeight,randomWidth, simulator);
        GameOfLifeNeighbors neighbors = new GameOfLifeNeighbors(board1);
        GameOfLifeCell cell = new GameOfLifeCell(true, n, m, neighbors);

        // Oczekiwana reprezentacja stringowa dla tej komórki
        String expectedToString = "GameOfLifeCell@[value=true,column=" + n + ",row=" + m + ",neighbors=" + neighbors + "]";

        String[] strArray = cell.toString().split("@");
        String[] strArray2 = cell.toString().split("\\[");
        String[] strArray3 = strArray[0].split("\\.");
        String actualToString = strArray3[2] + "@[" + strArray2[1];
        // Sprawdzamy czy sklejony string zgadza się z oczekiwanym
        assertEquals(expectedToString, actualToString);


        // DLA RZĘDU
        int randomRowLength = randomWidth;

        // Tworzenie listy komórek dla rzędu
        List<GameOfLifeCell> cells = new ArrayList<>();
        int n1 = random.nextInt(randomHeight); // Stała wartość dla wiersza
        for (int m1 = 0; m1 < randomRowLength; m1++) { // Iteracja tylko po kolumnach
            GameOfLifeCell cell1 = new GameOfLifeCell(random.nextBoolean(), n1, m1, neighbors);
            cells.add(cell1);
        }

        // Tworzenie rzędu
        GameOfLifeRow row = new GameOfLifeRow(cells);

        String expectedToString2 = "GameOfLifeRow@[cells=" + cells + "]";
        String[] strArray4 = row.toString().split("@");
        String[] strArray5 = row.toString().split("cells");
        String[] strArray6 = strArray4[0].split("\\.");
        String actualToString2 = strArray6[2] + "@[cells" + strArray5[1];

        // Sprawdzamy czy sklejony string zgadza się z oczekiwanym
        assertEquals(expectedToString2, actualToString2);


        // DLA KOLUMNY
        int randomColumnLength = randomHeight;

        // Tworzenie listy komórek dla rzędu
        List<GameOfLifeCell> cells2 = new ArrayList<>();
        int n2 = random.nextInt(randomWidth); // Stała wartość dla kolumny

        for (int m2 = 0; m2 < randomColumnLength; m2++) { // Iteracja tylko po wierszach
            GameOfLifeCell cell2 = new GameOfLifeCell(random.nextBoolean(), n2, m2, neighbors);
            cells2.add(cell2);
        }

        // Tworzenie rzędu
        GameOfLifeColumn column = new GameOfLifeColumn(cells2);

        String expectedToString3 = "GameOfLifeColumn@[cells=" + cells2 + "]";
        String[] strArray7 = column.toString().split("@");
        String[] strArray8 = column.toString().split("cells");
        String[] strArray9 = strArray7[0].split("\\.");
        String actualToString3 = strArray9[2] + "@[cells" + strArray8[1];

        // Sprawdzamy czy sklejony string zgadza się z oczekiwanym
        assertEquals(expectedToString3, actualToString3);

        // DLA BOARDA
        String expectedToStringBoard = "GameOfLifeBoard@[board=" + board1;

        String[] strArray10 = board1.toString().split("@");
        String[] strArray11 = board1.toString().split("@\\[");
        String[] strArray12 = strArray10[0].split("\\.");
        String actualToString4 = strArray12[2] + "@[board=" + strArray11[0];

        assertEquals(expectedToStringBoard, actualToString4);
    }

    @Test
    void testGameOfLifeConstructor() {
        Random random = new Random();

        // Losowanie wymiarów planszy
        int randomHeight = 2 + random.nextInt(50); // minimalny wymiar to 2, maksymalny to 50
        int randomWidth = 2 + random.nextInt(50); // minimalny wymiar to 2, maksymalny to 50
        GameOfLifeSimulator simulator = new PlainGameOfLifeSimulator(); // lub mock, jeśli klasa jest rozbudowana

        // Tworzenie obiektu GameOfLifeBoard
        GameOfLifeBoard board = new GameOfLifeBoard(randomHeight, randomWidth, simulator);

        // Test czy są dobre rozmiary planszy
        assertEquals(randomHeight, board.getHeight(), "Nieprawidłowa wysokość planszy");
        assertEquals(randomWidth, board.getWidth(), "Nieprawidłowa szerokość planszy");

        int totalCells = 0;

        // Test czy jest dobra ilosć komórek
        for (int i = 0; i < board.getHeight(); i++) {
            totalCells += board.getRow(i).countAliveCells() + board.getRow(i).countDeadCells();
        }

        assertEquals(randomHeight * randomWidth, totalCells, "Liczba komórek na planszy powinna być równa n * m.");

    }

    @Test
    public void testRandomBoardDoStep() {
        Random random = new Random();

        // Losowanie wymiarów planszy
        int randomHeight = 2 + random.nextInt(50); // minimalny wymiar to 2, maksymalny to 50
        int randomWidth = 2 + random.nextInt(50); // minimalny wymiar to 2, maksymalny to 50
        GameOfLifeSimulator simulator = new PlainGameOfLifeSimulator();
        GameOfLifeBoard board = new GameOfLifeBoard(randomHeight, randomWidth, simulator);

        // Kopiujemy stan początkowy do tablicy, aby porównać po kroku symulacji
        boolean[][] initialState = new boolean[randomHeight][randomWidth];
        for (int i = 0; i < randomHeight; i++) {
            for (int j = 0; j < randomWidth; j++) {
                initialState[i][j] = board.get(j, i);
            }
        }

        // Wykonujemy jeden krok symulacji
        board.doSimulationStep();

        // Weryfikujemy, czy każda komórka stosuje zasady Game of Life
        for (int i = 0; i < randomHeight; i++) {
            for (int j = 0; j < randomWidth; j++) {
                int aliveNeighbors = countAliveNeighbors(initialState, j, i, randomHeight, randomWidth);
                boolean wasAlive = initialState[i][j];
                boolean isAliveNow = board.get(j, i);

                // Sprawdzamy, czy komórka stosuje reguły przeżycia i narodzin
                if (wasAlive) {
                    // Jeśli komórka była żywa, powinna pozostać żywa przy 2 lub 3 żywych sąsiadach
                    assertEquals(aliveNeighbors == 2 || aliveNeighbors == 3, isAliveNow,
                            "Komórka (" + j + ", " + i + ") powinna stosować reguły przeżycia.");
                } else {
                    // Jeśli komórka była martwa, powinna ożyć przy dokładnie 3 żywych sąsiadach
                    assertEquals(aliveNeighbors == 3, isAliveNow,
                            "Martwa komórka (" + j + ", " + i + ") powinna stosować reguły 'ożycia'.");
                }
            }
        }
    }

    // Funkcja pomocnicza do zliczania żywych sąsiadów
    private int countAliveNeighbors(boolean[][] board, int x, int y, int n, int m) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue; // Pomijamy samą komórkę
                int newX = (x + j + m) % m;
                int newY = (y + i + n) % n;
                if (board[newY][newX]) {
                    count++;
                }
            }
        }
        return count;
    }


    @Test
    public void testGettersAndSet() {
        Random random = new Random();

        // Losowanie wymiarów planszy
        int randomHeight = 4 + random.nextInt(50); // minimalny wymiar to 4, maksymalny to 50
        int randomWidth = 4 + random.nextInt(50); // minimalny wymiar to 4, maksymalny to 50
        GameOfLifeSimulator simulator = new PlainGameOfLifeSimulator();
        //
        GameOfLifeBoard board1 = new GameOfLifeBoard(randomHeight, randomWidth, simulator);
        boolean [][] testBoard1 = new boolean[randomHeight][randomWidth];

        testBoard1[2][3]=true;
        testBoard1[3][3]=true;
        testBoard1[3][2]=true;
        testBoard1[2][2]=true;

        for (int i = 0; i < board1.getHeight(); i++) {
            for (int j = 0; j < board1.getWidth(); j++) {
                board1.set(j, i, testBoard1[i][j]);
            }
        }

        for (int i = 0; i < randomHeight; i++) {
            for (int j = 0; j < randomWidth; j++) {
                if ((i == 2 || i == 3) && (j == 2 || j == 3)) {
                    assertTrue(board1.get(j, i));
                } else {
                    assertFalse(board1.get(j, i));
                }
            }
        }

        int m=5;
        int n=5;
        GameOfLifeBoard board2=new GameOfLifeBoard(n,m, simulator);
        boolean [][] testBoard2 = new boolean[n][m];

        for (int i = 0; i < board2.getHeight(); i++) { //kopiujemy testBoard na board
            for (int j = 0; j < board2.getWidth(); j++) {
                board2.set(j, i, testBoard2[i][j]);
            }
        }

        board2.set(7, 8, true); // (7 % 5 = 2), (8 % 5 = 3)
        board2.set(-4, -2, true); // (-4 % 5 = 1), (-2 % 5 = 3)


        assertTrue(board2.get(2, 3));
        assertTrue(board2.get(1, 3));

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (!(i == 3 && j == 1) && !(i == 3 && j == 2)) {
                    assertFalse(board2.get(j, i), "Expected false at position (" + j + ", " + i + ").");
                }
            }
        }


    }

    @Test
    public void testDoStep() { // test sprawdza, czy metoda DoStep dziala poprawnie w roznych przypadkach

        GameOfLifeSimulator simulator = new PlainGameOfLifeSimulator();
        GameOfLifeBoard board1=new GameOfLifeBoard(5,5, simulator);
        boolean [][] testBoard1 = new boolean[5][5];

        testBoard1[2][3]=true;
        testBoard1[3][3]=true;
        testBoard1[3][2]=true;

        for (int i = 0; i < board1.getHeight(); i++) { //kopiujemy testBoard na board
            for (int j = 0; j < board1.getWidth(); j++) {
                board1.set(j, i, testBoard1[i][j]);
            }
        }

        board1.doSimulationStep();

        for (int i = 0; i < board1.getHeight(); i++) { // test czy ozywa przy trzech, i czy zostaje zywa przy dwoch zywych w sasiedztwie
            for (int j = 0; j < board1.getWidth(); j++) {
                if ((i == 2 || i == 3) && (j == 2 || j == 3)) {
                    assertTrue(board1.get(j, i));
                }
                else {
                    assertFalse(board1.get(j, i));
                }
            }
        }

        board1.doSimulationStep();

        for (int i = 0; i < board1.getHeight(); i++) { // test czy zostaje zywa przy trzech zywych w sasiedztwie
            for (int j = 0; j < board1.getWidth(); j++) {
                if ((i == 2 || i == 3) && (j == 2 || j == 3)) {
                    assertTrue(board1.get(j, i));
                } else {
                    assertFalse(board1.get(j, i));
                }
            }
        }

        boolean [][] testBoard2 = new boolean[5][5];

        testBoard2[4][0]=true;

        for (int i = 0; i < board1.getHeight(); i++) { //kopiujemy testBoard na board
            for (int j = 0; j < board1.getWidth(); j++) {
                board1.set(j, i, testBoard2[i][j]);
            }
        }

        board1.doSimulationStep();
        for (int i = 0; i < board1.getHeight(); i++) { // test smierci z samotnosci (<2 zywe w sasiedztwie)
            for (int j = 0; j < board1.getWidth(); j++) {
                assertFalse(board1.get(j, i));
            }
        }

        boolean [][] testBoard3 = new boolean[5][5];

        testBoard3[4][0]=true;
        testBoard3[4][1]=true;
        testBoard3[4][4]=true;

        for (int i = 0; i < board1.getHeight(); i++) { //kopiujemy testBoard na board
            for (int j = 0; j < board1.getWidth(); j++) {
                board1.set(j, i, testBoard3[i][j]);
            }
        }

        board1.doSimulationStep();

        for (int i = 0; i < board1.getHeight(); i++) { //test rogu
            for (int j = 0; j < board1.getWidth(); j++) {
                if ((i == 0 || i == 3 || i == 4) && j == 0) {
                    assertTrue(board1.get(j, i));
                }
                else {
                    assertFalse(board1.get(j, i));
                }
            }
        }

        boolean [][] testBoard4 = new boolean[5][5];

        testBoard4[1][1]=true;
        testBoard4[1][3]=true;
        testBoard4[2][2]=true;
        testBoard4[3][1]=true;
        testBoard4[3][3]=true;

        for (int i = 0; i < board1.getHeight(); i++) { //kopiujemy testBoard na board
            for (int j = 0; j < board1.getWidth(); j++) {
                board1.set(j, i, testBoard4[i][j]);
            }
        }

        board1.doSimulationStep();

        for (int i = 0; i < board1.getHeight(); i++) { // test smierci od zatloczenia (>3 zywe w sasiedztwie)
            for (int j = 0; j < board1.getWidth(); j++) {
                if ((i == 1 && j == 2) || (i == 2 && j == 1) || (i == 2 && j == 3) || (i == 3 && j == 2)) {
                    assertTrue(board1.get(j, i));
                }
                else {
                    assertFalse(board1.get(j, i));
                }
            }
        }

    }
    @Test
    public void testGetRowsAndGetCols () {
        Random random = new Random();

        // Losowanie wymiarów planszy
        int randomHeight = 2 + random.nextInt(50); // minimalny wymiar to 2, maksymalny to 50
        int randomWidth = 2 + random.nextInt(50); // minimalny wymiar to 2, maksymalny to 50

        GameOfLifeSimulator simulator = new PlainGameOfLifeSimulator();
        GameOfLifeBoard board1=new GameOfLifeBoard(randomHeight,randomWidth, simulator);



        //testujemy czy rzad ma dobra ilosc komorek, rowniez dla ujemnej wartosci
        for (int i = -2; i < board1.getHeight(); i++) {
            GameOfLifeRow row = board1.getRow(i);
            assertNotNull(row, "Row should not be null");
            assertEquals(board1.getWidth(), row.countAliveCells() + row.countDeadCells());
        }



        //testujemy czy kolumna ma dobra ilosc komorek, rowniez dla ujemnej wartosci
        for (int i = -2; i < board1.getWidth(); i++) {
            GameOfLifeColumn column = board1.getColumn(i);
            assertNotNull(column);
            assertEquals(board1.getHeight(), column.countAliveCells() + column.countDeadCells());
        }


        //sprawdzamy czy zmiana na boardzie odpowiada zmianie w wierszu (przy okazji weryfikujemy czy wiersz to wiersz)
        for (int i = 0; i < board1.getWidth(); i++) {
            board1.set(i, 0, false);
        }
        int deadRowAmount = board1.getRow(0).countDeadCells();
        int aliveRowAmount = board1.getRow(0).countAliveCells();
        assertEquals(board1.getWidth(), deadRowAmount);
        assertEquals(0, aliveRowAmount);

        //dla pewnosci zmieniamy jedna wartosc cella na true i sprwadzamy czy dziala
        board1.set(0,0, true);
        deadRowAmount = board1.getRow(0).countDeadCells();
        aliveRowAmount = board1.getRow(0).countAliveCells();
        assertEquals(board1.getWidth() - 1, deadRowAmount);
        assertEquals(1, aliveRowAmount);


        //analogicznie dla kolumny
        for (int i = 0; i < board1.getHeight(); i++) {
            board1.set(0, i, false);
        }

        int deadColAmount = board1.getColumn(0).countDeadCells();
        int aliveColAmount = board1.getColumn(0).countAliveCells();
        assertEquals(board1.getHeight(), deadColAmount);
        assertEquals(0, aliveColAmount);

        board1.set(0,0, true);
        deadColAmount = board1.getColumn(0).countDeadCells();
        aliveColAmount = board1.getColumn(0).countAliveCells();
        assertEquals(board1.getHeight() - 1, deadColAmount);
        assertEquals(1, aliveColAmount);

    }

    @Test
    public void testHashCodesAndEquals() {
        GameOfLifeSimulator simulator = new PlainGameOfLifeSimulator();

        GameOfLifeBoard board1 = new GameOfLifeBoard(2,2,simulator);

        GameOfLifeNeighbors neighbors = new GameOfLifeNeighbors(board1);
        GameOfLifeNeighbors neighbors2 = new GameOfLifeNeighbors(board1);
        GameOfLifeNeighbors neighbors3 = neighbors;

        GameOfLifeCell cell1 = new GameOfLifeCell(true, 0, 0, neighbors);
        GameOfLifeCell cell2 = new GameOfLifeCell(true, 0, 0, neighbors);
        GameOfLifeCell cell3 = cell1;


        assertFalse(neighbors.equals(neighbors2),
                "Attributes are equal, but they are different cells in the memory");

        assertTrue(neighbors.equals(neighbors3),
                "Reference should be the same");

        assertFalse(neighbors.equals(null),
                "Neighbors is not null");

        assertFalse(neighbors.equals(cell1),
                "Class are not equal");

        //Test dla cell
        assertEquals(cell1.hashCode(), cell2.hashCode(),
                "Hash should be the same, the attributes are equal");

        assertFalse(cell1.equals(cell2),
                "Attributes are equal, but they are different cells in the memory");

        assertTrue(cell1.equals(cell3),
                "Reference should be the same");

        assertEquals(cell1.hashCode(),cell3.hashCode());

        assertFalse(cell1.equals(null),
                "Cell is not null");

        assertFalse(cell1.equals(board1),
                "Class is not equal");

        for (int i = 0; i < board1.getHeight(); i++) {
            for (int j = 0; j < board1.getWidth(); j++) {
                board1.set(j, i, false);
            }
        }
        GameOfLifeBoard board2 = new GameOfLifeBoard(2,2,simulator);

        for (int i = 0; i < board1.getHeight(); i++) {
            for (int j = 0; j < board1.getWidth(); j++) {
                board2.set(j, i, false);
            }
        }
        GameOfLifeBoard board3 = board1;

        assertNotEquals(board1.hashCode(), board2.hashCode(),
                "Different columns and rows return different hash");

        assertFalse(board1.equals(board2),
                "Rows, columns and place in memory is different");

        assertTrue(board1.equals(board3),
                "Reference should be the same");

        assertEquals(board1.hashCode(),board3.hashCode());

        assertFalse(board1.equals(null),
                "Board is not null");

        assertFalse(board1.equals(cell1),
                "Class is not equal");

        GameOfLifeRow row1 = board1.getRow(0);
        GameOfLifeRow row2 = board2.getRow(0);
        GameOfLifeRow row3 = board1.getRow(0);

        assertNotEquals(row1.hashCode(), row2.hashCode(),
                "The same values should return the same hash");

        assertFalse(row1.equals(row2),
                "The place in the memory is different");

        assertTrue(row1.equals(row3),
                "Reference should be the same");

        assertEquals(row1.hashCode(),row3.hashCode());

        assertFalse(row1.equals(null),
                "Row is not null");

        assertFalse(row1.equals(cell1),
                "Class is not equal");

        GameOfLifeColumn column1 = board1.getColumn(0);
        GameOfLifeColumn column2 = board2.getColumn(0);
        GameOfLifeColumn column3 = board1.getColumn(0);

        assertNotEquals(column1.hashCode(), column2.hashCode(),
                "The same values should return the same hash");

        assertFalse(column1.equals(column2),
                "The place in the memory is different");

        assertTrue(column1.equals(column3),
                "Reference should be the same");

        assertEquals(column1.hashCode(),column3.hashCode());

        assertFalse(column1.equals(null),
                "Row is not null");

        assertFalse(column1.equals(cell1),
                "Class is not equal");

        //Sprawdzamy kazda kombinacje miedzy klasami z hashami
        assertNotEquals(column1.hashCode(), row1.hashCode(),
                "Hashes should be different between classes");

        assertNotEquals(column1.hashCode(), cell1.hashCode(),
                "Hashes should be different between classes");

        assertNotEquals(column1.hashCode(), board1.hashCode(),
                "Hashes should be different between classes");

        assertNotEquals(row1.hashCode(), cell1.hashCode(),
                "Hashes should be different between classes");

        assertNotEquals(row1.hashCode(), board1.hashCode(),
                "Hashes should be different between classes");

        assertNotEquals(cell1.hashCode(), board1.hashCode(),
                "Hashes should be different between classes");
    }

    @Test
    public void testCellCompare() {
        PlainGameOfLifeSimulator simulator = new PlainGameOfLifeSimulator();
        //nie musi byc random, potrzebujemy go tylko do utworzenia neighbors do konstruktora cella
        GameOfLifeBoard board = new GameOfLifeBoard(5, 5, simulator);
        GameOfLifeNeighbors neighbors = new GameOfLifeNeighbors(board);
        GameOfLifeCell cell1 = new GameOfLifeCell(true, 0, 0, neighbors);
        //rowny cell1
        GameOfLifeCell cell2 = new GameOfLifeCell(true, 0, 0, neighbors);
        //mniejszy od cell1, ta sama pozycja
        GameOfLifeCell cell3 = new GameOfLifeCell(false, 0, 0, neighbors);
        //rowny cell3
        GameOfLifeCell cell4 = new GameOfLifeCell(false, 0, 0, neighbors);
        //mniejszy od cell1 wg pozycji x
        GameOfLifeCell cell5 = new GameOfLifeCell(false, 1, 0, neighbors);
        //mniejszy od cell5 wg pozycji y
        GameOfLifeCell cell6 = new GameOfLifeCell(false, 1, 1, neighbors);

        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                cell1.compareTo(null));

        assertEquals("Specified object is null", exception.getMessage());

        //wiekszy = 1, mniejszy = -1, rowny = 0
        assertEquals(0, cell1.compareTo(cell1),
                "Comparing object to itself should return equal");
        assertEquals(0, cell1.compareTo(cell2),
                "Object with the same values (true) should return equal");
        assertEquals(0, cell2.compareTo(cell1),
                "Reversed compare when equal should also return equal");
        assertEquals(1, cell1.compareTo(cell3),
                "The value of cell1 is bigger (true > false)");
        assertEquals(0, cell3.compareTo(cell4),
                "Object with the same values (false) should return equal");
        assertEquals(1, cell1.compareTo(cell5),
                "Cell1 is bigger (cell1.row > cell5.row)");
        assertEquals(1, cell5.compareTo(cell6),
                "Cell5 is bigger (cell1.column > cell5.column)");
        assertEquals(-1, cell6.compareTo(cell5),
                "Reversed compare when object passed to the method is the one calling it" +
                        "while passing the other one should return reversed result (smaller)");

    }

    @Test
    public void testCloneable() {
        Random random = new Random();
        PlainGameOfLifeSimulator simulator = new PlainGameOfLifeSimulator();
        GameOfLifeBoard board1 = new GameOfLifeBoard(
                random.nextInt(3, 50),
                random.nextInt(3, 50),
                simulator);

        GameOfLifeBoard board2 = board1.clone();

        assertNotEquals(board1, board2, "Cloned board shouldn't be equal");
        assertEquals(board1.getHeight(), board2.getHeight());
        assertEquals(board1.getWidth(), board2.getWidth());

        board1.set(0, 0, false);
        board2.set(0, 0, true);

        assertFalse(board1.get(0, 0), "Cells shouldn't be referenced in board2");

        for (int i = 0; i < board1.getHeight(); i++) {
            assertNotEquals(board1.getColumn(i), board2.getColumn(i),
                    "Cloned columns shouldn't be equal");
        }

        for (int i = 0; i < board1.getWidth(); i++) {
            assertNotEquals(board1.getRow(i), board2.getRow(i),
                    "Cloned rows shouldn't be equal");
        }

        for (int i = 0; i < board1.getWidth(); i++) {
            board1.set(i, 0, false);
        }

        //test dla wiersza
        GameOfLifeRow row1 = board1.getRow(0).clone();

        assertEquals(board1.getWidth(), row1.countDeadCells(),
                "All values of cells in the column should be false");

        for (int i = 0; i < board1.getWidth(); i++) {
            board1.set(i, 0, true);
        }

        assertEquals(board1.getWidth(), row1.countDeadCells(),
                "All values of cells in the column should stay false");

        //test dla kolumny
        for (int i = 0; i < board1.getHeight(); i++) {
            board1.set(0, i, false);
        }

        GameOfLifeColumn column1 = board1.getColumn(0).clone();

        assertEquals(board1.getHeight(), column1.countDeadCells(),
                "All values of cells in the row should be false");

        for (int i = 0; i < board1.getHeight(); i++) {
            board1.set(i, 0, true);
        }

        assertEquals(board1.getHeight(), column1.countDeadCells(),
                "All values of cells in the row should stay false");


        boolean[][] initialState = new boolean[board2.getHeight()][board2.getWidth()];
        for (int i = 0; i < board2.getHeight(); i++) {
            for (int j = 0; j < board2.getWidth(); j++) {
                initialState[i][j] = board2.get(j, i);
            }
        }

        // Wykonujemy jeden krok symulacji
        board2.doSimulationStep();

        // Weryfikujemy, czy każda komórka stosuje zasady Game of Life (testujemy czy neighbors dobrze sie skopiowalo)
        for (int i = 0; i < board2.getHeight(); i++) {
            for (int j = 0; j < board2.getWidth(); j++) {
                int aliveNeighbors = countAliveNeighbors(initialState, j, i, board2.getHeight(), board2.getWidth());
                boolean wasAlive = initialState[i][j];
                boolean isAliveNow = board2.get(j, i);

                // Sprawdzamy, czy komórka stosuje reguły przeżycia i narodzin
                if (wasAlive) {
                    // Jeśli komórka była żywa, powinna pozostać żywa przy 2 lub 3 żywych sąsiadach
                    assertEquals(aliveNeighbors == 2 || aliveNeighbors == 3, isAliveNow,
                            "Cell (" + j + ", " + i + ") should stay alive.");
                } else {
                    // Jeśli komórka była martwa, powinna ożyć przy dokładnie 3 żywych sąsiadach
                    assertEquals(aliveNeighbors == 3, isAliveNow,
                            "Dead cell (" + j + ", " + i + ") should become alive.");
                }
            }
        }


        GameOfLifeNeighbors neighbors = new GameOfLifeNeighbors(board1);

        GameOfLifeCell cell1 = new GameOfLifeCell(true, 0, 0, neighbors);
        GameOfLifeCell cell2 = cell1.clone();

        assertNotEquals(cell1, cell2, "Cells shouldn't be equal");

        cell1.updateState(false);

        assertTrue(cell2.getCellValue(), "Value of the cell should stay true");

        GameOfLifeNeighbors neighbors2 = neighbors.clone();

        assertEquals(neighbors2.countAliveNeighbors(0, 0), neighbors.countAliveNeighbors(0, 0),
                "This value should be the same");

        for (int i = 0; i < board1.getHeight(); i++) {
            for (int j = 0; j < board1.getWidth(); j++) {
                board1.set(i, j, false);
            }
        }

        assertNotEquals(neighbors2.countAliveNeighbors(0, 0), neighbors.countAliveNeighbors(0, 0),
                "This value should be different");

    }

    }


