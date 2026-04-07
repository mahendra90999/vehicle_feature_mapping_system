package com.example.jpa.jpa1.Exception;

public class DuplicateUserException extends RuntimeException {
	
	public DuplicateUserException(String message) {
		super(message);
	}


}
