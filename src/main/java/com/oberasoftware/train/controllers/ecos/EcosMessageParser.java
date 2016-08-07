package com.oberasoftware.train.controllers.ecos;

import com.oberasoftware.train.controllers.ecos.messages.EcosReceivedMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Renze de Vries
 *
 * <EVENT 1005>
1005 speed[44]
1005 speedstep[44]
<END 0 (OK)>
 *
 */
@Component
public class EcosMessageParser {

    private static final Pattern HEADER_PATTERN = Pattern.compile("<REPLY.*");
    private static final Pattern FOOTER_PATTERN = Pattern.compile("<END.*");

    private String line;
    private EcosReceivedMessage message;

    private String headerLine;
    private String footerLine;
    private List<String> bodyLines;


    public boolean pushLine(String line) {
        this.line = line;
        if(HEADER_PATTERN.matcher(line).find()) {
            this.headerLine = line;
            bodyLines = new ArrayList<>();
        } else if(FOOTER_PATTERN.matcher(line).find()) {
            this.footerLine = line;
            return true;
        } else {
            bodyLines.add(line);
        }

        return false;
    }

    public String getLastLine() {
        return this.line;
    }

    public EcosReceivedMessage getLastMessage() {
        return message;
    }
}
