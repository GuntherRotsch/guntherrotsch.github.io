package io.github.guntherrotsch.simpledi;

import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EagerlyInitializedTest {

	static class Bean {

		static int instanceCount = 0;

		Bean() {
			instanceCount++;
			System.out.println("Bean instantiated");
		}

		static Supplier<Bean> supplier() {
			return EagerlyInitialized.of(Bean::new);
		}

		void sayHello() {
			System.out.println("Hello world");
		}
	}

	@Test
	void beanControlsLazyInitialization() {
		Supplier<Bean> testee = Bean.supplier();

		Assertions.assertEquals(1, Bean.instanceCount);
		testee.get().sayHello();
		Assertions.assertEquals(1, Bean.instanceCount);
		testee.get().sayHello();
		Assertions.assertEquals(1, Bean.instanceCount);
		testee.get().sayHello();
		Assertions.assertEquals(1, Bean.instanceCount);
	}
}
