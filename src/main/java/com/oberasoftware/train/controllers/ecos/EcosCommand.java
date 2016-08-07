package com.oberasoftware.train.controllers.ecos;

import com.oberasoftware.train.api.MessageParseException;
import com.oberasoftware.train.controllers.ecos.messages.EcosMessageParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.oberasoftware.train.controllers.ecos.EcosCommand.CommandType.fromName;

/**
 * @author Renze de Vries
 */
public class EcosCommand {
    private static final Logger LOG = LoggerFactory.getLogger(EcosCommand.class);

    private static final Pattern COMMAND_PATTERN = Pattern.compile("(.*)\\((\\d+), (.*)\\)");

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

        public static CommandType fromName(String name) {
            for(CommandType c : values()) {
                if(c.getName().equalsIgnoreCase(name)) {
                    return c;
                }
            }
            return null;
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

    public static EcosCommand parse(String command) throws MessageParseException {
        LOG.debug("Parsing ecos command: {}", command);
        Matcher matcher = COMMAND_PATTERN.matcher(command);
        if(matcher.find()) {
            CommandType commandType = fromName(matcher.group(1));
            int objectId = Integer.parseInt(matcher.group(2));
            List<EcosMessageParameter> params = parseParameters(matcher.group(3));

            return new EcosCommand(objectId, commandType, params);
        } else {
            throw new MessageParseException("Could not parse command: " + command);
        }

    }

    public static List<EcosMessageParameter> parseParameters(String parameters) throws MessageParseException {
        List<EcosMessageParameter> params = new ArrayList<>();
        for(String param : parameters.split(",")) {
            if(param.contains("[") && param.contains("]")) {
                String name = param.substring(0, param.indexOf("[")).trim();
                String attribute = param.substring(param.indexOf("[") + 1, param.length() - 1);
                params.add(new EcosMessageParameter(name, toAttribute(attribute)));
            } else {
                params.add(new EcosMessageParameter(param.trim()));
            }
        }

        return params;
    }

    private static List<Object> toAttribute(String attributes) {
        List<Object> attribs = new ArrayList<>();
        for(String attribute : attributes.split(",")) {
            if(attribute.startsWith("\"")) {
                attribs.add(attribute.substring(1, attribute.length() -1));
            } else {
                attribs.add(Integer.parseInt(attribute.trim()));
            }
        }
        return attribs;
    }

    @Override
    public String toString() {
        return "EcosCommand{" +
                "command='" + getCommand() + '\'' +
                '}';
    }
}
