package org.example.view.exceptions;

import org.example.exceptions.GameOfLifeException;
import java.util.ResourceBundle;

public class LocalizedGameOfLifeException extends GameOfLifeException {
    ResourceBundle bundle = ResourceBundle.getBundle("Language");

    public LocalizedGameOfLifeException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getLocalizedMessage() {
        return bundle.getString("golException");
    }
}
