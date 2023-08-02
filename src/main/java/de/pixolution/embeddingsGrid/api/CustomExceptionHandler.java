package de.pixolution.embeddingsGrid.api;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import de.pixolution.embeddingsGrid.model.ClientErrorResponseJson;
/**
 * This class catches all Exceptions and compose the error response.
 * Look into class ResponseEntityExceptionHandler for predefined methods to override or
 * use the @ExceptionHandler annotation for specific errors.
 * This class needs to be in a package that is part of the component-scan, 
 * see class de.pixolution.embeddingsGrid.invoker.OpenApiGeneratorApplication.java
 * for the @ComponentScan annotation.
 */
@RestControllerAdvice
public class CustomExceptionHandler  extends ResponseEntityExceptionHandler {

	private final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);
	
	@ExceptionHandler(ApiError.class)
	public ResponseEntity<ClientErrorResponseJson> handleException(ApiError ex) {
		ClientErrorResponseJson errorResponse = new ClientErrorResponseJson();
		if (ex.getError() != null && ex.getMessage() != null && ex.getMessage().equals(ex.getError())) {
			errorResponse.setError(ex.getError());
		} else {
			errorResponse.setError(ex.getError()+". Message: "+ex.getMessage());
		}
		errorResponse.setPath(ex.getPath());
		errorResponse.setStatus(ex.getStatus().value());
		errorResponse.setTimestamp(ex.getTimestamp());
		log.error(ex.getPath()+" handler: status "+HttpStatus.BAD_REQUEST.value()+" message: "+errorResponse.getError());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	/**
	 * Handle error regarding type and format of parameters
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ClientErrorResponseJson errorResponse = new ClientErrorResponseJson();
		String errMsg = ex.getMessage();
		errorResponse.setError(errMsg);
		if (request instanceof ServletWebRequest) {
			String contextPathStr = ((ServletWebRequest)request).getRequest().getRequestURI();
			errorResponse.setPath(contextPathStr);
		} else {
			errorResponse.setPath(request.getContextPath());
		}
		errorResponse.setStatus(status.value());
		errorResponse.setTimestamp(new Date());
		log.error(errorResponse.getPath()+" handler: status "+HttpStatus.BAD_REQUEST.value()+" message: "+errorResponse.getError());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);        
	}

	/**
	 * Hand out the error message in case a bind exception happened. Mostly this means
	 * that the parameter validation failed due to missing parameters.
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ClientErrorResponseJson errorResponse = new ClientErrorResponseJson();
		String errMsg = null;
		if (ex.hasErrors()) {
			StringBuilder errMsgBuilder =  new StringBuilder();
			// further inspect type of error
			if (ex.hasGlobalErrors()) {
				errMsgBuilder.append("The request body is malformed: ");
				// request itself is malformed
				for (ObjectError e:ex.getGlobalErrors()) {
					String msg = e.getDefaultMessage();
					if (msg != null) errMsgBuilder.append("  - "+e.getDefaultMessage());
				}
			}
			if (ex.hasFieldErrors()) {
				errMsgBuilder.append("Request body parameter are missing or malformed: ");
				errMsgBuilder.append(
						StringUtils.collectionToCommaDelimitedString(
								ex.getFieldErrors().stream().map(
										oe -> "\""+oe.getField()+"\" "+oe.getDefaultMessage()
										).toList()
								)
						);
			}
			errMsg = errMsgBuilder.toString();
		} else {
			// report the message in this case (should never happen)
			errMsg = ex.getMessage();
		}
		errorResponse.setError(errMsg);
		if (request instanceof ServletWebRequest) {
			String contextPathStr = ((ServletWebRequest)request).getRequest().getRequestURI();
			errorResponse.setPath(contextPathStr);
		} else {
			errorResponse.setPath(request.getContextPath());
		}
		errorResponse.setStatus(status.value());
		errorResponse.setTimestamp(new Date());
		log.error(errorResponse.getPath()+" handler: status "+HttpStatus.BAD_REQUEST.value()+" message: "+errorResponse.getError());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
}