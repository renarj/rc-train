package com.oberasoftware.train.controllers.ecos;


import com.oberasoftware.train.api.ConnectionException;
import com.oberasoftware.train.api.TrainCommand;
import com.oberasoftware.train.api.TrainController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Socket;

@Component
public class EcosTrainController implements TrainController {
	private Socket ecosSocket;

    @Value("ecos.host")
	private String hostName;

    @Value("ecos.port")
	private int portNumber;

    @Autowired
    private EcosProtocolReceiver protocolReceiver;

    @Autowired
    private EcosProtocolSender protocolSender;
	
	@Override
	public void connect() throws ConnectionException {
		try {
			ecosSocket = new Socket(hostName, portNumber);

            protocolReceiver.start(ecosSocket);
            protocolSender.start(ecosSocket);
		} catch(IOException e) {
			throw new ConnectionException("Unable to connect to Ecos Control center, due too IOException", e);
		}
	}

    @Override
    public void publish(TrainCommand command) {

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
