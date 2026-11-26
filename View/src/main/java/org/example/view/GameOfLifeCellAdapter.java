package org.example.view;

import org.example.GameOfLifeBoard;

public class GameOfLifeCellAdapter {
    private final GameOfLifeBoard board;
    private final int x; // Współrzędna X komórki
    private final int y; // Współrzędna Y komórki

    // Konstruktor przyjmujący planszę i współrzędne komórki
    public GameOfLifeCellAdapter(GameOfLifeBoard board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
    }

    // Metoda JavaBeans zwracająca obecny stan komórki
    public boolean isAlive() {
        return board.get(x, y);
    }

    // Metoda JavaBeans ustawiająca nowy stan komórki
    public void setAlive(boolean alive) {
        board.set(x, y, alive);
    }
}
