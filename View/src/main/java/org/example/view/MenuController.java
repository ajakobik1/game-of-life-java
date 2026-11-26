package org.example.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.example.view.exceptions.LocalizedGameOfLifeIllegalArgumentException;
import org.example.view.exceptions.LocalizedGameOfLifeRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MenuController {
    public ChoiceBox<Language> lang;
    @FXML
    private ChoiceBox<Density> density;
    @FXML
    public TextField columns;
    @FXML
    public TextField rows;

    private ResourceBundle resources = ResourceBundle.getBundle("language_en", Locale.getDefault());
    private Logger logger = LoggerFactory.getLogger(MenuController.class);

    @FXML
    private void initialize() {
        logger.debug("Initializing Menu");
        // Dodajemy wartości z Enum do ChoiceBox
        density.setItems(FXCollections.observableArrayList(Density.values()));
        lang.setItems(FXCollections.observableArrayList(Language.values()));

        // Walidacja liczb w polach tekstowych
        TextFormatter<Integer> integerFormatterRows = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {  // Dopasowanie tylko liczb
                return change;
            }
            return null;
        });
        rows.setTextFormatter(integerFormatterRows); // Ustawiamy formatter tylko dla rows

        TextFormatter<Integer> integerFormatterColumns = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {  // Dopasowanie tylko liczb
                return change;
            }
            return null;
        });
        columns.setTextFormatter(integerFormatterColumns); // Ustawiamy formatter tylko dla columns
    }

    @FXML
    private void handleButtonAction() {
        try {
            logger.debug("Handling button action");
            // Pobierz dane od użytkownika
            int rowSize = Integer.parseInt(rows.getText());
            int columnSize = Integer.parseInt(columns.getText());
            Density selectedDensity = density.getValue();

            // Walidacja danych
            if (rowSize < 4 || rowSize > 20 || columnSize < 4 || columnSize > 20) {
                throw new LocalizedGameOfLifeIllegalArgumentException(
                        resources.getString("buttonActionBoardSizeMessage"));
            }
            if (selectedDensity == null) {
                throw new LocalizedGameOfLifeIllegalArgumentException(
                        resources.getString("buttonActionDensityMessage"));
            }

            // Załaduj plik BoardView.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BoardView.fxml"), resources);
            Scene scene = new Scene(fxmlLoader.load());

            // Pobierz kontroler BoardViewController
            BoardViewController controller = fxmlLoader.getController();

            // Przekaż wymiary i gęstość do kontrolera
            controller.setBoardDimensions(rowSize, columnSize, selectedDensity);

            // Ustaw nową scenę
            Stage stage = (Stage) Stage.getWindows().filtered(window -> window.isFocused()).get(0);
            stage.setScene(scene);
            stage.setTitle("GameOfLifeSimulation");

        } catch (NumberFormatException e) {
            logger.error("Parsing integers failed while handling button action");
        } catch (IllegalArgumentException e) {
            logger.warn("Wrong arguments were passed while handling button action");
        } catch (IOException e) {
            logger.error("IO Exception encountered while handling button action, throwing Runtime Exception");
            throw new LocalizedGameOfLifeRuntimeException(e);
        }
    }

    @FXML
    private void handleLanguageChange() {
        try {
            logger.debug("Initiated language change.");
            // Tworzymy nowy locale i ustawiamy go wraz z bundle
            Locale locale = new Locale.Builder().
                    setLanguageTag(lang.getValue().getLanguage()).
                    setRegion(lang.getValue().getCountry()).
                    build();
            Language selectedLanguage = lang.getValue();
            this.resources = ResourceBundle.getBundle(selectedLanguage.getPath(), locale);

            // Załaduj plik BoardView.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Menu.fxml"), resources);
            Scene scene = new Scene(fxmlLoader.load(), 370, 300);
            // Ustawiamy bundle kontrolera na wybrany
            MenuController controller = fxmlLoader.getController();
            controller.setBundle(resources);

            // Ustaw nową scenę
            Stage stage = (Stage) Stage.getWindows().filtered(window -> window.isFocused()).get(0);
            stage.setScene(scene);

            logger.info("Changed language to {}", selectedLanguage);
        } catch (IOException e) {
            logger.error("IO exception while changing language");
        }
    }

    private void setBundle(ResourceBundle resources) {
        logger.debug("Setting resource bundle");
        this.resources = resources;
    }
}