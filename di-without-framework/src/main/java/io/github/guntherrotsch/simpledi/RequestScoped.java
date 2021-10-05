package io.github.guntherrotsch.simpledi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class RequestScoped<T> implements Supplier<T> {

	private static Map<Class<?>, Supplier<?>> suppliers = new ConcurrentHashMap<>();
	private static ThreadLocal<Map<Class<?>, Object>> instances = new ThreadLocal<>();

	private Class<T> clazz;

	private RequestScoped() {
	}

	private RequestScoped(Supplier<T> supplier, Class<T> clazz) {
		suppliers.putIfAbsent(clazz, supplier);
		this.clazz = clazz;
	}

	public static <T> RequestScoped<T> of(Supplier<T> delegate, Class<T> clazz) {
//		if (suppliers.get(clazz) != null) {
//			throw new RuntimeException("Ambiguous registration of application scoped bean");
//		}
		return new RequestScoped<>(delegate, clazz);
	}

	public static RequestContext getContext() {
		return new RequestContext(new RequestScoped<>());
	}

	@Override
	public T get() {
		if (instances.get() == null) {
			throw new RequestScopeNotActiveException();
		}
		return clazz.cast(instances.get().computeIfAbsent(clazz, clazz -> suppliers.get(clazz).get()));
	}

	public void start() {
		synchronized (instances) {
			if (instances.get() != null) {
				throw new RuntimeException("Request scope is already active.");
			}
			instances.set(new ConcurrentHashMap<Class<?>, Object>());
		}
	}

	public void stop() {
		synchronized (instances) {
			instances.set(null);
		}
	}
}
