package com.sales.notification.app.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SalesNotificationExceptionHandler {

	@ExceptionHandler
	public String handleException(Exception exception) {
		return "Techical Error occcured in Sales Notification Service";
	}

}
