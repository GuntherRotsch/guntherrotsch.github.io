package net.gunther.wildfly.demo.app;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.gunther.multipart.client.MultiPartMessage;

@Path("/form")
public class FormDataResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(FormDataResource.class);

	@Context
	private HttpServletRequest request;

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response postFormData(MultiPartMessage message) {
		StringBuilder strBuilder = new StringBuilder();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			strBuilder.append("\t").append(headerName).append(": ").append(request.getHeader(headerName)).append("\n");
		}
		LOGGER.info("Request Header:\n\t{}", strBuilder);
		LOGGER.info("Request Message:\n\t{}", message);
		

		return Response.noContent().build();
	}

	/**
	 * Method returns simple HTML form for testing of mutlipart-form messages with the Browser.
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response getForm() {
		// @formatter:off
		String form =
			  "<h2>File Upload Example</h2>  \r\n"
			+ "<form action=\"form\" method=\"post\" enctype=\"multipart/form-data\">  \r\n"
			+ "       <p>  \r\n"
			+ "        Select a file : <input type=\"file\" name=\"file\" />  \r\n"
			+ "       <p>  \r\n"
			+ "        Input your name: <input type=\"string\" name=\"name\" />  \r\n"
			+ "       <p>  \r\n"
			+ "        Input your age: <input type=\"number\" name=\"age\" />  \r\n"
			+ "       </p>  \r\n"
			+ "       <input type=\"submit\" value=\"Submit\" />  \r\n"
			+ "</form>";
		// @formatter:on
		return Response.ok(form).build();
	}
}
