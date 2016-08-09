package com.oberasoftware.train.controllers;

import com.oberasoftware.train.api.ControllerManager;
import com.oberasoftware.train.api.TrainController;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Renze de Vries
 */
@Component
public class DefaultControllerManager implements ControllerManager {

    private Map<String, TrainController> trainControllerMap = new ConcurrentHashMap<>();

    @Override
    public List<TrainController> getControllers() {
        return new ArrayList<>(trainControllerMap.values());
    }

    @Override
    public TrainController getController(String controllerId) {
        return trainControllerMap.get(controllerId);
    }

    @Override
    public void register(TrainController trainController) {
        trainControllerMap.put(trainController.getControllerId(), trainController);
    }
}
