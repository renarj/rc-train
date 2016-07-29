package com.oberasoftware.train.controllers.ecos;

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
    private final List<EcosCommandParameter> parameters;

    public EcosCommand(int objectId, CommandType commandType, List<EcosCommandParameter> parameters) {
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

    public List<EcosCommandParameter> getParameters() {
        return parameters;
    }

    public String getCommand() {
        StringBuilder builder = new StringBuilder();
        builder.append(commandType.getName()).append("(");
        builder.append(objectId);

        for(EcosCommandParameter parameter : parameters) {
            builder.append(", ");
            builder.append(parameter.getParameter());
            if(!parameter.getAttributes().isEmpty()) {
                builder.append(getAttribute(parameter));
            }
        }
        builder.append(")");

        return builder.toString();
    }

    private String getAttribute(EcosCommandParameter parameter) {
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
