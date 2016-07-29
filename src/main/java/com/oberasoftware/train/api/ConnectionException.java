package com.oberasoftware.train.api;

import com.oberasoftware.home.api.exceptions.HomeAutomationException;

public class ConnectionException extends HomeAutomationException {
	public ConnectionException(String message) {
		super(message);
	}
	
	public ConnectionException(String message, Throwable embeddedException) {
		super(message, embeddedException);
	}
}
