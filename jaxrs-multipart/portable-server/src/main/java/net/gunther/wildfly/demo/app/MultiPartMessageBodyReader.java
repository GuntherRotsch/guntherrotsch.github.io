package net.gunther.wildfly.demo.app;

import static java.util.stream.Collectors.toList;
import static net.gunther.wildfly.demo.app.PartInputStream.CR;
import static net.gunther.wildfly.demo.app.PartInputStream.EOF;
import static net.gunther.wildfly.demo.app.PartInputStream.LF;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.gunther.multipart.client.FieldPart;
import net.gunther.multipart.client.FilePart;
import net.gunther.multipart.client.MultiPartMessage;
import net.gunther.multipart.client.Part;

@Provider
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class MultiPartMessageBodyReader implements MessageBodyReader<MultiPartMessage> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MultiPartMessageBodyReader.class);

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		LOGGER.info("isReadable called with type: {} and mediaType: {}", type, mediaType);
		return MultiPartMessage.class.isAssignableFrom(type)
				&& mediaType.toString().toLowerCase().startsWith("multipart/form-data");
	}

	@Override
	public MultiPartMessage readFrom(Class<MultiPartMessage> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		if (!httpHeaders.getFirst("Content-type").toLowerCase().startsWith("multipart/form-data")) {
			throw new IllegalArgumentException(
					"MultiPartMessageBodyReader is applicable for multipart/form-data content type only.");
		}

		String boundary = getBoundary(httpHeaders);
		InputStream inputStream = getResetableInputStream(entityStream);
		PartInputStream partInputStream = readPrecedingBoundary(boundary, inputStream);

		MultiPartMessage multiPartMessage = new MultiPartMessage();
		while (!partInputStream.isLastPart()) {
			partInputStream = new PartInputStream(inputStream, ("\r\n" + boundary).getBytes(StandardCharsets.US_ASCII));
			Map<String, String> contentHeaders = getContentHeader(partInputStream);
			String dispositionHeader = contentHeaders.get("content-disposition");
			LOGGER.debug("Part Content Header: {}", contentHeaders);

			Map<String, String> qualifiers;
			if (dispositionHeader != null) {
				qualifiers = extractQualifiers(dispositionHeader);
				LOGGER.debug("Content-disposition qualifiers: {}", qualifiers);
				if (qualifiers.get("filename") != null) {
					multiPartMessage.addPart(createFilePart(qualifiers, partInputStream));
				} else {
					multiPartMessage.addPart(createFieldPart(qualifiers, partInputStream));
				}
			}
		}
		return multiPartMessage;
	}

	private PartInputStream readPrecedingBoundary(String boundary, InputStream inputStream) throws IOException {
		// PartInputStream is used to consume the first boundary leading the message
		// body. That's possible because the way PartInputStream is implemented the
		// leading boundary is a part with empty part's body.
		PartInputStream partInputStream = new PartInputStream(inputStream,
				boundary.getBytes(StandardCharsets.US_ASCII));
		byte[] bytes = readStream(partInputStream);
		if (bytes.length != 0) {
			List<Byte> bytesList = IntStream.range(0, bytes.length).mapToObj(i -> bytes[i]).collect(toList());
			String bytesAsHex = bytesList.stream().map(b -> String.format("%02X", Byte.toUnsignedInt(b)))
					.collect(Collectors.joining(" "));

			LOGGER.warn("Multipart form-data message should start with boundary, but begins with bytes: {}",
					bytesAsHex);
		}
		return partInputStream;
	}

	private Part createFilePart(Map<String, String> qualifiers, InputStream partInputStream)
			throws FileNotFoundException, IOException {
		String tempPath = Files.createTempDirectory("upload").toString();
		File file = Paths.get(tempPath, qualifiers.get("filename")).toFile();
		partInputStream.transferTo(new FileOutputStream(file));
		return FilePart.of(qualifiers.getOrDefault("name", "unnamed"), file);
	}

	private Part createFieldPart(Map<String, String> qualifiers, InputStream partInputStream) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		partInputStream.transferTo(os);
		return FieldPart.of(qualifiers.getOrDefault("name", "unnamed"), os.toString());
	}

	private String getBoundary(MultivaluedMap<String, String> httpHeaders) {
		String contentTypeHeader = httpHeaders.getFirst("Content-type");
		Map<String, String> qualifiers = extractQualifiers(contentTypeHeader);
		String boundary = "--" + qualifiers.get("boundary");
		LOGGER.debug("Boundary: {}", boundary);
		return boundary;
	}

	private byte[] readStream(InputStream is) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		is.transferTo(os);
		return os.toByteArray();
	}

	private Map<String, String> getContentHeader(PartInputStream inputStream) throws IOException {
		List<String> lines = readLines(inputStream);
		return lines.stream().map(l -> l.split(":"))
				.collect(Collectors.toMap(a -> a[0].trim().toLowerCase(), a -> a[1].trim()));
	}

	private List<String> readLines(PartInputStream is) throws IOException {
		List<String> lines = new ArrayList<>();
		String line = readLine(is);
		while (!line.isEmpty()) {
			lines.add(line);
			line = readLine(is);
		}
		return lines;
	}

	private String readLine(PartInputStream is) throws IOException {
		int c1 = is.read();
		if (c1 == EOF) {
			return "";
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int c2 = is.read();
		// searching for end of file (EOF) or sequence of carriage return (CR) and line
		// feed (LF) sequence characters, whichever comes first
		while (c2 != EOF && !(c1 == CR && c2 == LF)) {
			os.write(c1);
			c1 = c2;
			c2 = is.read();
		}
		return os.toString(StandardCharsets.US_ASCII);
	}

	private Map<String, String> extractQualifiers(String headerValue) {
		Map<String, String> qualifiers = new HashMap<>();
		String assignments[] = headerValue.split(";");
		for (String assignment : assignments) {
			String lr[] = assignment.split("=");
			if (lr.length == 2) {
				qualifiers.put(lr[0].trim().toLowerCase(), lr[1].trim().replaceAll("\"", ""));
			}
		}
		return qualifiers;
	}

	private InputStream getResetableInputStream(InputStream entityStream) {
		if (!entityStream.markSupported()) {
			LOGGER.debug("Wrap entity input stream to buffered input stream to support mark and reset operations.");
			return new BufferedInputStream(entityStream);
		} else {
			return entityStream;
		}
	}
}
