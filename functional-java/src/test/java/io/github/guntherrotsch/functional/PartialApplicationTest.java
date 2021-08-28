package io.github.guntherrotsch.functional;

import static io.github.guntherrotsch.functional.PartialApplication.partial;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PartialApplicationTest {

	@Test
	void appliesParameterPartially() {
		BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
		Function<Integer, Integer> add3 = PartialApplication.partial(add, 3);
		assertEquals(6, add3.apply(3));
	}

	@Test
	void appliesFirstOrSecondNonCommutativeFunction() {
		BiFunction<Integer, Integer, Integer> divide = (a, b) -> a / b;
		Function<Integer, Integer> divideFourBy = PartialApplication.applyFirst(divide, 4);
		assertEquals(2, divideFourBy.apply(2));

		Function<Integer, Integer> divideByTwo = PartialApplication.applySecond(divide, 2);
		assertEquals(2, divideByTwo.apply(4));
	}

	@Test
	void partiallyAppliesAnyNonCommutativeFunction() {
		BiFunction<Integer, Integer, Integer> divide = (a, b) -> a / b;
		Function<Integer, Integer> divideByFour = partial(divide, Matches._any, 4);
		assertEquals(2, divideByFour.apply(8));

		Function<Integer, Integer> divideSixBy = partial(divide, 6, Matches._any);
		Assertions.assertEquals(3, divideSixBy.apply(2));
	}

	@Test
	void partiallyAppliesSimpleTriFunction() {
		TriFunction<Integer, String, List<String>, String> formatter = (indent, delimiter,
				strings) -> " ".repeat(indent) + strings.stream().collect(Collectors.joining(delimiter));
		BiFunction<Integer, List<String>, String> commaSeparatingFormatter = PartialApplication.partial(formatter,
				Matches._any, ",", Matches._any);

		assertEquals("   one,two,three", commaSeparatingFormatter.apply(3, Arrays.asList("one", "two", "three")));
	}

	@Test
	void partiallyAppliesTriFunctionCascaded() {
		TriFunction<Integer, String, List<String>, String> formatter = (indent, delimiter,
				strings) -> " ".repeat(indent) + strings.stream().collect(Collectors.joining(delimiter));
		Function<List<String>, String> indentThreeCommaSeparatingFormatter = partial(
				partial(formatter, Matches._any, ",", Matches._any), 3, Matches._any);

		assertEquals("   one,two,three",
				indentThreeCommaSeparatingFormatter.apply(Arrays.asList("one", "two", "three")));
	}

	@Test
	void partiallyAppliesQuadFunction() {
		QuadFunction<Integer, Character, String, List<String>, String> formatter = (indent, indentChar, delimiter,
				strings) -> indentChar.toString().repeat(indent)
						+ strings.stream().collect(Collectors.joining(delimiter));
		Function<List<String>, String> indentThreeCommaSeparatingFormatter = partial(
				partial(partial(formatter, Matches._any, Matches._any, ",", Matches._any), 3, Matches._any,
						Matches._any),
				Character.valueOf('.'), Matches._any);

		assertEquals("...one,two,three", indentThreeCommaSeparatingFormatter.apply(asList("one", "two", "three")));
	}
}
