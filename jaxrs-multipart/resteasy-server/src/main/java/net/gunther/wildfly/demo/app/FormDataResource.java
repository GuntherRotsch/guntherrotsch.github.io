package net.gunther.wildfly.demo.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/form")
public class FormDataResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(FormDataResource.class);

	@Context
	private HttpServletRequest request;

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response postFormData(MultipartFormDataInput input) {
		StringBuilder strBuilder = new StringBuilder("Request Headers:\n");
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			strBuilder.append("\t").append(headerName).append(": ").append(request.getHeader(headerName)).append("\n");
		}
		LOGGER.info("{}", strBuilder);
		LOGGER.info("{}", input.getParts());
		input.getParts().stream().forEach(p -> {
			try {
				MultivaluedMap<String, String> headers = p.getHeaders();
				LOGGER.info("------Part Headers: ", headers);
				String contentType = headers.getFirst("Content-Type");
				if (contentType != null) {
					LOGGER.info("Content type of part: {}", contentType);
				}
				String contentDisposition = headers.getFirst("Content-Disposition");
				Map<String, String> qualifiers = extractQualifiers(contentDisposition);
				LOGGER.info("Content disposition qualifiers: {}", qualifiers);
				if (qualifiers.get("filename") != null) {
					// Convert the uploaded file to inputstream
					InputStream inputStream = p.getBody(InputStream.class, null);
					File file = Paths.get(Files.createTempDirectory("upload").toFile().getAbsolutePath(),
							qualifiers.get("filename")).toFile();
					inputStream.transferTo(new FileOutputStream(file));
					LOGGER.info("File content written to: {}", file);
				} else {
					LOGGER.info(p.getBodyAsString());
				}
			} catch (IOException e) {
				LOGGER.error("Error occured.", e);
			}
		});

		return Response.noContent().build();
	}

	private Map<String, String> extractQualifiers(String contentDisposition) {
		Map<String, String> qualifiers = new HashMap<>();
		String assignments[] = contentDisposition.split(";");
		for (String assignment : assignments) {
			String lr[] = assignment.split("=");
			if (lr.length == 2) {
				qualifiers.put(lr[0].trim().toLowerCase(), lr[1].trim().replaceAll("\"", ""));
			}
		}
		return qualifiers;
	}

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
