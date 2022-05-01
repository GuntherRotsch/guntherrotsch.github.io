package net.gunther.jsr375.rest.api;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;

@ApplicationScoped
public class HelloService {

	private SecurityContext securityContext;

	/**
	 * Constructor required by CDI.
	 */
	public HelloService() {
		// no-op
	}

	@Inject
	public HelloService(SecurityContext securityContext) {
		this.securityContext = securityContext;
	}

	public String getUserName() {
		return securityContext.getCallerPrincipal().getName();
	}
}
