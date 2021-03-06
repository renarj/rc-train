package com.oberasoftware.train.controllers.ecos;

import com.oberasoftware.train.api.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class EcosProtocolSender implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(EcosProtocolSender.class);
	
	private Socket clientSocket;
	private PrintWriter printWriter;
	private BlockingQueue<EcosCommand> messageQueue = new LinkedBlockingQueue<>();
	
	private AtomicBoolean isRunning = new AtomicBoolean(false);

	private ExecutorService executorService = Executors.newSingleThreadExecutor();

	public void publish(EcosCommand ecosCommand) throws ConnectionException {
		this.messageQueue.add(ecosCommand);
	}

	public void start(Socket clientSocket) throws ConnectionException {
        this.clientSocket = clientSocket;
		try {
			printWriter = new PrintWriter(clientSocket.getOutputStream(), true);

			isRunning.set(true);
            executorService.submit(this);
		} catch(IOException e) {
			throw new ConnectionException("Unable to fetch output stream to Ecos controller", e);
		}
	}

	public void stop() throws ConnectionException {
		isRunning.set(false);
        executorService.shutdown();
	}
	
	@Override
	public void run() {
        LOG.info("Ecos Sender thread has started");
		while(isRunning.get() && !Thread.currentThread().isInterrupted()) {
			try {
                EcosCommand command = this.messageQueue.take();
                String sendMessage = command.getCommand();

                LOG.info("Sending message to Ecos controller: {}", sendMessage);
                printWriter.println(sendMessage);
			} catch(InterruptedException e) {
                LOG.error("Interrupted while waiting for a queue item", e);
			}
		}
        LOG.info("Eocs sender thread has stopped");
	}
}
