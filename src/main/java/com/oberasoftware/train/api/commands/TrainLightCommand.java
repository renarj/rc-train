package com.oberasoftware.train.api.commands;

import com.oberasoftware.train.api.TrainCommand;

/**
 * @author Renze de Vries
 */
public class TrainLightCommand implements TrainCommand {
    private final String trainId;
    private final State state;

    public TrainLightCommand(String trainId, State state) {
        this.trainId = trainId;
        this.state = state;
    }

    public String getTrainId() {
        return trainId;
    }

    public State getState() {
        return state;
    }

    @Override
    public String toString() {
        return "TrainLightCommand{" +
                "trainId='" + trainId + '\'' +
                ", state=" + state +
                '}';
    }
}
