package com.erp.farwood.portal.exception;

public class InvalidURLExpception extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidURLExpception() {
		super();
	}

	public InvalidURLExpception(String message) {
		super(message);
	}
}
