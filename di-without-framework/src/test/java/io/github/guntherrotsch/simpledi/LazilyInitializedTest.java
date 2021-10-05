package io.github.guntherrotsch.simpledi;

import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LazilyInitializedTest {

	static class Bean {

		static int instanceCount = 0;

		Bean() {
			instanceCount++;
		}

		static void reset() {
			instanceCount = 0;
		}

		static Supplier<Bean> supplier() {
			return LazilyInitialized.of(Bean::new);
		}

		void sayHello() {
			System.out.println("Hello world");
		}
	}

	@BeforeEach
	void resetInstanceCount() {
		Bean.reset();
	}

	@Test
	void lazilyInitializesBean() {
		Supplier<Bean> testee = LazilyInitialized.of(Bean::new);

		Assertions.assertEquals(0, Bean.instanceCount);
		testee.get().sayHello();
		Assertions.assertEquals(1, Bean.instanceCount);
		testee.get().sayHello();
		Assertions.assertEquals(1, Bean.instanceCount);
	}

	@Test
	void beanControlsLazyInitialization() {
		Supplier<Bean> testee = Bean.supplier();

		Assertions.assertEquals(0, Bean.instanceCount);
		testee.get().sayHello();
		Assertions.assertEquals(1, Bean.instanceCount);
		testee.get().sayHello();
		Assertions.assertEquals(1, Bean.instanceCount);
	}
}
