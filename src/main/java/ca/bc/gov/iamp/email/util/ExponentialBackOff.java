package ca.bc.gov.iamp.email.util;

import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSendException;

import com.sun.mail.util.MailConnectException;

import ca.bc.gov.iamp.api.exception.ServiceInternalException;

public final class ExponentialBackOff {

	private final Logger log = LoggerFactory.getLogger(ExponentialBackOff.class);
	private static final int[] FIBONACCI = new int[] { 1, 1, 2, 3, 5, 8, 13 };
	private static final List<Class<? extends Exception>> EXPECTED_COMMUNICATION_ERRORS = Arrays
			.asList(MailConnectException.class, SocketTimeoutException.class);
	private Random r = new Random();

	public ExponentialBackOff() {
		super();
	}

	public <T> T execute(ExponentialBackOffFunction<T> fn) {
		for (int attempt = 0; attempt < FIBONACCI.length; attempt++) {
			try {
				return fn.execute();
			} catch (Exception e) {
				handleFailure(attempt, e);
			}
		}
		throw new ServiceInternalException(String.format("Failed to execute after %s attempts.", FIBONACCI.length - 1));
	}

	private void handleFailure(int attempt, Exception e) {
		log.debug("Failed to execute");
		if (attempt != (FIBONACCI.length - 1) && validException(e)) {
			doWait(attempt);
		} else {
			throw new ServiceInternalException(String.format("Failed to execute after %s attempts.", attempt + 1), e,
					Collections.emptyList());
		}
	}

	private boolean validException(Exception e) {
		if (e.getCause() != null && EXPECTED_COMMUNICATION_ERRORS.contains(e.getCause().getClass())) {
			return true;
		} else if (e instanceof MailSendException) {
			return e.toString().contains("SocketTimeoutException");
		}
		return false;
	}

	private void doWait(int attempt) {
		try {
			Long timeToWait = FIBONACCI[attempt] * 1000L + (r.nextInt(1000));
			log.debug("Trying again in {} milliseconds", timeToWait);
			Thread.sleep(timeToWait);
		} catch (InterruptedException e) {
			throw new ServiceInternalException(e);
		}
	}
}
