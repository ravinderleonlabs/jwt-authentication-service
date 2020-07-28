package com.leonlabs.auth.rest;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.leonlabs.core.rest.AbstractRestHandler;
import com.leonlabs.core.view.ErrorView;
import com.leonlabs.core.view.HTTPStatus;
import com.leonlabs.core.view.ResponseView;

/**
 * This class is meant to be extended by all REST resource "controllers". It
 * contains exception mapping and other common REST API functionality
 */
@ControllerAdvice
public abstract class AuthenticationRestHandler extends AbstractRestHandler {

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler(UsernameNotFoundException.class)
	public @ResponseBody ResponseView handleUsernameNotFoundException(UsernameNotFoundException ex,
			WebRequest request) {
		ex.printStackTrace();
		return new ResponseView(HTTPStatus.PRECONDITION_FAILED, null,
				new ErrorView(HTTPStatus.PRECONDITION_FAILED.getName(), ex.getMessage()));
	}
	
}