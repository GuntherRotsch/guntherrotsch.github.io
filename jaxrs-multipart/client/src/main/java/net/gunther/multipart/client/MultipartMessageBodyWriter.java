package net.gunther.multipart.client;

import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA_TYPE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipartMessageBodyWriter implements MessageBodyWriter<MultiPartMessage> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MultipartMessageBodyWriter.class);

	private static final String HTTP_LINE_DELIMITER = "\r\n";

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return MultiPartMessage.class.isAssignableFrom(type) && MULTIPART_FORM_DATA_TYPE.equals(mediaType);
	}

	@Override
	public void writeTo(MultiPartMessage t, Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {

		String boundary = "-----------" + UUID.randomUUID().toString().replace("-", "");
		LOGGER.debug("Boundary: {}", boundary);

		List<Object> contentTypeHeader = new ArrayList<>();
		contentTypeHeader.add(MediaType.MULTIPART_FORM_DATA + "; boundary=\"" + boundary + "\"");
		httpHeaders.put("Content-type", contentTypeHeader);

		for (Part part : t.getParts()) {
			writePart(boundary, entityStream, part);
			LOGGER.debug("Part written: {}", part);
		}
		String endBoundary = "--" + boundary + "--" + HTTP_LINE_DELIMITER;
		print(entityStream, endBoundary);
	}

	private void writePart(String boundary, OutputStream entityStream, Part part) throws IOException {
		String startBoundary = "--" + boundary + HTTP_LINE_DELIMITER;

		print(entityStream, startBoundary);
		for (String contentHeader : part.getContentHeaders()) {
			print(entityStream, contentHeader + HTTP_LINE_DELIMITER);
		}
		print(entityStream, HTTP_LINE_DELIMITER);

		try (InputStream contentStream = part.getContentStream().get()) {
			contentStream.transferTo(entityStream);
		}
		print(entityStream, HTTP_LINE_DELIMITER);
	}

	private void print(OutputStream stream, String str) throws IOException {
		stream.write(str.getBytes(StandardCharsets.US_ASCII));
	}
}
