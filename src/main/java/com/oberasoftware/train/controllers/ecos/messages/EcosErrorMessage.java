package com.oberasoftware.train.controllers.ecos.messages;

import com.oberasoftware.train.controllers.ecos.EcosCommand;

import java.util.Optional;

/**
 * @author Renze de Vries
 */
public class EcosErrorMessage implements EcosReceivedMessage {
    private final int errorCode;
    private final Optional<EcosCommand> command;
    private final String errorMessage;

    public EcosErrorMessage(int errorCode, Optional<EcosCommand> command, String errorMessage) {
        this.errorCode = errorCode;
        this.command = command;
        this.errorMessage = errorMessage;
    }

    public EcosErrorMessage(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.command = Optional.empty();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Optional<EcosCommand> getCommand() {
        return command;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "EcosErrorMessage{" +
                "errorCode=" + errorCode +
                ", command=" + command +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
