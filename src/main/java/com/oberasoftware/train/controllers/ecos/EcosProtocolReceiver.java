package com.oberasoftware.train.controllers.ecos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
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
	
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public EcosProtocolReceiver(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	public void start() {
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

                while(!messageParser.pushLine(inputReader.readLine())) {

                }
//				EcosMessageBuilder messageBuilder = new EcosMessageBuilder();
//				String returnMessage = "";
//				while(!messageBuilder.isMessageComplete() && (returnMessage = inputReader.readLine()) != null) {
//					LOG.debug("Received a partial message fragment: " + returnMessage);
//					messageBuilder.pushResponse(returnMessage);
//				}
//
//				EcosMessage ecosMessage = messageBuilder.getEcosMessage();
//				if(ecosMessage != null) {
//					LOG.debug("Finished receive of message: " + ecosMessage.getMessageAsString());
//
//					//blockingMessageQueue.put(ecosMessage);
//
//					handlerCallback(ecosMessage);
//				} else {
//                    LOG.error("The decoding of the received message was unsuccesfull");
//				}
			}
		} catch(IOException e) {
            LOG.debug("Ecos Controller IO Channel was closed");
			isRunning.set(false);
		}
		LOG.info("Ecos Receiver thread has stopped");
	}

}
