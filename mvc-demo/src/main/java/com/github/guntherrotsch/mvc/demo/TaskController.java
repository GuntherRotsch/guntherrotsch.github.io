package com.github.guntherrotsch.mvc.demo;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("tasks")
@Controller
@RequestScoped
public class TaskController {

	private Models models;

	public enum TaskPriority {
		HIGH, MEDIUM, LOW;
	}

	public static class Task {
		private String description;
		private TaskPriority priority;

		public Task(String description, TaskPriority priority) {
			super();
			this.description = description;
			this.priority = priority;
		}

		public String getDescription() {
			return description;
		}

		public TaskPriority getPriority() {
			return priority;
		}
	}

	private static List<Task> tasksList = new ArrayList<>();

	public TaskController() {
		// no-arg constructor required by CDI
	}

	@Inject
	public TaskController(Models models) {
		this.models = models;
	}

	@GET
	public String showTasks() {
		models.put("tasks", tasksList);
		return "tasks/tasks.ftl";
	}

	@POST
	public String addTask(@FormParam("description") String description,
			@FormParam("priority") TaskPriority priority) {
		tasksList.add(new Task(description, priority));
		models.put("tasks", tasksList);
		return "tasks/tasks.ftl";
	}
}
