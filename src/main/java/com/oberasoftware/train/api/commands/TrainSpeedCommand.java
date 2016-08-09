package com.oberasoftware.train.api.commands;

import com.oberasoftware.train.api.TrainCommand;

/**
 * @author Renze de Vries
 */
public class TrainSpeedCommand implements TrainCommand {
    private final String trainId;
    private final int speed;

    public TrainSpeedCommand(String trainId, int speed) {
        this.trainId = trainId;
        this.speed = speed;
    }

    public String getTrainId() {
        return trainId;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return "TrainSpeedCommand{" +
                "trainId='" + trainId + '\'' +
                ", speed=" + speed +
                '}';
    }
}
