package net.gunther.jee.beansvalidation.cdi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.gunther.blog.codegen.models.Range;

class RangeTest {

	Range testee;

	static ValidatorFactory validatorFactory;
	static Validator validator;

	@BeforeAll
	static void createValidator() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@Test
	void acceptsValidRangeBean() {
		testee = new Range();
		testee.setMin(1);
		testee.setMax(1000);

		Set<ConstraintViolation<Range>> actual = validator.validate(testee);

		assertTrue(actual.isEmpty());
	}

	@Test
	void reportsTooSmallStartOfRange() {
		testee = new Range();
		testee.setMin(0);
		testee.setMax(1000);

		Set<ConstraintViolation<Range>> actual = validator.validate(testee);

		assertEquals(1, actual.size());
		assertTrue(actual.toString().contains("must be greater than or equal to 1"));
	}
}
