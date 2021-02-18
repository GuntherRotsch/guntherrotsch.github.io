package net.gunther.multipart.client;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.junit.jupiter.api.Test;

/**
 * Unit test of MultipartMessageBodyWriter class.
 */
public class MultipartMessageBodyWriterTest {

	MultipartMessageBodyWriter testee = new MultipartMessageBodyWriter();

	@Test
	public void isWritableForMultiPartMessageAndMultipartFormDataMime() {
		assertTrue(testee.isWriteable(MultiPartMessage.class, null, null, MediaType.MULTIPART_FORM_DATA_TYPE));
	}

	@Test
	public void isWritableForMultiPartMessageSubtypeAndMultipartFormDataMime() {
		var multiPartMessage = new MultiPartMessage() {
		};
		System.out.println("Type of subcass: " + multiPartMessage.getClass());

		assertTrue(testee.isWriteable(multiPartMessage.getClass(), null, null, MediaType.MULTIPART_FORM_DATA_TYPE));
	}

	@Test
	public void writesPartFields() throws WebApplicationException, IOException {
		var entityStream = new ByteArrayOutputStream();
		var multiPartMesaage = new MultiPartMessage();
		multiPartMesaage.addPart(FieldPart.of("firstname", "Gunther"));
		multiPartMesaage.addPart(FieldPart.of("lastname", "Rotsch"));
		MultivaluedMap<String, Object> httpHeaders = new MultivaluedMapImpl<>();

		testee.writeTo(multiPartMesaage, MultiPartMessage.class, null, null, MediaType.MULTIPART_FORM_DATA_TYPE,
				httpHeaders, entityStream);

		var actualMessageBody = entityStream.toString(StandardCharsets.UTF_8);
		System.out.println("Actual message body: " + actualMessageBody);
		assertTrue(actualMessageBody.contains("Content-Disposition: form-data; name=\"firstname\"\r\n\r\nGunther"));
		assertTrue(actualMessageBody.contains("Content-Disposition: form-data; name=\"lastname\"\r\n\r\nRotsch"));
		var contentTypeHeaders = httpHeaders.get("Content-type");
		System.out.println("Content-type headers: " + contentTypeHeaders);
		assertTrue(contentTypeHeaders.get(0).toString().startsWith("multipart/form-data"));
		assertTrue(contentTypeHeaders.get(0).toString().contains("boundary="));
	}
}
