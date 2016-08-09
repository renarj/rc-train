package com.oberasoftware.train.controllers.ecos.converters;

import com.oberasoftware.home.api.converters.Converter;
import com.oberasoftware.home.api.converters.TypeConverter;
import com.oberasoftware.home.util.IntUtils;
import com.oberasoftware.train.api.commands.TrainSpeedCommand;
import com.oberasoftware.train.controllers.ecos.EcosCommand;
import com.oberasoftware.train.controllers.ecos.EcosCommandBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Renze de Vries
 */
@Component
public class TrainSpeedConverter implements Converter {
    private static final Logger LOG = LoggerFactory.getLogger(TrainSpeedConverter.class);

    @TypeConverter
    public EcosCommand convert(TrainSpeedCommand trainSpeedCommand) {
        Optional<Integer> trainId = IntUtils.toInt(trainSpeedCommand.getTrainId());

        if(trainId.isPresent()) {
            LOG.info("Setting train: {} speed to: {}", trainId.get(), trainSpeedCommand.getSpeed());
            return EcosCommandBuilder.set(trainId.get())
                    .param("speed", trainSpeedCommand.getSpeed())
                    .build();
        } else {
            LOG.warn("Train speed command given but no trainId specified: {}", trainSpeedCommand);
            return null;
        }
    }
}
