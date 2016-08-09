package com.oberasoftware.train.controllers.ecos.converters;

import com.oberasoftware.home.api.converters.Converter;
import com.oberasoftware.home.api.converters.TypeConverter;
import com.oberasoftware.home.util.IntUtils;
import com.oberasoftware.train.api.commands.TrainControlCommand;
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
public class TrainControlConverter implements Converter {
    private static final Logger LOG = LoggerFactory.getLogger(TrainControlConverter.class);

    @TypeConverter
    public EcosCommand convert(TrainControlCommand command) {
        Optional<Integer> trainId = IntUtils.toInt(command.getTrainId());
        if(trainId.isPresent()) {
            LOG.info("Obtaining train control for train: {}", trainId.get());
            return EcosCommandBuilder.request(trainId.get())
                    .param("control")
                    .param("force")
                    .build();
        } else {
            LOG.warn("Train control command given but no trainId specified: {}", command);
            return null;
        }
    }
}
