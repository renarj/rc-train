package com.oberasoftware.train.controllers.ecos;

import com.oberasoftware.base.event.EventBus;
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
    private EventBus eventBus;
	
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
				LOG.debug("Waiting for an incoming message from Ecos Controller");

                Optional<EcosReceivedMessage> receivedMessage;
                while((receivedMessage = messageParser.pushLine(inputReader.readLine())).isPresent()) {
                    LOG.debug("Received a line: {}", messageParser.getLastLine());
                }

				eventBus.publish(receivedMessage.get());
			}
		} catch(IOException e) {
            LOG.debug("Ecos Controller IO Channel was closed");
			isRunning.set(false);
		}
		LOG.info("Ecos Receiver thread has stopped");
	}

}
