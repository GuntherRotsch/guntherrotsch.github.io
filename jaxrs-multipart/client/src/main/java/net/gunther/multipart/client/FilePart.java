package net.gunther.multipart.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilePart implements Part {

	private static final Logger LOGGER = LoggerFactory.getLogger(FilePart.class);

	private String name;
	private File file;

	private FilePart(String name, File file) {
		this.name = name;
		this.file = file;
	}

	public static FilePart of(String name, File file) {
		return new FilePart(name, file);
	}

	public String getName() {
		return name;
	}

	public File getFile() {
		return file;
	}

	@Override
	public List<String> getContentHeaders() {
		String contentDisposition = "Content-Disposition: form-data; name=\"" + name + "\"; filename=\""
				+ file.getName() + "\"";
		String contentType = "Content-Type: " + getMimeType().orElse("application/octet-stream");

		return Arrays.asList(new String[] { contentDisposition, contentType });
	}

	private Optional<String> getMimeType() {
		String mimeType = null;
		try {
			mimeType = Files.probeContentType(file.toPath());
		} catch (IOException e) {
			LOGGER.warn("Exception while probing content type of file: {}, exception: {}", file, e);
		}
		if (mimeType == null) {
			mimeType = URLConnection.guessContentTypeFromName(file.getName());
		}
		return Optional.ofNullable(mimeType);
	}

	@Override
	public Supplier<InputStream> getContentStream() {
		return () -> createInputStreamFromFile();
	}

	private FileInputStream createInputStreamFromFile() {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return "FilePart [name=" + name + ", file=" + file + "]";
	}
}
