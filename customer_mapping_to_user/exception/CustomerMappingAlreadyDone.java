package com.tnqsoftware.customer_mapping_to_user.exception;

public class CustomerMappingAlreadyDone extends RuntimeException {

	public CustomerMappingAlreadyDone() {
		super("Customer is already mapped");
		// TODO Auto-generated constructor stub
	}

	public CustomerMappingAlreadyDone(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
}
