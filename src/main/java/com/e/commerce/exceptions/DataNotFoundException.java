package com.e.commerce.exceptions;

public class DataNotFoundException extends RuntimeException{
	public DataNotFoundException(String msg){
		super(msg);
	}
}
