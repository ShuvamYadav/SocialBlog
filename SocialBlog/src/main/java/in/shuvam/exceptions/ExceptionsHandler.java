package in.shuvam.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {
	
	@ExceptionHandler(UsernameNotFound.class)
	public ResponseEntity<String> notfound(UsernameNotFound ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());	
	}
	@ExceptionHandler(UsernameTaken.class)
	public ResponseEntity<String> found(UsernameTaken ex){
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());	
	}
	@ExceptionHandler(Alreadyliked.class)
	public ResponseEntity<String> found(Alreadyliked ex){
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());	
	}
	@ExceptionHandler(DefaultException.class)
	public ResponseEntity<String> notfound(DefaultException ex){
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());	
	}
	@ExceptionHandler(LoginException.class)
	public ResponseEntity<String> notfound(LoginException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());	
	}
	@ExceptionHandler(IdTaken.class)
	public ResponseEntity<String> found(IdTaken ex){
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());	
	}

}
