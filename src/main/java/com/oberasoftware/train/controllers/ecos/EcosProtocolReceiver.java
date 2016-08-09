package com.oberasoftware.train.controllers.ecos;

import com.oberasoftware.home.core.mqtt.MQTTTopicEventBus;
import com.oberasoftware.train.controllers.ecos.messages.EcosReceivedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class EcosProtocolReceiver implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(EcosProtocolReceiver.class);

	private AtomicBoolean isRunning = new AtomicBoolean(false);
	private Socket clientSocket;

    @Autowired
    private EcosMessageParser messageParser;

    @Autowired
    private MQTTTopicEventBus eventBus;
	
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

	public void start(Socket clientSocket) {
        this.clientSocket = clientSocket;

		LOG.info("Create the Ecos Controller receiver thread");
		isRunning.set(true);
        executorService.submit(this);
	}
	
	public void stop() {
		LOG.info("Stopping the Ecos Controller receiver");
		isRunning.set(false);
	}
	
	@Override
	public void run() {
		LOG.info("Started the Ecos Receiver thread");
		try {
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			while(isRunning.get() && !Thread.currentThread().isInterrupted()) {
				LOG.info("Waiting for an incoming message from Ecos Controller");

                String line = inputReader.readLine();
                LOG.info("Received a line: {}", line);
                Optional<EcosReceivedMessage> received = messageParser.pushLine(line);
                if(received.isPresent()) {
                    LOG.info("Received a complete message: {}", received);
//                    eventBus.publish(received.get());
                }
			}
		} catch(IOException e) {
            LOG.error("Ecos Controller IO Channel was closed");
			isRunning.set(false);
		} catch(Exception e) {
            LOG.error("", e);
        }
		LOG.info("Ecos Receiver thread has stopped");
	}

}
