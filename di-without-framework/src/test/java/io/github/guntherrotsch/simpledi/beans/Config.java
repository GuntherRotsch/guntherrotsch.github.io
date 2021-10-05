package io.github.guntherrotsch.simpledi.beans;

import java.util.Properties;
import java.util.function.Supplier;

import io.github.guntherrotsch.simpledi.ApplicationScoped;

public class Config {

	private static final Properties props = new Properties();
	static {
		// Configuration comes usually from other source, e.g. a configuration file
		props.put("foo.message", "Hello from Foo");
		props.put("bar.message", "Hello from Bar");
		props.put("baz.message", "Hello from Baz");
	}

	public static Supplier<Config> supplier() {
		return ApplicationScoped.of(Config::new, Config.class);
	}

	public String getConfigValue(String configName) {
		System.out.println("Config#getConfigValue called from "+Thread.currentThread().getName());
		return props.getProperty(configName);
	}
}
