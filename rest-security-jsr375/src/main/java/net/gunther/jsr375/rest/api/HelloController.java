package net.gunther.jsr375.rest.api;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/hello")
@ApplicationScoped
@DenyAll
public class HelloController {

	private HelloService service;

	/**
	 * Constructor required by CDI.
	 */
	public HelloController() {
		// no-op
	}

	@Inject
	public HelloController(HelloService service) {
		this.service = service;
	}

	@GET
	@RolesAllowed("users")
	public String sayHello() {
		System.out.println("User: " + service.getUserName());
		return "Hello world";
	}

	@GET
	@Path("/privileged")
	@RolesAllowed("admins")
	public String sayHelloAgain() {
		System.out.println("User: " + service.getUserName());
		return "Hello, privileged dude";
	}
}
