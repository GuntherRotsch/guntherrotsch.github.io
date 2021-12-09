package com.github.guntherrotsch.mvc.demo;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;

public class NamedAnnotation extends AnnotationLiteral<Named> implements Named {

	private static final long serialVersionUID = 1L;

	private final String value;

	public NamedAnnotation(final String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
