package com.oberasoftware.train.controllers.ecos.converters;

import com.oberasoftware.home.api.converters.Converter;
import com.oberasoftware.home.api.converters.TypeConverter;
import com.oberasoftware.home.util.IntUtils;
import com.oberasoftware.train.api.commands.State;
import com.oberasoftware.train.api.commands.TrainFunctionCommand;
import com.oberasoftware.train.controllers.ecos.EcosCommand;
import com.oberasoftware.train.controllers.ecos.EcosCommandBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Renze de Vries
 */
@Component
public class TrainFunctionConverter implements Converter {
    @TypeConverter
    public EcosCommand convert(TrainFunctionCommand functionCommand) {
        int state = functionCommand.getState() == State.ON ? 1 : 0;

        Optional<Integer> trainId = IntUtils.toInt(functionCommand.getTrainId());
        if(trainId.isPresent()) {
            return EcosCommandBuilder.set(trainId.get())
                    .param("func", getFunctionId(functionCommand.getFunction()), state)
                    .build();
        }

        return null;
    }

    private int getFunctionId(TrainFunctionCommand.FUNCTION function) {
        switch(function) {
            case SOUND:
                return 1;
            case LIGHT:
            default:
                return 0;
        }
    }

}
