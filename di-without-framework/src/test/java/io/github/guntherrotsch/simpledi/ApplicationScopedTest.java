package io.github.guntherrotsch.simpledi;

import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ApplicationScopedTest {

	static class Bean {

		final String message;

		Bean(String message) {
			this.message = message;
			System.out.println("Bean instantiated");
		}

		static Supplier<Bean> supplier(String message) {
			return ApplicationScoped.of(() -> new Bean(message), Bean.class);
		}

		void sayHello() {
			System.out.println(message);
		}
	}

	@Test
	void ensuresSingletonBeans() {
		Bean b1 = Bean.supplier("Hello World").get();
		Bean b2 = Bean.supplier("Hello Dude").get();

		b1.sayHello();
		b2.sayHello();

		Assertions.assertTrue(b1 == b2);
	}
}
