package com.oberasoftware.train.api.commands;

import com.oberasoftware.train.api.TrainCommand;

/**
 * @author Renze de Vries
 */
public class TrainFunctionCommand implements TrainCommand {
    public enum FUNCTION {
        SOUND,
        LIGHT
    }

    private final String trainId;
    private final FUNCTION function;
    private final State state;

    public TrainFunctionCommand(String trainId, FUNCTION function, State state) {
        this.trainId = trainId;
        this.function = function;
        this.state = state;
    }

    public String getTrainId() {
        return trainId;
    }

    public FUNCTION getFunction() {
        return function;
    }

    public State getState() {
        return state;
    }

    @Override
    public String toString() {
        return "TrainFunctionCommand{" +
                "trainId='" + trainId + '\'' +
                ", function=" + function +
                ", state=" + state +
                '}';
    }
}
