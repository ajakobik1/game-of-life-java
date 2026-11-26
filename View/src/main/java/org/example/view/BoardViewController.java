package org.example.view;

import javafx.beans.property.adapter.JavaBeanBooleanProperty;
import javafx.beans.property.adapter.JavaBeanBooleanPropertyBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.*;
import org.example.exceptions.GameOfLifeDaoCloseException;
import org.example.exceptions.GameOfLifeDaoIoException;
import org.example.view.exceptions.LocalizedGameOfLifeRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BoardViewController {

    @FXML
    public TextField boardName;
    @FXML
    private GridPane boardGrid;


    private static final int CELL_SIZE = 20; // Rozmiar komórek
    private GameOfLifeBoard gameBoard;
    private Integer rows;
    private Integer columns;
    private JavaBeanBooleanProperty[][] cellBindings;
    private Logger logger = LoggerFactory.getLogger(BoardViewController.class);

    public void setBoardDimensions(int rowSize, int columnSize, Density density) {
        this.gameBoard = new GameOfLifeBoard(rowSize, columnSize, new PlainGameOfLifeSimulator());
        this.cellBindings = new JavaBeanBooleanProperty[rowSize][columnSize];

        // Wypełnij planszę zgodnie z gęstością
        density.fillBoard(gameBoard);

        // Narysuj planszę
        drawBoard();
        logger.debug("Drawing board with dimensions:{}x{} with density:{}", rowSize, columnSize, density);
    }

    private void drawBoard() {
        boardGrid.getChildren().clear(); // Czyszczenie siatki przed rysowaniem

        for (int y = 0; y < gameBoard.getHeight(); y++) {
            for (int x = 0; x < gameBoard.getWidth(); x++) {
                try {
                    // Tworzenie adaptera dla komórki na planszy
                    GameOfLifeCellAdapter cellAdapter = new GameOfLifeCellAdapter(gameBoard, x, y);

                    // Tworzenie JavaBeanBooleanProperty z adaptera
                    JavaBeanBooleanProperty cellBinding = JavaBeanBooleanPropertyBuilder.create()
                            .bean(cellAdapter)
                            .name("alive") // Właściwość "alive" zgodna z adapterem
                            .build();

                    // Tworzenie prostokąta reprezentującego komórkę
                    Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                    cell.setStroke(Color.GRAY);

                    // Inicjalne ustawienie koloru prostokąta zgodnie z początkowym stanem
                    cell.setFill(cellBinding.get() ? Color.web("#f87ccf") : Color.WHITE);

                    // Wiązanie koloru prostokąta z właściwością komórki
                    cellBinding.addListener((obs, oldValue, newValue) -> {
                        cell.setFill(newValue ? Color.web("#f87ccf") : Color.WHITE);
                    });

                    // Obsługa kliknięcia na komórkę
                    cell.setOnMouseClicked(event -> {
                        boolean newState = !cellBinding.get();
                        cellBinding.set(newState); // Zmiana stanu w modelu
                    });

                    // Dodanie prostokąta do siatki
                    boardGrid.add(cell, x, y);

                } catch (NoSuchMethodException e) {
                    logger.error("Could not draw board on cell {},{}", x, y);
                }
            }
        }
    }
    @FXML
    private void handleDoStep() {
        gameBoard.doSimulationStep();
        drawBoard();
        logger.debug("Handling doStep method");
    }

    @FXML
    private void handleLoad() {
        logger.debug("Initiated load handling");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game of Life Board");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Serialized Files", "*.ser"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try (FileGameOfLifeBoardDao dao = (FileGameOfLifeBoardDao) new GameOfLifeBoardDaoFactory().
                    createFile(selectedFile.getAbsolutePath())) {
                GameOfLifeBoard loadedBoard = dao.read();
                if (loadedBoard != null) {
                    this.gameBoard = loadedBoard;
                    drawBoard();
                }
            } catch (IOException e) {
                logger.error("IO Exception: could not load Game of Life Board");
            } catch (GameOfLifeDaoCloseException e) {
                logger.error("Game Of Life Dao Close Exception: could not close file resource while loading");
            } catch (GameOfLifeDaoIoException e) {
                logger.error("Game Of Life Dao IO Exception: could not read file");
            }
        }
    }

    @FXML
    private void handleSave() {
        logger.debug("Initiated save handling");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game of Life Board");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Serialized Files", "*.ser"));

        File selectedFile = fileChooser.showSaveDialog(new Stage());
        if (selectedFile != null) {
            try (FileGameOfLifeBoardDao dao = (FileGameOfLifeBoardDao) new GameOfLifeBoardDaoFactory().
                    createFile(selectedFile.getAbsolutePath())) {
                dao.write(gameBoard);
            } catch (IOException e) {
                logger.error("IO Exception: could not save Game of Life Board");
            } catch (GameOfLifeDaoCloseException e) {
                logger.error("Game Of Life Dao Close Exception: could not close file resource while saving");
            } catch (GameOfLifeDaoIoException e) {
                logger.error("Game Of Life Dao Close Exception: could not write file");
            }
        }
    }

    @FXML
    private void handleSaveSlot1(ActionEvent actionEvent) {
        saveToDatabase(1);
    }

    @FXML
    private void handleSaveSlot2(ActionEvent actionEvent) {
        saveToDatabase(2);
    }

    @FXML
    private void handleSaveSlot3(ActionEvent actionEvent) {
        saveToDatabase(3);
    }

    @FXML
    private void handleLoadSlot1(ActionEvent actionEvent) {
        loadFromDatabase(1);
    }

    @FXML
    private void handleLoadSlot2(ActionEvent actionEvent) {
        loadFromDatabase(2);
    }

    @FXML
    private void handleLoadSlot3(ActionEvent actionEvent) {
        loadFromDatabase(3);
    }

    private void saveToDatabase(int slotId) {
        String name = boardName.getText();
        if (name.isEmpty()) {
            logger.warn("Board name is empty, using default name");
            name = "Default Board";
        }

        try (JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao("jdbc:postgresql://localhost:5432/postgres",
                "postgres", "jakieshaslo", slotId, name)) {
            dao.write(gameBoard);
            logger.info("Saved board '{}' to database in slot {}", name, slotId);
        } catch (Exception e) {
            logger.error("Failed to save board '{}' to database in slot {}", name, slotId, e);
        }
    }

    private void loadFromDatabase(int slotId) {
        try (JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao("jdbc:postgresql://localhost:5432/postgres",
                "postgres", "jakieshaslo", slotId, "nazwa")) {
            GameOfLifeBoard loadedBoard = dao.read();
            if (loadedBoard != null) {
                try (Connection connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/postgres",
                        "postgres",
                        "jakieshaslo")) {
                    PreparedStatement nameStmt = connection.
                            prepareStatement("SELECT board_name FROM GameOfLifeBoard WHERE id = ?");
                    nameStmt.setInt(1, slotId);
                    ResultSet rs = nameStmt.executeQuery();
                    if (rs.next()) {
                        boardName.setText(rs.getString(1));
                    } else {
                        throw new LocalizedGameOfLifeRuntimeException("Didn't find board name in database");
                    }

                }
                this.gameBoard = loadedBoard;

                drawBoard();
                logger.info("Loaded board from database slot {}", slotId);
                logger.debug("Loaded board dimensions: {}x{}", gameBoard.getHeight(), gameBoard.getWidth());

            }
        } catch (Exception e) {
            logger.error("Failed to load board from database slot {}", slotId, e);
        }
    }


}
