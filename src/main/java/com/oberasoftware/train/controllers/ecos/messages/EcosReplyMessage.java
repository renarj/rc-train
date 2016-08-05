package com.oberasoftware.train.controllers.ecos.messages;

import com.oberasoftware.train.controllers.ecos.EcosCommand;

import java.util.List;

/**
 * @author Renze de Vries
 */
public class EcosReplyMessage implements EcosReceivedMessage {
    private final EcosCommand command;
    private final List<ReplyLine> replies;

    public EcosReplyMessage(EcosCommand command, List<ReplyLine> replies) {
        this.command = command;
        this.replies = replies;
    }

    public EcosCommand getCommand() {
        return command;
    }

    public List<ReplyLine> getReplies() {
        return replies;
    }

    @Override
    public String toString() {
        return "EcosReplyMessage{" +
                "command=" + command +
                ", replies=" + replies +
                '}';
    }
}
