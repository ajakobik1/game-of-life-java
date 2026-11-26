package org.example;

import java.io.*;

public class GameOfLifeBoardDaoFactory implements DaoFactory<GameOfLifeBoard> {

    private GameOfLifeBoard prototype;

    public Dao<GameOfLifeBoard> createFile(String fileName) throws IOException {
        return new FileGameOfLifeBoardDao(fileName);
    }

    public GameOfLifeBoardDaoFactory(int width, int height) {
        this.prototype = new GameOfLifeBoard(height, width, new PlainGameOfLifeSimulator());
    }

    public GameOfLifeBoardDaoFactory() {
        this.prototype = null;
    }

    public GameOfLifeBoard createInstance() {
        return prototype.clone();
    }
}

