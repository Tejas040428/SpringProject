package com.tnqsoftware.customer_mapping_to_user.entity;

import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomException {

	private Date timestamp;
	private String message;
	private String details;
	private int status;
	
}
