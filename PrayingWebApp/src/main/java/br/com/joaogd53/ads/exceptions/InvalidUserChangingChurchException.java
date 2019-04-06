package br.com.joaogd53.ads.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidUserChangingChurchException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5992881319111758353L;

	public InvalidUserChangingChurchException() {
		super();
	}

	public InvalidUserChangingChurchException(String message) {
		super(message);
	}

}
