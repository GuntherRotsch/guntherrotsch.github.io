package com.github.guntherrotsch.mvc.demo;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.eclipse.krazo.engine.Viewable;

@Path("hello")
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

	@GET
	public Viewable greet(@QueryParam("name") String name) {
		models.put("visitor", name);
		return new Viewable("greeting.ftl");
	}
}
