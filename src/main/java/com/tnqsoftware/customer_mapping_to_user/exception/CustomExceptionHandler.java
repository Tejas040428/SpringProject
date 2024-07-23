package com.tnqsoftware.customer_mapping_to_user.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tnqsoftware.customer_mapping_to_user.entity.CustomException;

import jakarta.validation.ConstraintViolationException;
@RestController
@ControllerAdvice
public class CustomExceptionHandler  extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(UsernameAlreadyFoundException.class)
	public ResponseEntity<Object> handleUserAlreadyFound(UsernameAlreadyFoundException exception,
			WebRequest request){
		CustomException customException= new CustomException(
				new Date(),exception.getMessage(),request.getDescription(false),HttpStatus.UNPROCESSABLE_ENTITY.value());
		return new ResponseEntity<Object>(customException,HttpStatus.UNPROCESSABLE_ENTITY);
	}
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleUserNameVoilation(ConstraintViolationException exception,
			WebRequest request){
		CustomException customException= new CustomException(
				new Date(),exception.getMessage(),exception.getLocalizedMessage(),HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<Object>(customException,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(NoCustomerMappedException.class)
	public ResponseEntity<Object> handleNoCustomerMappedException(NoCustomerMappedException exception,
			WebRequest request){
		CustomException customException= new CustomException(
				new Date(),exception.getMessage(),exception.getLocalizedMessage(),HttpStatus.NO_CONTENT.value());
		return new ResponseEntity<Object>(customException,HttpStatus.OK);
	}
	@ExceptionHandler(CustomerRequestNotFound.class)
	public ResponseEntity<Object> handleCustomerRequestNotFound(CustomerRequestNotFound exception,
			WebRequest request){
		CustomException customException= new CustomException(
				new Date(),exception.getMessage(),exception.getLocalizedMessage(),HttpStatus.NO_CONTENT.value());
		return new ResponseEntity<Object>(customException,HttpStatus.NO_CONTENT);
	}
	@ExceptionHandler(CustomerAlreadyFound.class)
	public ResponseEntity<Object> handleCustomerMappingAlreadyDone(CustomerAlreadyFound exception,
			WebRequest request){
		CustomException customException= new CustomException(
				new Date(),exception.getMessage(),exception.getLocalizedMessage(),HttpStatus.UNPROCESSABLE_ENTITY.value());
		return new ResponseEntity<Object>(customException,HttpStatus.UNPROCESSABLE_ENTITY);
	}
	@ExceptionHandler(CustomerMappingAlreadyDone.class)
	public ResponseEntity<Object> handleCustomerMappingAlreadyDone(CustomerMappingAlreadyDone exception,
			WebRequest request){
		CustomException customException= new CustomException(
				new Date(),exception.getMessage(),exception.getLocalizedMessage(),HttpStatus.OK.value());
		return new ResponseEntity<Object>(customException,HttpStatus.OK);
	}
	
	@ExceptionHandler(CustomerValueNotUpatedException.class)
	public ResponseEntity<Object> handleCustomerValueNotUpatedException(CustomerValueNotUpatedException exception,
			WebRequest request){
		CustomException customException= new CustomException(
				new Date(),exception.getMessage(),exception.getLocalizedMessage(),HttpStatus.OK.value());
		return new ResponseEntity<Object>(customException,HttpStatus.NOT_MODIFIED);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleExpcetion(Exception exception,
			WebRequest request){
		CustomException customException= new CustomException(
				new Date(),exception.getMessage(),exception.getLocalizedMessage(),HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<Object>(customException,HttpStatus.BAD_REQUEST);
	}
	
}
