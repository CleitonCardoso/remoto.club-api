package com.remototech.remototechapi.exceptions;

public class GlobalException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GlobalException(String message) {
		super( message );
	}

	public GlobalException(String message, Exception e) {
		super( message, e );
	}

}
