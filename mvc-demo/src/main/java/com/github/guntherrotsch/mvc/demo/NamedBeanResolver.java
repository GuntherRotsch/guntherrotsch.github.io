package com.github.guntherrotsch.mvc.demo;

import java.util.List;

import javax.enterprise.inject.spi.CDI;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.Version;

public class NamedBeanResolver implements TemplateMethodModelEx {

	@Override
	@SuppressWarnings("rawtypes")
	public TemplateModel exec(List args) throws TemplateModelException {
		if (args.size() != 1) {
			throw new TemplateModelException("Wrong arguments");
		}
		SimpleScalar beanName = (SimpleScalar) args.get(0);
		Object namedBean = CDI.current().select(Object.class, new NamedAnnotation(beanName.getAsString())).get();
		DefaultObjectWrapper objectWrapper = new DefaultObjectWrapperBuilder(new Version("2.3.31")).build();
		TemplateModel result = objectWrapper.wrap(namedBean);
		return result;
	}
}
