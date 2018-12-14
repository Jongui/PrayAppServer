package br.com.joaogd53.ads.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailNotMatchException extends RuntimeException {

	private static final long serialVersionUID = 6716909939170184745L;

	public EmailNotMatchException() {
		super();
	}

	public EmailNotMatchException(String message) {
		super(message);
	}
}
