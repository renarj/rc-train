package com.oberasoftware.train.controllers.ecos;


import com.oberasoftware.train.api.ConnectionException;
import com.oberasoftware.train.api.TrainCommand;
import com.oberasoftware.train.api.TrainController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Socket;

@Component
public class EcosTrainController implements TrainController {
	private Socket ecosSocket;
	private EcosProtocolReceiver ecosReceiver;
	private EcosProtocolSender ecosSender;

    @Value("ecos.host")
	private String hostName;

    @Value("ecos.port")
	private int portNumber;
	
	@Override
	public void connect() throws ConnectionException {
		try {
			ecosSocket = new Socket(hostName, portNumber);
			
			ecosReceiver = new EcosProtocolReceiver(ecosSocket);
			ecosSender = new EcosProtocolSender(ecosSocket);
			
			ecosReceiver.start();
			ecosSender.start();
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
			this.ecosSender.stop();
			this.ecosReceiver.stop();

			this.ecosSocket.close();
		} catch(IOException e) {
			throw new ConnectionException("Unable to close connection to Ecos Controller", e);
		}
	}
}
