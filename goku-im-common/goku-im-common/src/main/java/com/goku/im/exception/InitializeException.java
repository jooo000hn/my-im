package com.goku.im.exception;

public class InitializeException extends Exception {
	public InitializeException() {
	}

	public InitializeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InitializeException(String message) {
		super(message);
	}
	
	public InitializeException(Throwable cause) {
		super(cause);
	}
}