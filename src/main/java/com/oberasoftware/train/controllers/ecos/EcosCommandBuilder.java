package com.oberasoftware.train.controllers.ecos;

import com.oberasoftware.train.controllers.ecos.messages.EcosMessageParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Renze de Vries
 */
public class EcosCommandBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(EcosCommandBuilder.class);


    private final int objectId;
    private final EcosCommand.CommandType commandType;

    private final List<EcosMessageParameter> parameters = new ArrayList<>();

    private EcosCommandBuilder(EcosCommand.CommandType commandType, int objectId) {
        this.commandType = commandType;
        this.objectId = objectId;
    }

    public static EcosCommandBuilder request(int objectId) {
        return new EcosCommandBuilder(EcosCommand.CommandType.REQUEST, objectId);
    }

    public static EcosCommandBuilder get(int objectId) {
        return new EcosCommandBuilder(EcosCommand.CommandType.GET, objectId);
    }

    public static EcosCommandBuilder set(int objectId) {
        return new EcosCommandBuilder(EcosCommand.CommandType.SET, objectId);
    }

    public static EcosCommandBuilder create(int objectId) {
        return new EcosCommandBuilder(EcosCommand.CommandType.CREATE, objectId);
    }

    public static EcosCommandBuilder query(int objectId) {
        return new EcosCommandBuilder(EcosCommand.CommandType.QUERYOBJECT, objectId);
    }

    public EcosCommandBuilder param(String name, Object... attributes) {
        this.parameters.add(new EcosMessageParameter(name, newArrayList(attributes)));
        return this;
    }

    public EcosCommand build() {
        EcosCommand command = new EcosCommand(objectId, commandType, parameters);
        LOG.info("Created command: {}", command.getCommand());
        return command;
    }
}
