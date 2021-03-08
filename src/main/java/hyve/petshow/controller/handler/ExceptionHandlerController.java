package hyve.petshow.controller.handler;

import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static hyve.petshow.util.LogUtils.Messages.ERROR_REQUEST_MESSAGE;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {
	
	private static final String ERRO_REQUISICAO = "ERRO_REQUISICAO";

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<String> businessExceptionHandler(BusinessException e) {
		log.error(ERROR_REQUEST_MESSAGE, HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<String> notFoundExceptionHander(NotFoundException e) {
		log.error(ERROR_REQUEST_MESSAGE, HttpStatus.NO_CONTENT.getReasonPhrase(), e.getMessage());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> exceptionHander(Exception e) {
		log.error(ERROR_REQUEST_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERRO_REQUISICAO);
	}
}
