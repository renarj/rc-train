package com.oberasoftware.train.controllers.ecos;

import com.oberasoftware.train.api.MessageParseException;
import com.oberasoftware.train.controllers.ecos.messages.EcosErrorMessage;
import com.oberasoftware.train.controllers.ecos.messages.EcosReceivedMessage;
import com.oberasoftware.train.controllers.ecos.messages.EcosReplyMessage;
import com.oberasoftware.train.controllers.ecos.messages.ReplyLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.oberasoftware.train.controllers.ecos.EcosCommand.parse;
import static com.oberasoftware.train.controllers.ecos.EcosCommand.parseParameters;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Component
public class EcosMessageParser {
    private static final Logger LOG = LoggerFactory.getLogger(EcosMessageParser.class);


    private static final Pattern HEADER_PATTERN = Pattern.compile("<REPLY (.*)>|<EVENT (.*)>");
    private static final Pattern FOOTER_PATTERN = Pattern.compile("<END (\\d+) \\((.*)\\)>");
    private static final Pattern LINE_PATTERN = Pattern.compile("(\\d+) (.*)");


    private static final int ALL_OK = 0;

    private String line;
    private EcosReceivedMessage message;

    private String headerLine;
    private String footerLine;
    private List<String> bodyLines;

    public Optional<EcosReceivedMessage> pushLine(String line) {
        this.line = line;
        if(HEADER_PATTERN.matcher(line).find()) {
            this.headerLine = line;
            bodyLines = new ArrayList<>();
        } else if(FOOTER_PATTERN.matcher(line).find()) {
            this.footerLine = line;
            return buildMessage();
        } else {
            bodyLines.add(line);
        }
        return Optional.empty();
    }

    private Optional<EcosReceivedMessage> buildMessage() {
        try {
            Matcher footerMatcher = FOOTER_PATTERN.matcher(footerLine);
            if (footerMatcher.find()) {
                int statusCode = Integer.parseInt(footerMatcher.group(1));
                String message = footerMatcher.group(2);
                if (statusCode == ALL_OK) {
                    if(headerLine.contains("<REPLY")) {
                        return of(buildReplyMessage());
                    } else if(headerLine.contains("<EVENT")) {
                        return ofNullable(buildEventMessage());
                    } else {
                        throw new MessageParseException("Unknown header was sent: " + headerLine);
                    }
                } else {
                    return of(buildErrorMessage(statusCode, message));
                }
            }
        } catch(MessageParseException e) {
            LOG.error("Could not parse received message, header: {} footer: {}", headerLine, footerLine);
        }
        return empty();
    }

    private EcosReceivedMessage buildReplyMessage() throws MessageParseException {
        return new EcosReplyMessage(getCommand().get(), getLines());
    }

    private EcosReceivedMessage buildEventMessage() throws MessageParseException {
        return null;
    }

    private EcosReceivedMessage buildErrorMessage(int status, String message) throws MessageParseException {
        try {
            return new EcosErrorMessage(status, getCommand(), message);
        } catch(MessageParseException e) {
            return new EcosErrorMessage(status, message);
        }
    }

    private List<ReplyLine> getLines() {
        List<ReplyLine> lines = new ArrayList<>();

        bodyLines.forEach(b -> {
            Matcher matcher = LINE_PATTERN.matcher(b);
            if(matcher.find()) {
                int objectId = Integer.parseInt(matcher.group(1));
                String params = matcher.group(2);
                try {
                    lines.add(new ReplyLine(objectId, parseParameters(params)));
                } catch (MessageParseException e) {
                    LOG.error("Could not parse body line: " + b, e);
                }
            }
        });

        return lines;
    }

    private Optional<EcosCommand> getCommand() throws MessageParseException {
        Matcher matcher = HEADER_PATTERN.matcher(headerLine);
        if(matcher.find()) {
            return ofNullable(parse(matcher.group(1)));
        } else {
            return empty();
        }
    }



    public String getLastLine() {
        return this.line;
    }
}
