package org.example;

import java.io.*;

public class GameOfLifeNeighbors implements Serializable, Cloneable {

    @Serial
    private static final long serialVersionUID = 5790639202433008168L;
    private GameOfLifeBoard board;

    public int countAliveNeighbors(int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (board.get(x + i, y + j)) {
                    count++;
                }
            }
        }
        return count;
    }

    public GameOfLifeNeighbors(GameOfLifeBoard board) {
        this.board = board;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(board);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        board = (GameOfLifeBoard) in.readObject();
    }


    @Override
    public GameOfLifeNeighbors clone() {
        try {
            //Serializacja obiektu
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(byteOutputStream);
            output.writeObject(this);

            //Deserializacja
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteOutputStream.toByteArray());
            ObjectInputStream input = new ObjectInputStream(byteInputStream);
            return (GameOfLifeNeighbors) input.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
