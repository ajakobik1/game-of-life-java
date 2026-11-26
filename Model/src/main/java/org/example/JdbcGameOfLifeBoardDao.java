package org.example;

import org.example.exceptions.GameOfLifeDaoException;
import org.example.exceptions.GameOfLifeDaoIoException;

import java.sql.*;

public class JdbcGameOfLifeBoardDao implements Dao<GameOfLifeBoard> {

    private final String dbUrl;
    private final String user;
    private final String password;
    private final int id;
    private final String name;

    public JdbcGameOfLifeBoardDao(String dbUrl, String user, String password, int id, String name)
            throws GameOfLifeDaoException {
        if (id > 0 && id <= 3 && name != null && !name.isEmpty() && name.length() < 256) {
            this.dbUrl = dbUrl;
            this.user = user;
            this.password = password;
            this.id = id;
            this.name = name;
        } else {
            throw new GameOfLifeDaoException("Illegal argument used.");
        }
    }

    @Override
    public GameOfLifeBoard read() throws GameOfLifeDaoIoException {
        String selectFieldsQuery = "SELECT row, col, value FROM GameOfLifeField WHERE board_id = ?";

        try (Connection connection = DriverManager.getConnection(dbUrl, user, password)) {

            connection.setAutoCommit(false);

            // Zakładamy, że rozmiar planszy musimy wywnioskować z danych
            int height = 0;
            int width = 0;

            try (PreparedStatement fieldStmt = connection.prepareStatement(selectFieldsQuery)) {
                fieldStmt.setInt(1, this.id);

                try (ResultSet fieldRs = fieldStmt.executeQuery()) {
                    connection.commit();
                    while (fieldRs.next()) {
                        int row = fieldRs.getInt("row");
                        int col = fieldRs.getInt("col");

                        // Określenie wymiarów planszy na podstawie danych
                        height = Math.max(height, row + 1);
                        width = Math.max(width, col + 1);
                    }
                }
            }

            boolean[][] values = new boolean[height][width];

            try (PreparedStatement fieldStmt = connection.prepareStatement(selectFieldsQuery)) {
                fieldStmt.setInt(1, this.id);

                try (ResultSet fieldRs = fieldStmt.executeQuery()) {
                    connection.commit();
                    while (fieldRs.next()) {
                        int row = fieldRs.getInt("row");
                        int col = fieldRs.getInt("col");
                        boolean value = fieldRs.getBoolean("value");
                        values[row][col] = value;
                    }
                }
            }

            GameOfLifeBoard board = new GameOfLifeBoard(height, width, new PlainGameOfLifeSimulator());

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    board.set(j, i, values[i][j]);
                }
            }

            connection.rollback();
            return board;

        } catch (SQLException e) {
            throw new GameOfLifeDaoIoException(e);
        }
    }


    @Override
    public void write(GameOfLifeBoard board) throws GameOfLifeDaoIoException {
        if (board.getHeight() <= 0 || board.getWidth() <= 0) {
            throw new GameOfLifeDaoIoException("Board dimensions must be greater than 0");
        }

        String insertBoardQuery = "UPDATE GameOfLifeBoard SET board_name = ? WHERE id = ?";
        String insertFieldQuery = "INSERT INTO GameOfLifeField (row, col, value, board_id) VALUES (?, ?, ?, ?)";
        String removeFieldsQuery = "DELETE FROM GameOfLifeField WHERE board_id = ?";

        try (Connection connection = DriverManager.getConnection(dbUrl, user, password);
             PreparedStatement removeStmt = connection.prepareStatement(removeFieldsQuery);
             PreparedStatement boardStmt = connection.prepareStatement(insertBoardQuery);
             PreparedStatement fieldStmt = connection.prepareStatement(insertFieldQuery)) {

            removeStmt.setInt(1, this.id);
            removeStmt.executeUpdate();

            connection.setAutoCommit(false);

            boardStmt.setString(1, this.name);
            boardStmt.setInt(2, this.id);
            boardStmt.executeUpdate();

            for (int row = 0; row < board.getHeight(); row++) {
                for (int col = 0; col < board.getWidth(); col++) {
                    fieldStmt.setInt(1, row);
                    fieldStmt.setInt(2, col);
                    fieldStmt.setInt(3, board.get(col, row) ? 1 : 0);
                    fieldStmt.setInt(4, this.id);
                    fieldStmt.addBatch();
                }
            }

            fieldStmt.executeBatch();
            connection.commit();
            connection.rollback();

        } catch (SQLException e) {
            throw new GameOfLifeDaoIoException(e);
        }
    }


    @Override
    public void close() {
    }
}
