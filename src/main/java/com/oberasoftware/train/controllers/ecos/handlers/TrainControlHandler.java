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
import com.oberasoftware.train.api.commands.TrainControlCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.oberasoftware.home.util.ConverterHelper.mapFromJson;

/**
 * @author Renze de Vries
 */
@Component
public class TrainControlHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(TrainControlHandler.class);

    @Autowired
    private ControllerManager controllerManager;

    @EventSubscribe
    @MQTTPath(group = MessageGroup.COMMANDS, device = "train", label = "control")
    public void receive(MQTTMessage mqttMessage) {
        LOG.info("Requesting train control: {} from topic: {}", mqttMessage.getMessage(), mqttMessage.getTopic());
        BasicCommand basicCommand = mapFromJson(mqttMessage.getMessage(), BasicCommandImpl.class);
        String trainId = basicCommand.getProperty("trainId");

        TrainController trainController = controllerManager.getController(basicCommand.getControllerId());
        trainController.publish(new TrainControlCommand(trainId));
    }
}
