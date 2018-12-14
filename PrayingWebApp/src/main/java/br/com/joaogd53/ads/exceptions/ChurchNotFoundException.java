package br.com.joaogd53.ads.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ChurchNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -220153475546381521L;

	public ChurchNotFoundException() {
		super();
	}

	public ChurchNotFoundException(String message) {
		super(message);
	}

}
