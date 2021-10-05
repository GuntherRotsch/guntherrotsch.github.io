package io.github.guntherrotsch.simpledi;

import java.util.function.Supplier;

public class LazilyInitialized<T> implements Supplier<T> {

	private T instance;

	private Supplier<T> delegate;

	private LazilyInitialized(Supplier<T> delegate) {
		this.delegate = delegate;
	}

	public static <T> Supplier<T> of(Supplier<T> delegate) {
		return new LazilyInitialized<>(delegate);
	}

	@Override
	public T get() {
		synchronized (delegate) {
			if (instance == null) {
				instance = delegate.get();
			}
			return instance;
		}
	}
}
