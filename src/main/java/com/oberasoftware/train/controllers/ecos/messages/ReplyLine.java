package com.oberasoftware.train.controllers.ecos.messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Renze de Vries
 */
public class ReplyLine {
    private final int objectId;
    private final List<EcosMessageParameter> parameters;

    public ReplyLine(int objectId, List<EcosMessageParameter> parameters) {
        this.objectId = objectId;
        this.parameters = parameters;
    }

    public int getObjectId() {
        return objectId;
    }

    public List<EcosMessageParameter> getParameters() {
        return parameters;
    }

    public List<Object> getAttributes(String paramName) {
        Optional<EcosMessageParameter> parameter = this.parameters.stream().filter(p -> p.getName().equalsIgnoreCase(paramName)).findFirst();
        if(parameter.isPresent()) {
            return parameter.get().getAttributes();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public String toString() {
        return "ReplyLine{" +
                "objectId=" + objectId +
                ", parameters=" + parameters +
                '}';
    }
}
