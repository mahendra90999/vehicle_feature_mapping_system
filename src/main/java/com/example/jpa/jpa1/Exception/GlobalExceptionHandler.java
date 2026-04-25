package com.example.jpa.jpa1.Exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.jpa.jpa1.Dto.ApiResponseDto;
import com.example.jpa.jpa1.Exception.ForeignkeyException.ForeignkeyException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(DuplicateMappingException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateMappingException ex) {
		Map<String, Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("status", 400);
	    error.put("error", "Bad Request");
	    error.put("message", ex.getMessage());

        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", 404);
        error.put("error", "Not Found");
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
	
	
	@ExceptionHandler(MappingNotFoundException.class)
    public ApiResponseDto<Object> handleMappingNotFound(MappingNotFoundException ex) {
        return ApiResponseDto.builder()
                .success(false)
                .message(ex.getMessage())
                .data(null)
                .build();
    }
	
	 @ExceptionHandler(RuntimeException.class)
	    public ResponseEntity<String> handleException(RuntimeException ex) {
	        return ResponseEntity.badRequest().body(ex.getMessage());
	    }
	 
	 @ExceptionHandler(DuplicateUserException.class)
	    public ResponseEntity<Map<String, Object>> handleUserDuplicate(DuplicateUserException ex) {
			Map<String, Object> error = new HashMap<>();
			error.put("timestamp", LocalDateTime.now());
			error.put("status", 400);
		    error.put("error", "Bad Request");
		    error.put("message", ex.getMessage());

	        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	    }
	 
	 @ExceptionHandler(ForeignkeyException.class)
	    public ResponseEntity<Map<String, Object>> handleUserDuplicate(ForeignkeyException ex) {
			Map<String, Object> error = new HashMap<>();
			error.put("timestamp", LocalDateTime.now());
			error.put("status", 400);
		    error.put("error", "Bad Request");
		    error.put("message", ex.getMessage());

	        return new ResponseEntity<>(error,HttpStatus.FORBIDDEN);
	    }
	
}
