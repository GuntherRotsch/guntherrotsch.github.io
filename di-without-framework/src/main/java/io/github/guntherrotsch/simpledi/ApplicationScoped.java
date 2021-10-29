package io.github.guntherrotsch.simpledi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public final class ApplicationScoped<T> implements Supplier<T> {

	private static Map<Class<?>, Object> instances = new ConcurrentHashMap<>();

	private final Supplier<T> delegate;
	private final Class<T> clazz;

	private ApplicationScoped(Supplier<T> delegate, Class<T> clazz) {
		this.delegate = delegate;
		this.clazz = clazz;
	}

	public static <T> ApplicationScoped<T> of(Supplier<T> delegate, Class<T> clazz) {
		return new ApplicationScoped<>(delegate, clazz);
	}

	@Override
	public T get() {
		return clazz.cast(instances.computeIfAbsent(clazz, clazz -> delegate.get()));
	}
}
