package com.erp.farwood.portal.exception;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import javax.validation.ConstraintViolationException;

import org.hibernate.exception.SQLGrammarException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.erp.farwood.portal.response.Response;
import com.erp.farwood.portal.util.MotorInsuranceConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String errorMessage = ex.getBindingResult().getFieldErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage).findFirst().orElse(ex.getMessage());
		log.error(ex.getLocalizedMessage() + "{}", errorMessage);
		return setStatusAndMessage(HttpStatus.BAD_REQUEST, ex, request);
	}

	@ExceptionHandler(value = SQLException.class)
	public Response<String> sqlException(SQLException ex) {
		log.error(ex.getLocalizedMessage() + "{}", ex.toString());
		return setStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), MotorInsuranceConstants.UNABLE_TO_PROCEED);
	}

	@ExceptionHandler(value = JpaSystemException.class)
	public Response<String> jpaSystemException(JpaSystemException ex) {
		log.error(ex.getLocalizedMessage() + "{}", ex.toString());
		return setStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), MotorInsuranceConstants.UNABLE_TO_PROCEED);
	}

	@ExceptionHandler(value = SQLGrammarException.class)
	public Response<String> jdbcException(SQLGrammarException ex) {
		log.error(ex.getLocalizedMessage() + "{}", ex.toString());
		return setStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), MotorInsuranceConstants.UNABLE_TO_PROCEED);
	}

	@ExceptionHandler(value = SQLSyntaxErrorException.class)
	public Response<String> invalidataAccess(SQLSyntaxErrorException ex) {
		log.error(ex.getLocalizedMessage() + "{}", ex.toString());
		return setStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), MotorInsuranceConstants.UNABLE_TO_PROCEED);
	}

	@ExceptionHandler(value = InvalidDataAccessResourceUsageException.class)
	public Response<String> invalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException ex) {
		log.error(ex.getLocalizedMessage() + "{}", ex.toString());
		return setStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), MotorInsuranceConstants.UNABLE_TO_PROCEED);
	}

	@ExceptionHandler(value = DataAccessResourceFailureException.class)
	public Response<String> genericJDBCException(DataAccessResourceFailureException ex) {
		log.error(ex.getLocalizedMessage() + "{}", ex.toString());
		return setStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), MotorInsuranceConstants.UNABLE_TO_PROCEED);
	}

	@ExceptionHandler(value = IncorrectResultSizeDataAccessException.class)
	public Response<String> uniqueResultException(IncorrectResultSizeDataAccessException ex) {
		log.error(ex.getLocalizedMessage() + "{}", ex.toString());
		return setStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), MotorInsuranceConstants.UNABLE_TO_PROCEED);
	}

	@ExceptionHandler(value = ServerWebInputException.class)
	public Response<String> serverWebInputException(ServerWebInputException ex) {
		log.error(ex.getLocalizedMessage() + "{}", ex.toString());
		return setStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), MotorInsuranceConstants.UNABLE_TO_PROCEED);
	}

	@ExceptionHandler(value = FileNotFoundException.class)
	public ResponseEntity<Object> fileNotFoundException(FileNotFoundException ex, WebRequest request) {
		return setStatusAndMessage(HttpStatus.NOT_FOUND, ex, request);
	}

	@ExceptionHandler(value = ArrayIndexOutOfBoundsException.class)
	public ResponseEntity<Object> arrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException ex,
			WebRequest request) {
		return setStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
	}

	@ExceptionHandler(value = ClassCastException.class)
	public ResponseEntity<Object> classCastException(ClassCastException ex, WebRequest request) {
		return setStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
	}

	@ExceptionHandler(value = IllegalArgumentException.class)
	public ResponseEntity<Object> illegalArgumentException(IllegalArgumentException ex, WebRequest request) {
		return setStatusAndMessage(HttpStatus.BAD_REQUEST, ex, request);
	}

	@ExceptionHandler(value = NullPointerException.class)
	public ResponseEntity<Object> nullPointerException(NullPointerException ex, WebRequest request) {
		return setStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
	}

	@ExceptionHandler(value = NoSuchMethodError.class)
	public ResponseEntity<Object> nullPointerException(NoSuchMethodError ex, WebRequest request) {
		return setStatusAndMessage(HttpStatus.NOT_FOUND, null, request);
	}

	@ExceptionHandler(value = NumberFormatException.class)
	public ResponseEntity<Object> numberFormatException(NumberFormatException ex, WebRequest request) {
		return setStatusAndMessage(HttpStatus.BAD_REQUEST, ex, request);
	}

	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<Object> resourceNotFoundException(RuntimeException ex, WebRequest request) {
		return setStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
	}

	@ExceptionHandler(value = IllegalStateException.class)
	public ResponseEntity<Object> illegalStateException(IllegalStateException ex, WebRequest request) {
		return setStatusAndMessage(HttpStatus.BAD_REQUEST, ex, request);
	}

	@ExceptionHandler(value = ConstraintViolationException.class)
	public ResponseEntity<Object> resourceNotFoundException(ConstraintViolationException ex, WebRequest request) {
		return setStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> exception(Exception ex, WebRequest request) {
		return setStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
	}

	@ExceptionHandler(value = BadDataExceptionHandler.class)
	public ResponseEntity<Object> badDataExceptionHandler(BadDataExceptionHandler ex, WebRequest request) {
		return setStatusAndMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
	}

	@ExceptionHandler(value = AccessDeniedException.class)
	public ResponseEntity<Object> accessDeniedExceptionHandler(AccessDeniedException ex, WebRequest request) {
		return setStatusAndMessage(HttpStatus.FORBIDDEN, ex, request);
	}

	@ExceptionHandler(value = InvalidURLExpception.class)
	public ResponseEntity<Object> urlExpiredExpception(InvalidURLExpception ex, WebRequest request) {
		return setStatusAndMessage(HttpStatus.GONE, ex, request);
	}

	@ExceptionHandler(value = NoDataFoundException.class)
	public ResponseEntity<Object> noDataFoundException(NoDataFoundException ex, WebRequest request) {
		return setStatusAndMessage(HttpStatus.NO_CONTENT, ex, request);
	}

	private ResponseEntity<Object> setStatusAndMessage(HttpStatus status, Exception ex, WebRequest request) {
		log.error(ex.getMessage(), ex);
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), status, request);
	}

	private Response<String> setStatusAndMessage(int status, String message) {
		Response<String> reponseMap = new Response<>();
		reponseMap.setStatus(status);
		reponseMap.setData("");
		reponseMap.setMessage(message);
		return reponseMap;
	}

}
