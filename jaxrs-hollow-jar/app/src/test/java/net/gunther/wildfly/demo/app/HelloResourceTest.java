package net.gunther.wildfly.demo.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HelloResourceTest {

	HelloResource testee = new HelloResource();

	@Test
	void returnsHelloEntity() {

		Response actual = testee.sayHello();

		assertEquals("Hello from Wildfly JAR\n", actual.getEntity());
	}
}
