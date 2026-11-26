package org.example.exceptions;

public class GameOfLifeDaoIoException extends GameOfLifeDaoException {
    public GameOfLifeDaoIoException(Throwable cause) {
        super(cause);
    }

    public GameOfLifeDaoIoException(String message) {
        super(message);
    }
}
