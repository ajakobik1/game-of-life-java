package org.example.view.exceptions;

import org.example.exceptions.GameOfLifeDaoException;

import java.util.ResourceBundle;

public class LocalizedGameOfLifeDaoException extends GameOfLifeDaoException {
    ResourceBundle bundle = ResourceBundle.getBundle("Language");

    public LocalizedGameOfLifeDaoException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getLocalizedMessage() {
        return bundle.getString("golDaoException");
    }
}
