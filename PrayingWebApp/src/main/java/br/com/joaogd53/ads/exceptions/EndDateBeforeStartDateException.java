package br.com.joaogd53.ads.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EndDateBeforeStartDateException extends RuntimeException {

	private static final long serialVersionUID = 8479377666466298174L;
	
	public EndDateBeforeStartDateException(String message) {
		super(message);
	}

}
