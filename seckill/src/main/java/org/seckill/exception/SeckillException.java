package org.seckill.exception;

public class SeckillException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8181529577964661145L;

	public SeckillException(String message, Throwable cause) {
		super(message, cause);
	}

	public SeckillException(String message) {
		super(message);
	}

	
}
