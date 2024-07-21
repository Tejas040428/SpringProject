package com.tnqsoftware.customer_mapping_to_user.exception;

public class CustomerNotFoundException extends RuntimeException {

	public CustomerNotFoundException() {
		super("Customer Not Found");
		// TODO Auto-generated constructor stub
	}

	public CustomerNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
}
