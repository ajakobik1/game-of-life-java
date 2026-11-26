import org.example.GameOfLifeBoard;
import org.example.JdbcGameOfLifeBoardDao;
import org.example.PlainGameOfLifeSimulator;
import org.example.exceptions.GameOfLifeDaoException;
import org.example.exceptions.GameOfLifeDaoIoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcDaoTest {

    private final String dbUrl = "jdbc:postgresql://localhost:5432/postgres"; // Connection string to the test database
    private final String user = "postgres";
    private final String password = "jakieshaslo";

    @Test
    public void testJdbcWriteAndRead() {
        Random random = new Random();
        PlainGameOfLifeSimulator simulator = new PlainGameOfLifeSimulator();
        GameOfLifeBoard board = new GameOfLifeBoard(random.nextInt(10) + 3, random.nextInt(10) + 3, simulator);

        // Fill the board with random values
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                board.set(j, i, random.nextBoolean());
            }
        }

        try (Connection connection = DriverManager.getConnection(dbUrl, user, password);
             JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao(dbUrl, user, password, 1, "TestBoard")) {

            // Ensure the board exists in GameOfLifeBoard table
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO GameOfLifeBoard (id, board_name) VALUES (1, 'TestBoard')")) {
                stmt.executeUpdate();
            }

            dao.write(board);
            GameOfLifeBoard readBoard = dao.read();

            assertEquals(board.getHeight(), readBoard.getHeight());
            assertEquals(board.getWidth(), readBoard.getWidth());
            assertNotSame(board, readBoard, "Odczytana plansza powinna być inną instancją, ale równą oryginałowi.");

            for (int i = 0; i < board.getHeight(); i++) {
                for (int j = 0; j < board.getWidth(); j++) {
                    assertEquals(board.get(j, i), readBoard.get(j, i),
                            "Mismatch at cell (" + j + ", " + i + ")");
                }
            }
        } catch (GameOfLifeDaoException | SQLException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testWriteInvalidBoard() {
        GameOfLifeBoard invalidBoard = new GameOfLifeBoard(0, 0, new PlainGameOfLifeSimulator());

        try (Connection connection = DriverManager.getConnection(dbUrl, user, password);
             JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao(dbUrl, user, password, 1, "TestBoard")) {

            // Ensure the board exists in GameOfLifeBoard table
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO GameOfLifeBoard (id, board_name) VALUES (1, 'TestBoard')")) {
                stmt.executeUpdate();
            }

            assertThrows(GameOfLifeDaoIoException.class, () -> dao.write(invalidBoard));
        } catch (GameOfLifeDaoException | SQLException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidDaoInitialization() {
        assertThrows(GameOfLifeDaoException.class, () ->
                new JdbcGameOfLifeBoardDao(dbUrl, user, password, -1, ""));
    }

    @Test
    public void testDatabaseConnection() {
        try (JdbcGameOfLifeBoardDao jdbcBoard = new JdbcGameOfLifeBoardDao("wrongurl",
                user,
                password,
                1,
                "testcon")) {

            assertThrows(GameOfLifeDaoIoException.class, () -> jdbcBoard.read(),
                    "not connected to the database");

            GameOfLifeBoard board = new GameOfLifeBoard(6, 6, new PlainGameOfLifeSimulator());

            assertThrows(GameOfLifeDaoIoException.class, () -> jdbcBoard.write(board),
                    "not connected to the database");

        } catch (GameOfLifeDaoException e) {
            fail();
        }

        try (JdbcGameOfLifeBoardDao jdbcBoard = new JdbcGameOfLifeBoardDao(dbUrl,
                "notauser",
                password,
                1,
                "testcon")) {

            assertThrows(GameOfLifeDaoIoException.class, () -> jdbcBoard.read(),
                    "wrong username");

            GameOfLifeBoard board = new GameOfLifeBoard(6, 6, new PlainGameOfLifeSimulator());

            assertThrows(GameOfLifeDaoIoException.class, () -> jdbcBoard.write(board),
                    "wrong username");

        } catch (GameOfLifeDaoException e) {
            fail();
        }


    }

    @AfterEach
    public void cleanupDatabase() {
        try (Connection connection = DriverManager.getConnection(dbUrl, user, password)) {
            try (PreparedStatement deleteFieldsStmt = connection.prepareStatement("DELETE FROM GameOfLifeField WHERE board_id = 1");
                 PreparedStatement deleteBoardStmt = connection.prepareStatement("DELETE FROM GameOfLifeBoard WHERE id = 1")) {
                deleteFieldsStmt.executeUpdate();
                deleteBoardStmt.executeUpdate();
            }
        } catch (SQLException e) {
            fail("Failed to clean up the database: " + e.getMessage());
        }
    }


}
