package com.github.guntherrotsch.mvc.demo;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Named;

@Named("greetingGenerator")
public class GreetingGenerator {

	private static final List<String> greetingTemplates = asList("Hi %s", "Hello %s", "Ciao %s");

	public String select(String name) {
		String greetingTemplate = greetingTemplates
				.get(ThreadLocalRandom.current().nextInt(0, greetingTemplates.size()));
		return String.format(greetingTemplate, name);
	}
}
