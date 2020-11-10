package com.redhat.consulting.jdg.healthcheck.exception;

public class HealthCheckFailure extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public HealthCheckFailure() {}

	public HealthCheckFailure(String aMessage) {
		super(aMessage);
	}
	
	public HealthCheckFailure(Throwable t) {
		super(t);
	}
}
