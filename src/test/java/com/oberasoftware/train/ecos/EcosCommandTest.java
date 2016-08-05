package com.oberasoftware.train.ecos;

import com.oberasoftware.train.controllers.ecos.EcosCommand;
import com.oberasoftware.train.controllers.ecos.EcosCommandBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
}
