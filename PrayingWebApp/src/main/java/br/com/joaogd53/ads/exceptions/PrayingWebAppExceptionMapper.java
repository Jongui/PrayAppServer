package br.com.joaogd53.ads.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@RestControllerAdvice
public class PrayingWebAppExceptionMapper extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	@ResponseBody
	public ResponseEntity<Object> handleBadRequestException(BadRequestException exception, WebRequest request) {
		List<DetailError> errors = new ArrayList<DetailError>();
		errors.add(new DetailError(exception.getLocalizedMessage()));

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), request,
				errors.toArray(new DetailError[errors.size()]));

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception,
			WebRequest request) {
		List<DetailError> errors = new ArrayList<DetailError>();
		for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
			String msg = violation.getRootBeanClass().getSimpleName() + " " + violation.getPropertyPath() + ": "
					+ violation.getMessage() + " [current value = " + violation.getInvalidValue() + "]";

			errors.add(new DetailError(msg));
		}

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), request,
				errors.toArray(new DetailError[errors.size()]));

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseBody
	public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException exception,
			WebRequest request) {
		List<DetailError> errors = new ArrayList<DetailError>();
		errors.add(new DetailError(exception.getLocalizedMessage()));

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), request,
				errors.toArray(new DetailError[errors.size()]));

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EmailNotUniqueException.class)
	@ResponseBody
	public ResponseEntity<Object> handleEmailNotUniqueException(EmailNotUniqueException exception, WebRequest request) {
		List<DetailError> errors = new ArrayList<DetailError>();
		errors.add(new DetailError(exception.getLocalizedMessage()));

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), request,
				errors.toArray(new DetailError[errors.size()]));

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseBody
	public ResponseEntity<Object> handleNotFoundException(NotFoundException exception, WebRequest request) {
		List<DetailError> errors = new ArrayList<DetailError>();
		errors.add(new DetailError(exception.getLocalizedMessage()));

		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, exception.getLocalizedMessage(), request,
				errors.toArray(new DetailError[errors.size()]));

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseBody
	public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException exception, WebRequest request) {
		List<DetailError> errors = new ArrayList<DetailError>();
		errors.add(new DetailError(exception.getLocalizedMessage()));

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), request,
				errors.toArray(new DetailError[errors.size()]));

		return new ResponseEntity<Object>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Common structure for an Error Response.
	 */
	@JsonTypeName("error")
	@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
	public static class ApiError {
		private HttpStatus status;
		private String message; // user-facing (localizable) message, describing the error
		private String target; // endpoint of origin request
		private List<DetailError> details;

		public ApiError(HttpStatus status, String message, WebRequest request, DetailError... errors) {
			this.status = status;
			this.message = message;
			if (message == null) {
				this.message = status.getReasonPhrase();
			}
			this.details = Arrays.asList(errors);
			this.target = request.getDescription(false).substring(4);
		}

		public int getStatus() {
			return status.value();
		}

		public String getTarget() {
			return target;
		}

		public String getMessage() {
			return message;
		}

		public List<DetailError> getDetails() {
			return details;
		}

	}

	protected static class DetailError {
		private final String message; // user-facing (localizable) message, describing the error

		public DetailError(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

}
