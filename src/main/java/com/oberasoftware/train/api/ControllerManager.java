package com.oberasoftware.train.api;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface ControllerManager {
    List<TrainController> getControllers();

    TrainController getController(String controllerId);

    void register(TrainController trainController);
}
