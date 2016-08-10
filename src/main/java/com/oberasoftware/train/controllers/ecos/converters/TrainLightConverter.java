package com.oberasoftware.train.controllers.ecos.converters;

import com.oberasoftware.home.api.converters.Converter;
import com.oberasoftware.home.api.converters.TypeConverter;
import com.oberasoftware.home.util.IntUtils;
import com.oberasoftware.train.api.commands.State;
import com.oberasoftware.train.api.commands.TrainLightCommand;
import com.oberasoftware.train.controllers.ecos.EcosCommand;
import com.oberasoftware.train.controllers.ecos.EcosCommandBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Renze de Vries
 */
@Component
public class TrainLightConverter implements Converter {
    @TypeConverter
    public EcosCommand conver(TrainLightCommand lightCommand) {
        int state = lightCommand.getState() == State.ON ? 1 : 0;
        Optional<Integer> trainId = IntUtils.toInt(lightCommand.getTrainId());
        if(trainId.isPresent()) {
            return EcosCommandBuilder.set(trainId.get())
                    .param("func", 0, state)
                    .build();
        }

        return null;
    }
}
