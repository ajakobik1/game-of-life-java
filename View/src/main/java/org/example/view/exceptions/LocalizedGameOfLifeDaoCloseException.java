package org.example.view.exceptions;

import org.example.exceptions.GameOfLifeDaoCloseException;

import java.util.ResourceBundle;

public class LocalizedGameOfLifeDaoCloseException extends GameOfLifeDaoCloseException {
    ResourceBundle bundle = ResourceBundle.getBundle("Language");

    public LocalizedGameOfLifeDaoCloseException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getLocalizedMessage() {
        return bundle.getString("golDaoCloseException");
    }
}
