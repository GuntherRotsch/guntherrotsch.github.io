package com.github.guntherrotsch.mvc.demo;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mvc.MvcContext;
import javax.mvc.engine.ViewEngine;
import javax.mvc.engine.ViewEngineContext;
import javax.mvc.engine.ViewEngineException;
import javax.servlet.http.HttpServletRequest;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

@ApplicationScoped
@Priority(ViewEngine.PRIORITY_APPLICATION)
public class FreeMarkerViewEngine implements ViewEngine {

	// FreeMarker configuration is initialized lazily
	private volatile Configuration cfg;

	@Inject
	MvcContext mvcContext;

	@Override
	public boolean supports(String view) {
		return view.endsWith(".ftl");
	}

	@Override
	public void processView(ViewEngineContext context) throws ViewEngineException {
		ensureInit(context);

		Map<String, Object> modelsMap = new HashMap<>(context.getModels().asMap());
		// Add the 'mvcContext' bean with key 'mvc' to the models map
		modelsMap.put("mvc", mvcContext);
		// Add an instance of 'NamedBeanResolver' with key 'named' to the models map
		modelsMap.put("named", new NamedBeanResolver());
		// Add the 'request' to the models map
		modelsMap.put("request", context.getRequest(HttpServletRequest.class));

		try {
			Template temp = cfg.getTemplate(context.getView());
			/* Merge data-model with template */
			Writer out = new OutputStreamWriter(context.getOutputStream());
			temp.process(modelsMap, out);
		} catch (IOException | TemplateException e) {
			throw new ViewEngineException(e);
		}
	}

	private void ensureInit(ViewEngineContext context) {
		if (cfg == null) {
			synchronized (FreeMarkerViewEngine.class) {
				if (cfg == null) {
					HttpServletRequest httpRequest = context.getRequest(HttpServletRequest.class);
					Map<String, Object> appConfig = context.getConfiguration().getProperties();
					Object viewFolderConfig = appConfig.get(ViewEngine.VIEW_FOLDER);
					String viewFolder = viewFolderConfig != null ? viewFolderConfig.toString() : DEFAULT_VIEW_FOLDER;

					// Create your Configuration instance, and specify if up to what FreeMarker
					// version (here 2.3.29) do you want to apply the fixes that are not 100%
					// backward-compatible. See the Configuration JavaDoc for details.
					cfg = new Configuration(Configuration.VERSION_2_3_29);

					// Specify the source where the template files come from. Here I set a
					// plain directory for it, but non-file-system sources are possible too:
					cfg.setServletContextForTemplateLoading(httpRequest.getServletContext(), viewFolder);

					// From here we will set the settings recommended for new projects. These
					// aren't the defaults for backward compatibility.

					// Set the preferred charset template files are stored in. UTF-8 is
					// a good choice in most applications:
					cfg.setDefaultEncoding("UTF-8");

					// Sets how errors will appear.
					// During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is
					// better.
					cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

					// Don't log exceptions inside FreeMarker that it will thrown at you anyway:
					cfg.setLogTemplateExceptions(false);

					// Wrap unchecked exceptions thrown during template processing into
					// TemplateException-s:
					cfg.setWrapUncheckedExceptions(true);

					// Do not fall back to higher scopes when reading a null loop variable:
					cfg.setFallbackOnNullLoopVariable(false);
				}
			}
		}
	}
}
