package org.example.exceptions;

public abstract class GameOfLifeException extends Exception {
    public GameOfLifeException(Throwable cause) {
        super(cause);
    }

    public GameOfLifeException(String message) {
        super(message);
    }
}
