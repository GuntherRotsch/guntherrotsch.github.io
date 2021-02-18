package net.gunther.multipart.client;

import java.io.File;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JAXRS multipart/form client app.
 */
public class App {

	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {

		Client client = ClientBuilder.newBuilder().register(MultipartMessageBodyWriter.class).build();

		MultiPartMessage multiPartMessage = new MultiPartMessage();
		multiPartMessage.addPart(FilePart.of("duke.png", new File("duke.png")));
		multiPartMessage.addPart(FieldPart.of("name", "Gunther"));
		multiPartMessage.addPart(FieldPart.of("age", "55"));
		LOGGER.info("Posting form data as multi-part message: {}", multiPartMessage);

		try (Response response = client.target("http://localhost:8080/form").request()
				.post(Entity.entity(multiPartMessage, MediaType.MULTIPART_FORM_DATA))) {

			LOGGER.info("Response on POST to Form-Data POST: {}", response.getStatusInfo());
			if (response.getStatus() != HttpStatus.SC_NO_CONTENT) {
				LOGGER.info("Response Body: {}", response.readEntity(String.class));
			}
		}
	}
}
