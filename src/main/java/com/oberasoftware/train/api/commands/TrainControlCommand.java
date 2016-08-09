package com.oberasoftware.train.api.commands;

import com.oberasoftware.train.api.TrainCommand;

/**
 * @author Renze de Vries
 */
public class TrainControlCommand implements TrainCommand {
    private final String trainId;

    public TrainControlCommand(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainId() {
        return trainId;
    }

    @Override
    public String toString() {
        return "TrainControlCommand{" +
                "trainId='" + trainId + '\'' +
                '}';
    }
}
