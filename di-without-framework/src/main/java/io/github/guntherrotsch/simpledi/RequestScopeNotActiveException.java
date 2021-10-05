package io.github.guntherrotsch.simpledi;

public class RequestScopeNotActiveException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RequestScopeNotActiveException() {
		super("Request scope is not active");
	}
}
