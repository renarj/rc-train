package com.oberasoftware.train.ecos;

import com.oberasoftware.train.controllers.ecos.EcosCommand;
import com.oberasoftware.train.controllers.ecos.EcosMessageParser;
import com.oberasoftware.train.controllers.ecos.messages.EcosErrorMessage;
import com.oberasoftware.train.controllers.ecos.messages.EcosReceivedMessage;
import com.oberasoftware.train.controllers.ecos.messages.EcosReplyMessage;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Renze de Vries
 */
public class EcosMessageParserTest {
    @Test
    public void testReplyMessage() {
        EcosMessageParser messageParser = new EcosMessageParser();
        assertThat(messageParser.pushLine("<REPLY queryObjects(10, name)>").isPresent(), is(false));
        assertThat(messageParser.pushLine("1000 name[\"ice\"]").isPresent(), is(false));
        assertThat(messageParser.pushLine("1004 name[\"NS 1858 H0\"]").isPresent(), is(false));
        Optional<EcosReceivedMessage> receivedMessageOptional = messageParser.pushLine("<END 0 (OK)>");
        assertThat(receivedMessageOptional.isPresent(),  is(true));

        EcosReceivedMessage receivedMessage = receivedMessageOptional.get();
        assertThat(receivedMessage, notNullValue());
        assertThat(receivedMessage, instanceOf(EcosReplyMessage.class));
        EcosReplyMessage replyMessage = (EcosReplyMessage) receivedMessage;
        assertThat(replyMessage.getCommand().getCommandType(), is(EcosCommand.CommandType.QUERYOBJECT));
        assertThat(replyMessage.getCommand().getObjectId(), is(10));

        assertThat(replyMessage.getReplies().size(), is(2));
        assertThat(replyMessage.getReplies().get(0).getObjectId(), is(1000));
        assertThat(replyMessage.getReplies().get(0).getParameters().size(), is(1));
        assertThat(replyMessage.getReplies().get(0).getParameters().get(0).getName(), is("name"));
        assertThat(replyMessage.getReplies().get(0).getAttributes("name").get(0), is("ice"));

        assertThat(replyMessage.getReplies().get(1).getObjectId(), is(1004));
        assertThat(replyMessage.getReplies().get(1).getParameters().size(), is(1));
        assertThat(replyMessage.getReplies().get(1).getParameters().get(0).getName(), is("name"));
        assertThat(replyMessage.getReplies().get(1).getAttributes("name").get(0), is("NS 1858 H0"));
    }

    @Test
    public void testErrorMessage() {
        EcosMessageParser messageParser = new EcosMessageParser();
        assertThat(messageParser.pushLine("<REPLY ?>").isPresent(), is(false));
        Optional<EcosReceivedMessage> receivedMessageOptional = messageParser.pushLine("<END 21 (syntax error, ')' expected at 18)>");
        assertThat(receivedMessageOptional.isPresent(), is(true));

        EcosReceivedMessage receivedMessage = receivedMessageOptional.get();
        assertThat(receivedMessage, notNullValue());
        assertThat(receivedMessage, instanceOf(EcosErrorMessage.class));
        EcosErrorMessage errorMessage = (EcosErrorMessage) receivedMessage;
        assertThat(errorMessage.getCommand().isPresent(), is(false));
        assertThat(errorMessage.getErrorCode(), is(21));
        assertThat(errorMessage.getErrorMessage(), is("syntax error, ')' expected at 18"));

    }
}
