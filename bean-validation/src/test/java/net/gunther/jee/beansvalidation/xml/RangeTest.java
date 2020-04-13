package net.gunther.jee.beansvalidation.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;
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

	@Test
	void reportsMinGreaterThanMaxViolation() {
		testee = new Range();
		testee.setMin(10);
		testee.setMax(9);

		Set<ConstraintViolation<Range>> actual = validator.validate(testee);

		assertEquals(1, actual.size());
		ConstraintViolation<Range> actualViolation = actual.iterator().next();
		System.out.println("Actual Constraint Violation: " + actualViolation);
		assertEquals("min must be less than or equal to max", actualViolation.getMessage());
	}

	@Test
	void reportsFieldAndClassViolations() {
		testee = new Range();
		testee.setMin(10);
		testee.setMax(0);

		Set<ConstraintViolation<Range>> actual = validator.validate(testee);

		assertEquals(2, actual.size());
		Iterator<ConstraintViolation<Range>> iter = actual.iterator();
		while (iter.hasNext()) {
			ConstraintViolation<Range> actualViolation = iter.next();
			System.out.println("Actual Constraint Violation: " + actualViolation);
			if ("".equals(actualViolation.getPropertyPath().toString())) {
				assertEquals("min must be less than or equal to max", actualViolation.getMessage());
			} else if ("max".equals(actualViolation.getPropertyPath().toString())) {
				assertEquals("must be greater than or equal to 1", actualViolation.getMessage());
			} else
				fail("Unexpected property path");
		}
	}
}
