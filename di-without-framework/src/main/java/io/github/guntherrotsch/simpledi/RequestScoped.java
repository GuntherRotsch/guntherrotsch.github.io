package io.github.guntherrotsch.simpledi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public final class RequestScoped<T> implements Supplier<T> {

	private static ThreadLocal<Map<Class<?>, Object>> instances = new ThreadLocal<>();

	private final Supplier<T> delegate;
	private final Class<T> clazz;

	private RequestScoped() {
		this.delegate = null;
		this.clazz=null;
	}

	private RequestScoped(Supplier<T> delegate, Class<T> clazz) {
		this.delegate = delegate;
		this.clazz = clazz;
	}

	public static <T> RequestScoped<T> of(Supplier<T> delegate, Class<T> clazz) {
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
		return clazz.cast(instances.get().computeIfAbsent(clazz, clazz -> delegate.get()));
	}

	void start() {
		synchronized (instances) {
			if (instances.get() != null) {
				throw new RequestScopeAlreadyActiveException();
			}
			instances.set(new ConcurrentHashMap<Class<?>, Object>());
		}
	}

	void stop() {
		synchronized (instances) {
			instances.set(null);
		}
	}
}
