package com.tnqsoftware.customer_mapping_to_user.exception;

import org.springframework.http.ResponseEntity;

public class NoCustomerMappedException extends RuntimeException {

	public NoCustomerMappedException() {
		super("No cutomer found");
		// TODO Auto-generated constructor stub
	}

	public NoCustomerMappedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
