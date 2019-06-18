package ca.bc.gov.iamp.email.util;

public interface ExponentialBackOffFunction<T> {
	T execute();
}
