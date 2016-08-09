package com.oberasoftware.train.api;

/**
 * @author Renze de Vries
 */
public interface TrainController {
    String getControllerId();

    void connect() throws ConnectionException;

    void disconnect() throws ConnectionException;

    void publish(TrainCommand command);
}
