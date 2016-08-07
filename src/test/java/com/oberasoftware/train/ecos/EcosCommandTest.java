package com.oberasoftware.train.ecos;

import com.oberasoftware.train.api.MessageParseException;
import com.oberasoftware.train.controllers.ecos.EcosCommand;
import com.oberasoftware.train.controllers.ecos.EcosCommandBuilder;
import com.oberasoftware.train.controllers.ecos.messages.EcosMessageParameter;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Renze de Vries
 */
public class EcosCommandTest {
    @Test
    public void createRequestCommand() {
        EcosCommand command = EcosCommandBuilder.request(1001)
                .param("control").param("force").build();
        assertEquals("request(1001, control, force)", command.getCommand());
    }

    @Test
    public void setSpeedCommand() {
        EcosCommand command = EcosCommandBuilder.set(1001)
                .param("speed", 100).build();
        assertEquals("set(1001, speed[100])", command.getCommand());
    }

    @Test
    public void queryTrains() {
        EcosCommand command = EcosCommandBuilder.query(10)
                .param("name").param("locodesc").build();
        assertEquals("queryObjects(10, name, locodesc)", command.getCommand());
    }

    @Test
    public void regExTest() {
        Pattern test = Pattern.compile("[, ]*(.+?)\\[(.+?)\\]");
        Matcher m1 = test.matcher("func[0]");
        assertThat(m1.find(), is(true));
        assertThat(m1.group(1), is("func"));
        assertThat(m1.group(2), is("0"));

        Matcher m2 = test.matcher("func[0], param2[0, 100]");
        assertThat(m2.find(), is(true));
        assertThat(m2.group(1), is("func"));
        assertThat(m2.group(2), is("0"));
        assertThat(m2.find(), is(true));
        assertThat(m2.group(1), is("param2"));
        assertThat(m2.group(2), is("0, 100"));
    }

    @Test
    public void parseCommandSingleParamNoAttribs() throws MessageParseException {
        EcosCommand command = EcosCommand.parse("get(1, control)");
        assertThat(command.getObjectId(), is(1));
        assertThat(command.getCommandType(), is(EcosCommand.CommandType.GET));
        assertThat(command.getParameters().size(), is(1));
        EcosMessageParameter param = command.getParameters().get(0);
        assertThat(param, notNullValue());
        assertThat(param.getName(), is("control"));
        assertThat(param.getAttributes().size(), is(0));

    }

    @Test
    public void parseCommandSingleParamWithOneAttrib() throws MessageParseException {
        EcosCommand command = EcosCommand.parse("set(1, status[1])");
        assertThat(command.getObjectId(), is(1));
        assertThat(command.getCommandType(), is(EcosCommand.CommandType.SET));

        assertThat(command.getParameters().size(), is(1));
        EcosMessageParameter param = command.getParameters().get(0);
        assertThat(param, notNullValue());
        assertThat(param.getName(), is("status"));
        assertThat(param.getAttributes().size(), is(1));
        assertThat(param.getAttributes().get(0), is(1));
    }

    @Test
    public void parseCommandMultipleParams() throws MessageParseException {
        EcosCommand command = EcosCommand.parse("request(1005, control, force)");
        assertThat(command.getObjectId(), is(1005));
        assertThat(command.getCommandType(), is(EcosCommand.CommandType.REQUEST));
        assertThat(command.getParameters().size(), is(2));
        EcosMessageParameter param1 = command.getParameters().get(0);
        assertThat(param1, notNullValue());
        assertThat(param1.getName(), is("control"));
        assertThat(param1.getAttributes().size(), is(0));

        EcosMessageParameter param2 = command.getParameters().get(1);
        assertThat(param2, notNullValue());
        assertThat(param2.getName(), is("force"));
        assertThat(param2.getAttributes().size(), is(0));
    }

    @Test
    public void parseCommandMultipleParamsAttribs() throws MessageParseException {
        EcosCommand command = EcosCommand.parse("queryObjects(10, name, loco-desc[10])");
        assertThat(command.getObjectId(), is(10));
        assertThat(command.getCommandType(), is(EcosCommand.CommandType.QUERYOBJECT));
        assertThat(command.getParameters().size(), is(2));
        EcosMessageParameter param1 = command.getParameters().get(0);
        assertThat(param1, notNullValue());
        assertThat(param1.getName(), is("name"));
        assertThat(param1.getAttributes().size(), is(0));

        EcosMessageParameter param2 = command.getParameters().get(1);
        assertThat(param2, notNullValue());
        assertThat(param2.getName(), is("loco-desc"));
        assertThat(param2.getAttributes().size(), is(1));
        assertThat(param2.getAttributes().get(0), is(10));
    }

    @Test
    public void parseAttributeString() throws MessageParseException {
        EcosCommand command = EcosCommand.parse("queryObjects(10, name[\"test\"])");
        assertThat(command.getObjectId(), is(10));
        assertThat(command.getCommandType(), is(EcosCommand.CommandType.QUERYOBJECT));
        assertThat(command.getParameters().size(), is(1));
        EcosMessageParameter param1 = command.getParameters().get(0);
        assertThat(param1, notNullValue());
        assertThat(param1.getName(), is("name"));
        assertThat(param1.getAttributes().size(), is(1));
        assertThat(param1.getAttributes().get(0), is("test"));
    }
}
