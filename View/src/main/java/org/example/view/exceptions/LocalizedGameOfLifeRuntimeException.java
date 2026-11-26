package org.example.view.exceptions;

import org.example.exceptions.GameOfLifeDaoCloseException;
import org.example.exceptions.GameOfLifeException;

import java.util.ResourceBundle;

public class LocalizedGameOfLifeRuntimeException extends RuntimeException {
    ResourceBundle bundle = ResourceBundle.getBundle("Language");

    public LocalizedGameOfLifeRuntimeException(Throwable cause) {
        super(cause);
    }

    public LocalizedGameOfLifeRuntimeException(String message) {
        super(message);
    }

    @Override
    public String getLocalizedMessage() {
        return bundle.getString("golRuntimeException");
    }
}
