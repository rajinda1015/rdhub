package com.rad.rdhub.validator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@SuppressWarnings({"unchecked", "rawtypes"})
@ControllerAdvice
public class RDHubExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleRecordNotFoundException(Exception ex, WebRequest webRequest) {
		RDErrorResponse errorResponse = new RDErrorResponse("Resource Not Found", ex.getLocalizedMessage());
		return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND); 
	}
	
	@ExceptionHandler(RDBadRequestException.class)
	public ResponseEntity<Object> handleBadRequestException(Exception ex, WebRequest webRequest) {
		RDErrorResponse errorResponse = new RDErrorResponse("Bad Request", ex.getLocalizedMessage());
		return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST); 
	}
}
