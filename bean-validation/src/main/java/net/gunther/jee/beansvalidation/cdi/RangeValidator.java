package net.gunther.jee.beansvalidation.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;

@ApplicationScoped
public class RangeValidator {

	public void validate(@Valid ValidateableRange validateableRange) {
		// no-op
	}
}
