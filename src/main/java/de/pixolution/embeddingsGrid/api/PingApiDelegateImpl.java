package de.pixolution.embeddingsGrid.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import de.pixolution.embeddingsGrid.model.PingResponseJson;

@Component
public class PingApiDelegateImpl implements PingApiDelegate {
	
	@Autowired
	public PingApiDelegateImpl() {
		
	}

	@Override
	public ResponseEntity<PingResponseJson> pingGet() {
		PingResponseJson response = new PingResponseJson("Ping back");
		return new ResponseEntity<PingResponseJson>(response, HttpStatus.OK);
	}
}
