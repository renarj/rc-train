package com.oberasoftware.train.api.commands;

import com.oberasoftware.train.api.TrainCommand;

/**
 * @author Renze de Vries
 */
public class TrainDirectionCommand implements TrainCommand {

    public enum DIRECTION {
        FORWARD,
        BACKWARD
    }

    private final String trainId;
    private final DIRECTION direction;

    public TrainDirectionCommand(String trainId, DIRECTION direction) {
        this.trainId = trainId;
        this.direction = direction;
    }

    public String getTrainId() {
        return trainId;
    }

    public DIRECTION getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "TrainDirectionCommand{" +
                "trainId=" + trainId +
                ", direction=" + direction +
                '}';
    }
}
