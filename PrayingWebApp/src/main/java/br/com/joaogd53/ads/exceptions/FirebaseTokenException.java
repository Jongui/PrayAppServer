package br.com.joaogd53.ads.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class FirebaseTokenException extends Exception {

	private static final long serialVersionUID = 5457699584429973203L;

	public FirebaseTokenException(String message) {
		super(message);
	}
}
