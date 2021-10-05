package io.github.guntherrotsch.simpledi;

import java.util.function.Supplier;

public class EagerlyInitialized<T> implements Supplier<T> {

	private T instance;

	private EagerlyInitialized(Supplier<T> delegate) {
		this.instance = delegate.get();
	}

	public static <T> Supplier<T> of(Supplier<T> delegate) {
		return new EagerlyInitialized<>(delegate);
	}

	@Override
	public T get() {
		return instance;
	}
}
