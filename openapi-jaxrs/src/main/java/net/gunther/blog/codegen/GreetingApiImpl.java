package net.gunther.blog.codegen;

import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.ApiParam;
import net.gunther.blog.codegen.api.GreetingApi;

//public class GreetingApiImpl implements GreetingApi {
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(GreetingApiImpl.class);
//
//	@Context
//	HttpHeaders headers;
//	@Context
//	UriInfo uriInfo;
//
//	public Response greet(@QueryParam("name") @ApiParam("Optional name of person to greet") String name) {
//		StringBuilder headersBuilder = new StringBuilder("Headers:\n");
//		for (Map.Entry<String, List<String>> header : headers.getRequestHeaders().entrySet()) {
//			headersBuilder.append("\t").append(header.getKey()).append(": ").append(header.getValue()).append("\n");
//		}
//		LOGGER.info(headersBuilder.toString());
//		LOGGER.info("UriInfo: {}", uriInfo);
//
//		return Response.ok("Hello world").build();
//	}
//}
