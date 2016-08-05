package com.oberasoftware.train.controllers.ecos;

import com.oberasoftware.train.controllers.ecos.messages.EcosReceivedMessage;
import org.springframework.stereotype.Component;

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

    private static final Pattern HEADER_PATTERN = Pattern.compile("");
    private static final Pattern FOOTER_PATTERN = Pattern.compile("");

    public boolean pushLine(String line) {
        return false;
    }

    public EcosReceivedMessage getLastMessage() {
        return null;
    }
}
