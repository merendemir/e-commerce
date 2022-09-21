package com.e.commerce.exceptions;

public class DataAlreadyExistsException extends RuntimeException{
	public DataAlreadyExistsException(String msg){
		super(msg);
	}
}
