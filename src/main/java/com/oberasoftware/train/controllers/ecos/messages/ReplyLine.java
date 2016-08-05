package com.oberasoftware.train.controllers.ecos.messages;

import java.util.List;

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

    @Override
    public String toString() {
        return "ReplyLine{" +
                "objectId=" + objectId +
                ", parameters=" + parameters +
                '}';
    }
}
