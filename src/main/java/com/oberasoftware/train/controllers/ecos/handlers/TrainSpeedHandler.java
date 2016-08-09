package com.oberasoftware.train.controllers.ecos.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.api.commands.BasicCommand;
import com.oberasoftware.home.api.model.BasicCommandImpl;
import com.oberasoftware.home.core.mqtt.MQTTMessage;
import com.oberasoftware.home.core.mqtt.MQTTPath;
import com.oberasoftware.home.core.mqtt.MessageGroup;
import com.oberasoftware.home.util.IntUtils;
import com.oberasoftware.train.api.ControllerManager;
import com.oberasoftware.train.api.TrainController;
import com.oberasoftware.train.api.commands.TrainSpeedCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.oberasoftware.home.util.ConverterHelper.mapFromJson;

/**
 * @author Renze de Vries
 */
@Component
public class TrainSpeedHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(TrainSpeedHandler.class);

    @Autowired
    private ControllerManager controllerManager;

    @EventSubscribe
    @MQTTPath(group = MessageGroup.COMMANDS, device = "train", label = "speed")
    public void receive(MQTTMessage mqttMessage) {
        LOG.info("Setting train speed: {} from topic: {}", mqttMessage.getMessage(), mqttMessage.getTopic());
        BasicCommand basicCommand = mapFromJson(mqttMessage.getMessage(), BasicCommandImpl.class);
        String trainId = basicCommand.getProperty("trainId");

        Optional<Integer> speed = IntUtils.toInt(basicCommand.getProperty("speed"));
        if(speed.isPresent()) {
            TrainController trainController = controllerManager.getController(basicCommand.getControllerId());
            trainController.publish(new TrainSpeedCommand(trainId, speed.get()));
        }
    }
}
