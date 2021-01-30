package net.gunther.wildfly.demo.app;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/hello")
public class HelloResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(HelloResource.class);

	@GET
	@Produces("text/plain")
	public Response sayHello() {
		LOGGER.debug("GET HelloResource called.");
		
		return Response.ok("Hello from Wildfly JAR\n").build();
	}
}
