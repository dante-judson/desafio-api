package com.judson.desafio.exception;

public class UnauthorizedUserException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1638645496504927655L;
	
	public UnauthorizedUserException(String msg) {
		super(msg);
	}

}
