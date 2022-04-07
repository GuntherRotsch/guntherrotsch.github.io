package net.gunther.jsr375.rest.api;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/hello")
@ApplicationScoped
@DenyAll
public class HelloController {

	@GET
	@RolesAllowed("users")
	public String sayHello() {
		return "Hello world";
	}

	@GET
	@Path("/privileged")
	@RolesAllowed("admins")
	public String sayHelloAgain() {
		return "Hello, privileged dude";
	}
}
