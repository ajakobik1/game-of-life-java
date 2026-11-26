import org.example.*;
import org.example.exceptions.GameOfLifeDaoIoException;
import org.example.exceptions.GameOfLifeDaoCloseException;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class DaoTest {

    @Test
    public void testFileGameOfLifeBoardDao() {
        GameOfLifeBoardDaoFactory boardFactory = new GameOfLifeBoardDaoFactory(5, 5);

        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                boardFactory.createFile(null));

        Random random = new Random();
        PlainGameOfLifeSimulator simulator = new PlainGameOfLifeSimulator();
        GameOfLifeBoard board = new GameOfLifeBoard((random.nextInt(50)) + 3,
                random.nextInt(50) + 3, simulator);

        try (FileGameOfLifeBoardDao boardDao = (FileGameOfLifeBoardDao) boardFactory.
                createFile("test1.txt")) {
            GameOfLifeBoard board1 = boardDao.read();
        } catch (GameOfLifeDaoIoException e) {
            assertTrue(e.getCause() instanceof java.io.IOException, "Expected IOException as the cause");
        } catch (GameOfLifeDaoCloseException | IOException e) {
            fail("Unexpected exception");
        }

        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                board.set(j, i, false);
            }
        }

        GameOfLifeRow[] rows = new GameOfLifeRow[board.getHeight()];
        GameOfLifeColumn[] columns = new GameOfLifeColumn[board.getWidth()];

        for (int i = 0; i < board.getHeight(); i++) {
            rows[i] = board.getRow(i);
        }
        for (int i = 0; i < board.getWidth(); i++) {
            columns[i] = board.getColumn(i);
        }

        try (FileGameOfLifeBoardDao boardDao = (FileGameOfLifeBoardDao) boardFactory.
                createFile("test2.txt")) {
            boardDao.write(board);
            GameOfLifeBoard board2 = boardDao.read();
            assertEquals(board2.getHeight(), board.getHeight());
            assertEquals(board2.getWidth(), board.getWidth());

            for (int i = 0; i < board2.getHeight(); i++) {
                assertEquals(rows[i].countAliveCells(), board2.getRow(i).countAliveCells());
            }

            for (int i = 0; i < board2.getWidth(); i++) {
                assertEquals(columns[i].countAliveCells(), board2.getColumn(i).countAliveCells());
            }

            board2.doSimulationStep();
        } catch (GameOfLifeDaoCloseException | IOException e) {
            fail("Unexpected exception");
        } catch (GameOfLifeDaoIoException e) {
            fail("Unexpected read/write exception");
        }

        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                board.set(j, i, false);
            }
        }

        try (FileGameOfLifeBoardDao boardDao = (FileGameOfLifeBoardDao) boardFactory.
                createFile("test3.txt")) {
            boardDao.write(board);
            GameOfLifeBoard board2 = boardDao.read();
            assertEquals(board2.getHeight(), board.getHeight());
            assertEquals(board2.getWidth(), board.getWidth());
            for (int i = 0; i < board2.getHeight(); i++) {
                for (int j = 0; j < board2.getWidth(); j++) {
                    assertFalse(board2.get(i, j));
                }
            }
        } catch (GameOfLifeDaoCloseException | IOException e) {
            fail("Unexpected exception");
        } catch (GameOfLifeDaoIoException e) {
            fail("Unexpected read/write exception");
        }
    }

    @Test
    public void testReadIOException() {
        GameOfLifeBoardDaoFactory boardFactory = new GameOfLifeBoardDaoFactory(5, 5);

        try (FileGameOfLifeBoardDao boardDao = (FileGameOfLifeBoardDao) boardFactory.
                createFile("nonexistent_file")) {
            GameOfLifeBoard board = boardDao.read();
            fail("Expected GameOfLifeDaoIOException but no exception was thrown");
        } catch (GameOfLifeDaoIoException e) {
            assertTrue(e.getCause() instanceof java.io.IOException, "Expected IOException as the cause");
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testWriteIOException() {
        Random random = new Random();
        PlainGameOfLifeSimulator simulator = new PlainGameOfLifeSimulator();
        GameOfLifeBoard board = new GameOfLifeBoard((random.nextInt(50)) + 3,
                random.nextInt(50) + 3, simulator);

        File tempDir = new File("testDir");
        tempDir.mkdir();
        tempDir.deleteOnExit(); // Cleanup after test

        try {
            FileGameOfLifeBoardDao dao = new FileGameOfLifeBoardDao(tempDir.getPath());
            dao.write(board);
            fail("Expected GameOfLifeDaoIOException but no exception was thrown");
        } catch (GameOfLifeDaoIoException e) {
            assertInstanceOf(IOException.class, e.getCause(), "Expected IOException as the cause");
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testPrototype() {
        Random random = new Random();
        GameOfLifeBoardDaoFactory boardFactory = new GameOfLifeBoardDaoFactory(random.nextInt(50) + 3,
                random.nextInt(50) + 3);
        GameOfLifeBoard board1 = boardFactory.createInstance();
        GameOfLifeBoard board2 = boardFactory.createInstance();

        assertNotEquals(board1, board2, "Clones shouldn't be equal.");
    }

    @Test
    public void testJdbc(){

    }
}
