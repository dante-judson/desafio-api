package com.judson.desafio.api.controller.advice;

import org.apache.tomcat.websocket.AuthenticationException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.judson.desafio.exception.BadCredentialsException;
import com.judson.desafio.exception.UnauthorizedUserException;
import com.judson.desafio.exception.UserNotFoundException;

@ControllerAdvice
public class ErrorHandlingControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiError> handleDataIntegrityViolationException(ConstraintViolationException ex) {
		return ResponseEntity.badRequest().body(new ApiError("Integridade de dados violada.", 
				ex.getSQLException().getMessage(),
				ConstraintViolationException.class));
	}
	
	@ExceptionHandler(BadCredentialsException.class) 
	public ResponseEntity<ApiError> handleBadCrenditialsExceptions(BadCredentialsException ex) {
		return ResponseEntity.badRequest().body(new ApiError("Credenciais inválidas.",
				ex.getMessage(),
				BadCredentialsException.class));
	}

	@ExceptionHandler(UnauthorizedUserException.class) 
	public ResponseEntity<ApiError> handleUnauthorizedUserException(UnauthorizedUserException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError(ex.getMessage(),
				ex.getLocalizedMessage(),
				UnauthorizedUserException.class));
	}
	
	@ExceptionHandler(UserNotFoundException.class) 
	public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex) {
		return ResponseEntity.badRequest().body(new ApiError("Usuário não encontrado.",
				ex.getMessage(),
				UserNotFoundException.class));
	}
	
	@ExceptionHandler(AuthenticationException.class) 
	public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError(ex.getMessage(),
				ex.getLocalizedMessage(),
				AuthenticationException.class));
	}
	
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(ex.getMessage(),
//				ex.getLocalizedMessage(),
//				MethodArgumentNotValidException.class));
//	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String message = "";
		
		for ( FieldError err : ex.getBindingResult().getFieldErrors()) {
			message += "O campo " + err.getField()
					+ " " + err.getDefaultMessage() + System.getProperty("line.separator");
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(message,
				ex.getLocalizedMessage(),
				MethodArgumentNotValidException.class));
	}
	
	static class ApiError {
		String message;
		
		String detailedMessage;
		
		Class<? extends Exception> type;
		
		public ApiError(String message,String detailedMessage, Class<? extends Exception> type) {
			this.message = message;
			this.detailedMessage = detailedMessage;
			this.type = type;
		}

		
		public String getDetailedMessage() {
			return detailedMessage;
		}

		public String getMessage() {
			return message;
		}

		public String getType() {
			return type.getSimpleName();
		}
	}
	
}
