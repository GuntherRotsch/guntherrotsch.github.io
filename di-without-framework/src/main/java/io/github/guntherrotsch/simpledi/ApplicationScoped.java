package io.github.guntherrotsch.simpledi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ApplicationScoped<T> implements Supplier<T> {

	private static Map<Class<?>, Supplier<?>> suppliers= new ConcurrentHashMap<>();
	private static Map<Class<?>, Object> instances = new ConcurrentHashMap<>();

	private Class<T> clazz;

	private ApplicationScoped(Supplier<T> supplier, Class<T> clazz) {
		suppliers.putIfAbsent(clazz, supplier);
		this.clazz = clazz;
	}

	public static <T> ApplicationScoped<T> of(Supplier<T> delegate, Class<T> clazz) {
		return new ApplicationScoped<>(delegate, clazz);
	}

	@Override
	public T get() {
		return clazz.cast(instances.computeIfAbsent(clazz, clazz -> suppliers.get(clazz).get()));
	}
}
