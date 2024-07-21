package com.tnqsoftware.customer_mapping_to_user.exception;

public class CustomerNotAddedException extends RuntimeException {

	public CustomerNotAddedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CustomerNotAddedException() {
		super("Customer was not added");
		// TODO Auto-generated constructor stub
	}
	

}
