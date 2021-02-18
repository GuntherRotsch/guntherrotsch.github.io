package net.gunther.multipart.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FieldPart implements Part {

	private String name;
	private String value;

	private FieldPart(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public static FieldPart of(String name, String value) {
		return new FieldPart(name, value);
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	@Override
	public List<String> getContentHeaders() {
		return Arrays.asList(new String[] { "Content-Disposition: form-data; name=\"" + name + "\"" });
	}

	@Override
	public Supplier<InputStream> getContentStream() {
		return () -> new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public String toString() {
		return "FieldPart [name=" + name + ", value=" + value + "]";
	}
}
