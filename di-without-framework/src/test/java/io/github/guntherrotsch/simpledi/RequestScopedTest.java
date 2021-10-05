package io.github.guntherrotsch.simpledi;

import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RequestScopedTest {

	static class Bean {

		final String message;

		Bean(String message) {
			this.message = message;
			System.out.println("Bean instantiated");
		}

		static Supplier<Bean> supplier(String message) {
			return RequestScoped.of(() -> new Bean(message), Bean.class);
		}

		void sayHello() {
			System.out.println(message);
		}
	}

	@Test
	void requstScopedBeansRequireContext() {
		Supplier<Bean> bean = Bean.supplier("Hello world");

		Assertions.assertThrows(RequestScopeNotActiveException.class, () -> bean.get());
	}

	@Test
	void requstScopedBeansPoduced() throws Exception {
		Supplier<Bean> bean = Bean.supplier("Hello world");

		try (RequestContext context = RequestScoped.getContext()) {
			bean.get().sayHello();
		}
	}
}
