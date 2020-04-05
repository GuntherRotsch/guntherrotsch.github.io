package net.gunther.blog.codegen.api.services;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.gunther.blog.codegen.JAXRSContext;

@ApplicationScoped
public class GreetingApiImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger(GreetingApiImpl.class);
	
	@Inject
	JAXRSContext context;

	public Response greet(String name) {
		StringBuilder headersBuilder = new StringBuilder("Headers:\n");
		for (Map.Entry<String, List<String>> header : context.getHttpHeaders().getRequestHeaders().entrySet()) {
			headersBuilder.append("\t").append(header.getKey()).append(": ").append(header.getValue()).append("\n");
		}
		LOGGER.info(headersBuilder.toString());
		LOGGER.info("UriInfo.absolutePath: {}", context.getUriInfo().getAbsolutePath());
		return Response.ok("Hello world").build();
	}
}
