package net.gunther.wildfly.demo.app;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class PartInputStreamTest {

	PartInputStream testee;

	// @formatter:off
	String boundary = "\r\n--123456789abc";
	String body1 = "Content-Disposition: form-data, name=testcase1\r\n"
			+ "\r\n"
			+ "--123456789 this content starts like the boundary\r\n"
			+ "--123456789abc\r\n";
	String expected1 = "Content-Disposition: form-data, name=testcase1\r\n"
			+ "\r\n"
			+ "--123456789 this content starts like the boundary";
	
	String body2 = "Content-Disposition: form-data, name=testcase1\r\n"
			+ "\r\n"
			+ "--123456789 this content starts like the boundary\r\n"
			+ "--123456789abc\r\n"
			+ "Next Content";

	String body3 = "Content-Disposition: form-data, name=testcase1\r\n"
			+ "\r\n"
			+ "--123456789 this content starts like the boundary\r\n"
			+ "--123456789abc--";
	// @formatter:on

	@Test
	void readsPartContent() throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(body1.getBytes(StandardCharsets.UTF_8));
		testee = new PartInputStream(is, boundary.getBytes(StandardCharsets.UTF_8));
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		testee.transferTo(os);

//		System.out.println(os.toString(StandardCharsets.UTF_8));
		assertArrayEquals(os.toByteArray(), expected1.getBytes(StandardCharsets.UTF_8));
		assertEquals(PartInputStream.EOF, is.read());
	}

	@Test
	void positionToNextPart() throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(body2.getBytes(StandardCharsets.UTF_8));
		testee = new PartInputStream(is, boundary.getBytes(StandardCharsets.UTF_8));

		consumeInputStream(testee);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		is.transferTo(os);
//		System.out.println(os.toString(StandardCharsets.UTF_8));
		assertArrayEquals(os.toByteArray(), "Next Content".getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void detectsLastPart() throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(body3.getBytes(StandardCharsets.UTF_8));
		testee = new PartInputStream(is, boundary.getBytes(StandardCharsets.UTF_8));

		consumeInputStream(testee);

		assertTrue(testee.isLastPart());
	}

	private void consumeInputStream(InputStream is) throws IOException {
		while (is.read() != PartInputStream.EOF) {
			;
		}
	}
}
