package com.oberasoftware.train.api;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;

/**
 * @author Renze de Vries
 */
public class MessageParseException extends HomeAutomationException {
    public MessageParseException(String message, Throwable e) {
        super(message, e);
    }

    public MessageParseException(String message) {
        super(message);
    }
}
