package com.tnqsoftware.customer_mapping_to_user.exception;

public class UserNameNotFoundException  extends RuntimeException{

	public UserNameNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UserNameNotFoundException() {
		super("User Credentials are not correct");
		// TODO Auto-generated constructor stub
	}
	
}
