package net.gunther.jee.beansvalidation.xml;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import net.gunther.blog.codegen.models.Range;

public class RangeValidator implements ConstraintValidator<CheckMinLessOrEqualMax, Range> {
	
	public void initialize(CheckMinLessOrEqualMax arg0) {
	}

	public boolean isValid(Range range, ConstraintValidatorContext context) {
		return range.getMin() <= range.getMax();
	}
}
