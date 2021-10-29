package io.github.guntherrotsch.simpledi;

public class RequestScopeAlreadyActiveException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RequestScopeAlreadyActiveException() {
		super("Request scope is already active");
	}
}
