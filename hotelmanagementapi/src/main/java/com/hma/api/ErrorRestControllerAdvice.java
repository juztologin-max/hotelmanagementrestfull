package com.hma.api;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ErrorRestControllerAdvice {
	@ExceptionHandler(exception = Exception.class)
	public ResponseEntity<ExceptionResponseBody> defaultExceptionHandler(HttpServletRequest req, Exception ex) {
		ExceptionResponseBody body = new ExceptionResponseBody(ex.getClass().getSimpleName(), req.getRequestURI());
		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(exception = InsufficientAuthenticationException.class)
	public ResponseEntity<ExceptionResponseBody> insufficientAuthenticationExceptionHandler(HttpServletRequest req,
			Exception ex) {
		ExceptionResponseBody body = new ExceptionResponseBody("Missing credentials", req.getRequestURI());
		return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);

	}

	@ExceptionHandler(exception = HttpMessageNotReadableException.class)
	public ResponseEntity<ExceptionResponseBody> httpMessageNotReadableExceptionHandler(HttpServletRequest req,
			Exception ex) {
		ExceptionResponseBody body = new ExceptionResponseBody("Missing fields", req.getRequestURI());
		return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);

	}

	@ExceptionHandler(exception = BadCredentialsException.class)
	public ResponseEntity<ExceptionResponseBody> badCredentialsExceptionHandler(HttpServletRequest req, Exception ex) {
		ExceptionResponseBody body = new ExceptionResponseBody(ex.getMessage(), req.getRequestURI());
		return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);

	}

	@ExceptionHandler(exception = ExpiredJwtException.class)
	public ResponseEntity<ExceptionResponseBody> expiredJwtExceptionHandler(HttpServletRequest req, Exception ex) {
		ExceptionResponseBody body = new ExceptionResponseBody("Token Expired", req.getRequestURI());
		return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);

	}

}

record ExceptionResponseBody(String errorMessage, LocalDateTime occuredAt, String path) {
	ExceptionResponseBody(String errorMessage, String path) {
		this(errorMessage, LocalDateTime.now(), path);
	}
}
