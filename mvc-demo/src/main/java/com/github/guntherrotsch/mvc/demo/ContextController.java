package com.github.guntherrotsch.mvc.demo;

import javax.enterprise.context.RequestScoped;
import javax.mvc.Controller;
import javax.ws.rs.GET;
import javax.ws.rs.Path;


@Path("/context")
@Controller
@RequestScoped
public class ContextController {

	@GET
	public String show() {
		return "context.ftl";
	}
}
