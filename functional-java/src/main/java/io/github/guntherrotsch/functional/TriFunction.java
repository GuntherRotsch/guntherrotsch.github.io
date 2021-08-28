package io.github.guntherrotsch.functional;

import java.util.function.Function;

/**
 * Represents a function that accepts three arguments and produces a result.
 * This is a three-arity extension to the functional interfaces {@link Function}
 * and {@link java.util.function.BiFunction BiFunction} from the JDK.
 *
 * @param <T>
 *          the type of the first argument to the function
 * @param <U>
 *          the type of the second argument to the function
 * @param <V>
 *          the type of the third argument to the function
 * @param <R>
 *          the type of the result of the function
 */
public interface TriFunction<T, U, V, R> {
  R apply(T t, U u, V v);
}
