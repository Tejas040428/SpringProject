package com.tnqsoftware.customer_mapping_to_user.exception;

public class UsernameAlreadyFoundException extends RuntimeException {
	public static int status = 402;
	public UsernameAlreadyFoundException(String message){
		super(message);
	}

	public UsernameAlreadyFoundException() {
		super("User name already.!!!!");
		// TODO Auto-generated constructor stub
	}
	
}
