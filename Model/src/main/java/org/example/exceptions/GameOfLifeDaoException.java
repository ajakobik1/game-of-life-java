package org.example.exceptions;

public class GameOfLifeDaoException extends GameOfLifeException {
    public GameOfLifeDaoException(Throwable cause) {
        super(cause);
    }

    public GameOfLifeDaoException(String message) {
        super(message);
    }
}
