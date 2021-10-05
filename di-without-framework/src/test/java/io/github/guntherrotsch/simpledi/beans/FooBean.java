package io.github.guntherrotsch.simpledi.beans;

import java.util.function.Supplier;

import io.github.guntherrotsch.simpledi.ApplicationScoped;

public class FooBean {
	private final Supplier<Config> configSupplier;
	private final Supplier<MessageCollector> messageSupplier;

	private FooBean(Supplier<Config> configSupplier, Supplier<MessageCollector> messageSupplier) {
		this.configSupplier = configSupplier;
		this.messageSupplier = messageSupplier;
	}

	public static Supplier<FooBean> supplier(Supplier<Config> configSupplier,
			Supplier<MessageCollector> messageSupplier) {
		return ApplicationScoped.of(() -> new FooBean(configSupplier, messageSupplier), FooBean.class);
	}

	public void sayHello() {
		messageSupplier.get().putMessage(configSupplier.get().getConfigValue("foo.message"));
	}
}
