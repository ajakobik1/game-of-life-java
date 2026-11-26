package org.example.view.exceptions;

import java.util.ResourceBundle;

public class LocalizedGameOfLifeIllegalArgumentException extends LocalizedGameOfLifeRuntimeException {
    ResourceBundle bundle = ResourceBundle.getBundle("Language");

    public LocalizedGameOfLifeIllegalArgumentException(String message) {
        super(message);
    }

    public LocalizedGameOfLifeIllegalArgumentException(Throwable cause) {
        super(cause);
    }



    @Override
    public String getLocalizedMessage() {
        return bundle.getString("golIllegalArgumentException");
    }
}
