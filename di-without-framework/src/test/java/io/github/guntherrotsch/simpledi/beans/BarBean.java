package io.github.guntherrotsch.simpledi.beans;

import java.util.function.Supplier;

import io.github.guntherrotsch.simpledi.LazilyInitialized;

public class BarBean {
	private final Supplier<Config> configSupplier;
	private final Supplier<MessageCollector> messageSupplier;

	private BarBean(Supplier<Config> configSupplier, Supplier<MessageCollector> messageSupplier) {
		this.configSupplier = configSupplier;
		this.messageSupplier = messageSupplier;
	}

	public static Supplier<BarBean> supplier(Supplier<Config> configSupplier,
			Supplier<MessageCollector> messageSupplier) {
		return LazilyInitialized.of(() -> new BarBean(configSupplier, messageSupplier));
	}

	public void sayHello() {
		messageSupplier.get().putMessage(configSupplier.get().getConfigValue("bar.message"));
	}
}
