package com.oberasoftware.train.controllers.ecos.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.api.commands.BasicCommand;
import com.oberasoftware.home.api.model.BasicCommandImpl;
import com.oberasoftware.home.core.mqtt.MQTTMessage;
import com.oberasoftware.home.core.mqtt.MQTTPath;
import com.oberasoftware.home.core.mqtt.MessageGroup;
import com.oberasoftware.train.api.ControllerManager;
import com.oberasoftware.train.api.TrainController;
import com.oberasoftware.train.api.commands.State;
import com.oberasoftware.train.api.commands.TrainFunctionCommand;
import com.oberasoftware.train.api.commands.TrainLightCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.oberasoftware.home.util.ConverterHelper.mapFromJson;

/**
 * @author Renze de Vries
 */
@Component
public class TrainFunctionHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(TrainFunctionHandler.class);

    @Autowired
    private ControllerManager controllerManager;

    @EventSubscribe
    @MQTTPath(group = MessageGroup.COMMANDS, device = "train", label = "light")
    public void receive(MQTTMessage mqttMessage) {
        LOG.info("Setting train light function: {} from topic: {}", mqttMessage.getMessage(), mqttMessage.getTopic());
        BasicCommand basicCommand = mapFromJson(mqttMessage.getMessage(), BasicCommandImpl.class);
        String trainId = basicCommand.getProperty("trainId");

        String state = basicCommand.getProperty("state");
        if(state != null) {
            TrainController trainController = controllerManager.getController(basicCommand.getControllerId());
            State targetState = state.equalsIgnoreCase("on") ? State.ON : State.OFF;
            trainController.publish(new TrainLightCommand(trainId, targetState));
        }
    }

    @EventSubscribe
    @MQTTPath(group = MessageGroup.COMMANDS, device = "train", label = "function")
    public void functionReceive(MQTTMessage mqttMessage) {
        LOG.info("Setting train function: {} from topic: {}", mqttMessage.getMessage(), mqttMessage.getTopic());
        BasicCommand basicCommand = mapFromJson(mqttMessage.getMessage(), BasicCommandImpl.class);
        String trainId = basicCommand.getProperty("trainId");
        String function = basicCommand.getProperty("function");
        String state = basicCommand.getProperty("state");
        TrainFunctionCommand.FUNCTION targetFunction = TrainFunctionCommand.FUNCTION.valueOf(function.toUpperCase());

        if(state != null) {
            TrainController trainController = controllerManager.getController(basicCommand.getControllerId());
            State targetState = state.equalsIgnoreCase("on") ? State.ON : State.OFF;
            trainController.publish(new TrainFunctionCommand(trainId, targetFunction, targetState));
        }
    }
}
