package net.gunther.jee.beansvalidation.cdi;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;

import net.gunther.blog.codegen.models.Range;

public class ValidateableRange {

	private final Range range;

	public ValidateableRange(Range range) {
		this.range = range;
	}

	@Valid
	public Range getRange() {
		return range;
	}

	@AssertTrue(message = "min must be less than or equal to max")
	public boolean isMinLessThanOrEqualToMax() {
		return range.getMin() <= range.getMax();
	}
}
