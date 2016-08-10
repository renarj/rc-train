package com.oberasoftware.train.controllers.ecos.converters;

import com.oberasoftware.home.api.converters.Converter;
import com.oberasoftware.home.api.converters.TypeConverter;
import com.oberasoftware.home.util.IntUtils;
import com.oberasoftware.train.api.commands.TrainDirectionCommand;
import com.oberasoftware.train.controllers.ecos.EcosCommand;
import com.oberasoftware.train.controllers.ecos.EcosCommandBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Renze de Vries
 */
@Component
public class TrainDirectionConverter implements Converter {
    @TypeConverter
    public EcosCommand convert(TrainDirectionCommand directionCommand) {
        int direction = directionCommand.getDirection() == TrainDirectionCommand.DIRECTION.FORWARD ? 0 : 1;
        Optional<Integer> trainId = IntUtils.toInt(directionCommand.getTrainId());
        if(trainId.isPresent()) {
            return EcosCommandBuilder.set(trainId.get())
                    .param("dir", direction)
                    .build();
        }

        return null;
    }
}
