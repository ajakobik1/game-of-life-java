package org.example.view.exceptions;

import org.example.exceptions.GameOfLifeDaoIoException;

import java.util.ResourceBundle;

public class LocalizedGameOfLifeDaoIoException extends GameOfLifeDaoIoException {
    ResourceBundle bundle = ResourceBundle.getBundle("Language");

    public LocalizedGameOfLifeDaoIoException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getLocalizedMessage() {
        return bundle.getString("golDaoIOException");
    }
}
