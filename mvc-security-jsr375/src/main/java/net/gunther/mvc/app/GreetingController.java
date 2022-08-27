package net.gunther.mvc.app;

import java.util.logging.Logger;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

@Path("/greeting")
@Controller
@RequestScoped
public class GreetingController {

	private static final Logger LOGGER = Logger.getLogger("LoggingFeature");

	private Models models;

	public GreetingController() {
		// no-arg constructor required by CDI
	}

	@Inject
	public GreetingController(Models models) {
		this.models = models;
	}

	@GET
	@Produces("text/html")
	public String hello(@QueryParam("name") String name) {
		LOGGER.info("Greeting hello called with name: " + name);
		models.put("visitor", name);
		return "greeting.jsp";
	}
}
