package net.gunther.wildfly.demo.app;

import java.io.IOException;
import java.io.InputStream;

/**
 * PartInputStream filters a given InputStream till the boundary also given to
 * constructor. The InputStream need to support #mark and #reset methods to
 * enable 'read-ahead' of characters. This allows detection of the given
 * boundary, but still keep the state of the InputStream if only beginning parts
 * of the boundary match.
 *
 */
public class PartInputStream extends InputStream {

	public static final int EOF = -1;
	public static final int CR = 13;
	public static final int LF = 10;

	private InputStream inputStream;
	private byte[] boundary;

	private boolean endOfPart = false;
	private boolean lastPart = false;

	public PartInputStream(InputStream inputStream, byte[] boundary) {
		if (!inputStream.markSupported()) {
			throw new IllegalArgumentException("InputStream need to support mark and reset methods.");
		}
		this.inputStream = inputStream;
		this.boundary = boundary;
	}

	@Override
	public int read() throws IOException {
		if (endOfPart) {
			return EOF;
		}
		int c = inputStream.read();
		int saved = c;

		inputStream.mark(boundary.length);

		// the following loop is about to match the boundary, whereby the pos variable
		// is the current position in the boundary array
		int pos = 0;
		do {
			if (c == boundary[pos]) {
				// in sequence of boundary
				pos++;
				if (pos == boundary.length) {
					// the entire boundary sequence matched with input
					endOfPart = true;
					int c1 = checkForLastPart();
					// consume everything till next CR/LF sequence (including) or end of stream
					// (whichever comes first)
					int c2 = inputStream.read();
					while (c2 > 0 && !(c1 == CR && c2 == LF)) {
						c1 = c2;
						c2 = inputStream.read();
					}
					return EOF;
				}
				// read the next byte to be matched with the boundary
				c = inputStream.read();
			} else {
				// the input does at this point not match with the boundary, i.e. we reset the
				// stream (and eventually return the initially saved byte)
				inputStream.reset();
				break;
			}
		} while (c != EOF);

		return saved;
	}

	/**
	 * Checks for last part and returns the next byte of the input stream.
	 */
	private int checkForLastPart() throws IOException {
		int c = inputStream.read();
		// check for last part
		if (c == '-') {
			c = inputStream.read();
			if (c == '-') {
				lastPart = true;
				c = inputStream.read();
			}
		}
		return c;
	}

	public boolean isLastPart() {
		return lastPart;
	}
}
