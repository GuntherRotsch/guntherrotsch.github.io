package io.github.guntherrotsch.functional;

import java.util.function.BiFunction;
import java.util.function.Function;

public class PartialApplication {

	  // Simple partial application (of first argument)
	  public static <T, U, R> Function<U, R> partial(BiFunction<T, U, R> f, T x) {
	    return (y) -> f.apply(x, y);
	  }

	  // Partial application of first or second argument
	  public static <T, U, R> Function<U, R> applyFirst(BiFunction<T, U, R> f, T x) {
	    return (y) -> f.apply(x, y);
	  }

	  public static <T, U, R> Function<T, R> applySecond(BiFunction<T, U, R> f, U x) {
	    return (y) -> f.apply(y, x);
	  }

	  public static <T, U, R> Function<U, R> partial(BiFunction<T, U, R> f, T x, Matches m) {
	    return (y) -> f.apply(x, y);
	  }

	  public static <T, U, R> Function<T, R> partial(BiFunction<T, U, R> f, Matches m, U x) {
	    return (y) -> f.apply(y, x);
	  }

	  // Partial application for TriFunction


	  public static <T, U, V, R> BiFunction<U, V, R> partial(TriFunction<T, U, V, R> f, T a1, Matches m2,
	      Matches m3) {
	    return (x, y) -> f.apply(a1, x, y);
	  }

	  public static <T, U, V, R> BiFunction<T, V, R> partial(TriFunction<T, U, V, R> f, Matches m1, U a2,
	      Matches m3) {
	    return (x, y) -> f.apply(x, a2, y);
	  }

	  public static <T, U, V, R> BiFunction<T, U, R> partial(TriFunction<T, U, V, R> f, Matches m1,
	      Matches m2, V a3) {
	    return (x, y) -> f.apply(x, y, a3);
	  }

	  // Partial application for QuadFunction

	  public static <T, U, V, W, R> TriFunction<U, V, W, R> partial(QuadFunction<T, U, V, W, R> f, T a1,
	      Matches m2, Matches m3, Matches a4) {
	    return (x, y, z) -> f.apply(a1, x, y, z);
	  }

	  public static <T, U, V, W, R> TriFunction<T, V, W, R> partial(QuadFunction<T, U, V, W, R> f,
	      Matches m1, U a2, Matches m3, Matches a4) {
	    return (x, y, z) -> f.apply(x, a2, y, z);
	  }

	  public static <T, U, V, W, R> TriFunction<T, U, W, R> partial(QuadFunction<T, U, V, W, R> f,
	      Matches m1, Matches m2, V a3, Matches a4) {
	    return (x, y, z) -> f.apply(x, y, a3, z);
	  }

	  public static <T, U, V, W, R> TriFunction<T, U, V, R> partial(QuadFunction<T, U, V, W, R> f,
	      Matches m1, Matches m2, Matches m3, W a4) {
	    return (x, y, z) -> f.apply(x, y, z, a4);
	  }

	  // QuintFunction would be the next... then Sext, Sept, Oct, and Non/Nov
}
