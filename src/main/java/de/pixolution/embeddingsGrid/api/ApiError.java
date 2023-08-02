package de.pixolution.embeddingsGrid.api;
//see https://www.toptal.com/java/spring-boot-rest-api-error-handling

import java.util.Date;

import org.springframework.http.HttpStatus;

import de.pixolution.embeddingsGrid.model.ClientErrorResponseJson;

public class ApiError extends RuntimeException {

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = -2355963726340563256L;
	private HttpStatus status;
	private Date timestamp;
	private String error;
	private String path;

	public ApiError(HttpStatus status, String error, String path) {
		super(error);
		this.status = status;
		this.error = error;
		this.path = path;
		this.timestamp = new Date();
	}
	
	public HttpStatus getStatus() {
		return status;
	}

	public Date getTimestamp() {
		return timestamp;
	}
	
	public String getError() {
		return error;
	}

	public String getPath() {
		return path;
	}
	
	public ClientErrorResponseJson getClientErrorResponseJson() {
		return new ClientErrorResponseJson(this.timestamp, 
				   status.value(),
				   error, 
				   "/sort");
	}
}