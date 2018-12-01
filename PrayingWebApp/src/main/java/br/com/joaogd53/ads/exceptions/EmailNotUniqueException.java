package br.com.joaogd53.ads.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailNotUniqueException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EmailNotUniqueException() {
		super();
	}
	public EmailNotUniqueException(String message) {
		super(message);
	}
	
}
