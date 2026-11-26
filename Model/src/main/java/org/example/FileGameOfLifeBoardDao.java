package org.example;

import org.example.exceptions.GameOfLifeDaoCloseException;
import org.example.exceptions.GameOfLifeDaoIoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileGameOfLifeBoardDao implements Dao<GameOfLifeBoard> {

    private String fileName;
    private Logger logger;

    @Override
    public void close() throws GameOfLifeDaoCloseException {
        logger.info("Closing resource FileGameOfLifeBoardDao");
    }

    @Override
    public GameOfLifeBoard read() throws GameOfLifeDaoIoException {
        GameOfLifeBoard obj = null;
        try {
            logger.debug("Opening file to read:{}", fileName);
            FileInputStream file = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(file);

            obj = (GameOfLifeBoard)in.readObject();

            in.close();
            file.close();
            logger.info("Successfully read file: {}", fileName);
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Error while reading file {}: {}", fileName, e.getMessage());
            throw new GameOfLifeDaoIoException(e);
        }

        return obj;
    }

    @Override
    public void write(GameOfLifeBoard obj) throws GameOfLifeDaoIoException {
        try {
            logger.debug("Opening file to write:{}", fileName);
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(obj);

            out.close();
            file.close();
            logger.info("Successfully wrote file: {}", fileName);
        } catch (IOException e) {
            logger.error("IOException while writing file {}", fileName, e);
            throw new GameOfLifeDaoIoException(e);
        }
    }

    public FileGameOfLifeBoardDao(String fileName) throws IOException {
        if (fileName == null) {
            logger.warn("File name is null");
            throw new NullPointerException("fileName is null");
        }
        this.fileName = fileName;
        this.logger = LoggerFactory.getLogger(FileGameOfLifeBoardDao.class);
        logger.info("Successfully created file: {}", fileName);
    }
}
