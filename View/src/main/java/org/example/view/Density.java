package org.example.view;
import org.example.GameOfLifeBoard;

import java.util.Random;

public enum Density {
    LOW(0.1, "10%"),    // 10% komórek
    MEDIUM(0.3, "30%"), // 30% komórek
    HIGH(0.5, "50%");   // 50% komórek

    private final double percentage;
    private final String displayName; // Tekstowa reprezentacja procentu

    // Konstruktor do ustawienia procentu
    Density(double percentage, String displayName) {
        this.percentage = percentage;
        this.displayName = displayName;
    }

    // Metoda do wypełnienia planszy
    public void fillBoard(GameOfLifeBoard board) {
        Random rand = new Random();
        int height = board.getHeight();
        int width = board.getWidth();
        int cellsToFill = (int) (height * width * percentage); // Oblicz liczbę komórek do wypełnienia

        int filledCells = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board.set(j, i, false);
            }
        }

        while (filledCells < cellsToFill) {
            int randomRow = rand.nextInt(height);
            int randomCol = rand.nextInt(width);

            // Sprawdź, czy komórka jest już żywa
            if (!board.get(randomCol, randomRow)) {
                board.set(randomCol, randomRow, true); // Ustaw żywą komórkę
                filledCells++; // Zwiększ licznik wypełnionych komórek
            }
        }
    }

    // Przeciążenie metody toString dla przyjaznych nazw
    @Override
    public String toString() {
        return displayName;
    }

    public double getPercentage() {
        return percentage;
    }
}
