package com.oberasoftware.train.controllers.ecos;

import com.oberasoftware.train.controllers.ecos.messages.EcosMessageParameter;

import java.util.List;

/**
 * @author Renze de Vries
 */
public class EcosCommand {

    public enum CommandType {
        REQUEST("request"),
        GET("get"),
        SET("set"),
        CREATE("create"),
        QUERYOBJECT("queryObjects");

        private String name;

        CommandType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private final int objectId;
    private final CommandType commandType;
    private final List<EcosMessageParameter> parameters;

    public EcosCommand(int objectId, CommandType commandType, List<EcosMessageParameter> parameters) {
        this.objectId = objectId;
        this.commandType = commandType;
        this.parameters = parameters;
    }

    public int getObjectId() {
        return objectId;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public List<EcosMessageParameter> getParameters() {
        return parameters;
    }

    public String getCommand() {
        StringBuilder builder = new StringBuilder();
        builder.append(commandType.getName()).append("(");
        builder.append(objectId);

        for(EcosMessageParameter parameter : parameters) {
            builder.append(", ");
            builder.append(parameter.getName());
            if(!parameter.getAttributes().isEmpty()) {
                builder.append(getAttribute(parameter));
            }
        }
        builder.append(")");

        return builder.toString();
    }

    private String getAttribute(EcosMessageParameter parameter) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        boolean first = true;
        for(Object attribute : parameter.getAttributes()) {
            if(!first) {
                builder.append(",");
            } else {
                first = false;
            }

            if(attribute instanceof String) {
                builder.append("\"");
                builder.append(attribute);
                builder.append("\"");
            } else {
                builder.append(attribute);
            }

        }
        builder.append("]");

        return builder.toString();
    }

    @Override
    public String toString() {
        return "EcosCommand{" +
                "command='" + getCommand() + '\'' +
                '}';
    }
}
