package io.github.guntherrotsch.simpledi;

public class RequestContext implements AutoCloseable {

	private RequestScoped<Void> scope;

	public RequestContext(RequestScoped<Void> requestScoped) {
		this.scope = requestScoped;
		this.scope.start();
	}

	@Override
	public void close() throws Exception {
		scope.stop();
	}
}
