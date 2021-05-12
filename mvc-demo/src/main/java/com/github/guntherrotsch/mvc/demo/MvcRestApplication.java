package com.github.guntherrotsch.mvc.demo;

import java.util.HashMap;
import java.util.Map;

import javax.mvc.engine.ViewEngine;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 */
@ApplicationPath("/app")
public class MvcRestApplication extends Application {

	@Override
	public Map<String, Object> getProperties() {
		Map<String, Object> props = new HashMap<>();
		props.put(ViewEngine.VIEW_FOLDER, "/WEB-INF/ftl_views");
		return props;
	}
}
