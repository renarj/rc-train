package com.oberasoftware.train.controllers.ecos;


import com.oberasoftware.home.api.converters.ConverterManager;
import com.oberasoftware.train.api.ConnectionException;
import com.oberasoftware.train.api.TrainCommand;
import com.oberasoftware.train.api.TrainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Socket;

@Component
public class EcosTrainController implements TrainController {
    private static final Logger LOG = LoggerFactory.getLogger(EcosTrainController.class);

	private Socket ecosSocket;

    @Value("${ecos.host}")
	private String hostName;

    @Value("${ecos.port}")
	private int portNumber;

    @Value("${controllerId:ecos}")
    private String controllerId;

    @Autowired
    private EcosProtocolReceiver protocolReceiver;

    @Autowired
    private EcosProtocolSender protocolSender;

    @Autowired
    private ConverterManager converterManager;

    @Override
    public String getControllerId() {
        return controllerId;
    }

    @Override
	public void connect() throws ConnectionException {
		try {
			ecosSocket = new Socket(hostName, portNumber);

            protocolReceiver.start(ecosSocket);
            protocolSender.start(ecosSocket);

            //force it to enable the station
            protocolSender.publish(EcosCommandBuilder.set(1).param("go").build());
		} catch(IOException e) {
			throw new ConnectionException("Unable to connect to Ecos Control center, due too IOException", e);
		}
	}

    @Override
    public void publish(TrainCommand command) {
        LOG.info("Received train command: {}", command);
        EcosCommand ecosCommand = converterManager.convert(command, EcosCommand.class);
        if(ecosCommand != null) {
            try {
                LOG.debug("Sending ecos command: {}", ecosCommand);
                protocolSender.publish(ecosCommand);
            } catch (ConnectionException e) {
                LOG.error("", e);
            }
        } else {
            LOG.warn("Could not convert train command: {}", command);
        }
    }

    @Override
	public void disconnect() throws ConnectionException {
		try {
			this.protocolReceiver.stop();
			this.protocolSender.stop();

			this.ecosSocket.close();
		} catch(IOException e) {
			throw new ConnectionException("Unable to close connection to Ecos Controller", e);
		}
	}
}
