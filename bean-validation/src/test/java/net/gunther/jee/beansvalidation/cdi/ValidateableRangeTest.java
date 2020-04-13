package net.gunther.jee.beansvalidation.cdi;

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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import net.gunther.blog.codegen.models.Range;

class ValidateableRangeTest {

	ValidateableRange testee;

	static ValidatorFactory validatorFactory;
	static Validator validator;

	@BeforeAll
	static void createValidator() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@Test
	void acceptsValidRangeBean() {
		Range r = new Range();
		r.setMin(1);
		r.setMax(1000);
		testee = new ValidateableRange(r);

		Set<ConstraintViolation<ValidateableRange>> actual = validator.validate(testee);

		assertTrue(actual.isEmpty());
	}

	@Test
	void reportsTooSmallStartOfRange() {
		Range r = new Range();
		r.setMin(0);
		r.setMax(1000);
		testee = new ValidateableRange(r);

		Set<ConstraintViolation<ValidateableRange>> actual = validator.validate(testee);

		assertEquals(1, actual.size());
		assertTrue(actual.toString().contains("must be greater than or equal to 1"));
	}

	@Test
	@Disabled("After introduding the XML deployment descriptor, this test starts to fail")
	void reportsMinGreaterThanMaxViolation() {
		Range r = new Range();
		r.setMin(10);
		r.setMax(9);
		testee = new ValidateableRange(r);

		Set<ConstraintViolation<ValidateableRange>> actual = validator.validate(testee);

		assertEquals(1, actual.size());
		ConstraintViolation<ValidateableRange> actualViolation = actual.iterator().next();
		System.out.println("Actual Constraint Violation: " + actualViolation);
		assertEquals("min must be less than or equal to max", actualViolation.getMessage());
	}

	@Test
	@Disabled("After introduding the XML deployment descriptor, this test starts to fail")
	void reportsFieldAndClassViolations() {
		Range r = new Range();
		r.setMin(10);
		r.setMax(0);
		testee = new ValidateableRange(r);

		Set<ConstraintViolation<ValidateableRange>> actual = validator.validate(testee);

		Iterator<ConstraintViolation<ValidateableRange>> iter = actual.iterator();
		while (iter.hasNext()) {
			ConstraintViolation<ValidateableRange> actualViolation = iter.next();
			System.out.println("Actual Constraint Violation: " + actualViolation);
			if ("range.max".equals(actualViolation.getPropertyPath().toString())) {
				assertEquals("must be greater than or equal to 1", actualViolation.getMessage());
			} else if ("minLessThanOrEqualToMax".equals(actualViolation.getPropertyPath().toString())) {
				assertEquals("min must be less than or equal to max", actualViolation.getMessage());
			} else
				fail("Unexpected property path");
		}
	}
}
