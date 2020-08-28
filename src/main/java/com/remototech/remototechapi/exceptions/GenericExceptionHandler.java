package com.remototech.remototechapi.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String DEFAULT_MESSAGE = "Houve um erro inesperado, por favor entre em contato com o suporte do sistema.";

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	protected ErrorMessage handleConflictDataIntegrityViolation(DataIntegrityViolationException ex) {
		Throwable cause = ((DataIntegrityViolationException) ex).getRootCause();
		String message = DEFAULT_MESSAGE;
		if (cause.getMessage().contains( "update" ) || cause.getMessage().contains( "delete" )) {
			message = "Não é possível remover o registro pois ele está sendo referenciado em outro cadastro do sistema.";
		}

		if (cause.getMessage().contains( "duplicate key" )) {
			message = "Já existe um registro com os valores informados.";
		}

		log.error( ex.getMessage(), ex );
		return new ErrorMessage( message );
	}

	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	protected ErrorMessage handleConflictGeneric(Exception ex) {
		log.error( ex.getMessage(), ex );
		return new ErrorMessage( DEFAULT_MESSAGE );
	}

	@ExceptionHandler(value = GlobalException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	protected ErrorMessage handleConflictValidationError(GlobalException ex) {
		log.error( ex.getMessage(), ex );
		return new ErrorMessage( ex.getMessage() );
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error( ex.getMessage(), ex );
		return new ResponseEntity<Object>( new ErrorMessage( ex.getMessage() ), HttpStatus.BAD_REQUEST );
	}

}
