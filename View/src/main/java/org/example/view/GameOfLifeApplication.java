package org.example.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameOfLifeApplication extends Application {
    private Logger logger = LoggerFactory.getLogger(GameOfLifeApplication.class);
    @Override
    public void start(Stage stage) throws IOException {
        logger.debug("Starting GameOfLifeApplication");
        Locale locale = new Locale.Builder().setLanguage("en").setRegion("UK").build();
        FXMLLoader fxmlLoader = new FXMLLoader(GameOfLifeApplication.class.getResource("Menu.fxml"),
                ResourceBundle.getBundle(Language.EN.getPath(), locale));

        try (Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres",
                "postgres",
                "jakieshaslo")) {
            connection.setAutoCommit(false);
            String checkBoardCountQuery = "SELECT id FROM GameOfLifeBoard WHERE id = ?";
            String insertBoardQuery = "INSERT INTO GameOfLifeBoard (id, board_name) VALUES (?, 'abc')";

            PreparedStatement insertBoardStatement = connection.prepareStatement(insertBoardQuery);

            logger.debug("Checking for existing boards...");
            for (int i = 1; i <= 3; i++) {
                PreparedStatement checkBoardCountStatement = connection.prepareStatement(checkBoardCountQuery);
                checkBoardCountStatement.setInt(1, i);
                ResultSet checkBoardCountResult = checkBoardCountStatement.executeQuery();
                if (!checkBoardCountResult.next()) {
                    logger.info("Adding entry {} to the database", i);
                    insertBoardStatement.setInt(1, i);
                    insertBoardStatement.addBatch();
                }
            }
            insertBoardStatement.executeBatch();
            connection.commit();
            connection.rollback();

        } catch (SQLException e) {
            logger.error("SQL Exception while connecting to database", e);
        }
        Scene scene = new Scene(fxmlLoader.load(), 370, 300);
        stage.setTitle("GameOfLife Simulation");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}