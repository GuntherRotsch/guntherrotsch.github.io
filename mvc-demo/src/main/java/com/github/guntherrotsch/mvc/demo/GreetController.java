package com.github.guntherrotsch.mvc.demo;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/")
@Controller
@RequestScoped
public class GreetController {

	private Models models;

	public GreetController() {
		// no-arg constructor required by CDI
	}

	@Inject
	public GreetController(Models models) {
		this.models = models;
	}

	@Path("hello")
	@GET
	public String hello(@QueryParam("name") String name) {
		models.put("visitor", name);
		return "greeting.ftl";
	}

	@Path("hi")
	@GET
	public String hi(@QueryParam("name") String name) {
		models.put("visitor", name);
		return "randomGreeting.ftl";
	}
}
