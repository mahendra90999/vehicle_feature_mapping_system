package com.example.jpa.jpa1.Exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.jpa.jpa1.Dto.ApiResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(DuplicateMappingException.class)
    public ResponseEntity<String> handleDuplicate(DuplicateMappingException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
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
}
