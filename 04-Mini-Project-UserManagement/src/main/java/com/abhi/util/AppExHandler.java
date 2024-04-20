package com.abhi.util;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExHandler {
	
	@ExceptionHandler
	public String handleEx(Exception e) {
		return "errorPage";
	}

}
