package com.poo.modelo.dao;

public class SemVagaExeception extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SemVagaExeception() {
		super();
	}

	public SemVagaExeception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SemVagaExeception(String message, Throwable cause) {
		super(message, cause);
	}

	public SemVagaExeception(String message) {
		super(message);
	}

	public SemVagaExeception(Throwable cause) {
		super(cause);
	}

}
