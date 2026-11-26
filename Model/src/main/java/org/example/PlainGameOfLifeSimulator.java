package org.example;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

public class PlainGameOfLifeSimulator implements GameOfLifeSimulator, Serializable {
    @Serial
    private static final long serialVersionUID = 4530245815412569651L;

    @Override
    public void doStep(GameOfLifeBoard board) {
        int boardWidth = board.getWidth();
        int boardHeight = board.getHeight();

        boolean[][] tempBoard = new boolean[boardHeight][boardWidth];

        //zdobywamy nextValue na podstawie tego, co oblicza sobie cell
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                tempBoard[i][j] = board.getNext(j, i);
            }
        }

        //kopiujemy tablice na boarda
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                board.set(j, i, tempBoard[i][j]);
            }
        }
    }

    @Serial
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }

}
