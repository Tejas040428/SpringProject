package com.tnqsoftware.customer_mapping_to_user.exception;

public class OldPasswordWrongException extends RuntimeException {

	public OldPasswordWrongException() {
		super("Old Password is wrong");
		// TODO Auto-generated constructor stub
	}

	public OldPasswordWrongException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}
