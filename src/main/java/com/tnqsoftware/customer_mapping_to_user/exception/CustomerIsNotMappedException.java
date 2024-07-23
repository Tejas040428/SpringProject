package com.tnqsoftware.customer_mapping_to_user.exception;

public class CustomerIsNotMappedException extends RuntimeException {

	public CustomerIsNotMappedException() {
		super("Customer is not mapped yet");
		// TODO Auto-generated constructor stub
	}

	public CustomerIsNotMappedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
