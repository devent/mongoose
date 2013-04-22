package com.anrisoftware.mongoose.threads;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notEmpty;

import java.util.concurrent.Executors;

/**
 * Different threading pool policies.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 * 
 * @see Executors
 */
public enum ThreadingPolicy {

	/**
	 * Cached thread pool.
	 * 
	 * @see Executors#newCachedThreadPool()
	 * @see Executors#newCachedThreadPool(java.util.concurrent.ThreadFactory)
	 */
	CACHED,

	/**
	 * Fixed thread pool.
	 * 
	 * @see Executors#newFixedThreadPool(int)
	 * @see Executors#newFixedThreadPool(int,
	 *      java.util.concurrent.ThreadFactory)
	 */
	FIXED,

	/**
	 * Single thread pool.
	 * 
	 * @see Executors#newSingleThreadExecutor()
	 * @see Executors#newSingleThreadExecutor(java.util.concurrent.ThreadFactory)
	 */
	SINGLE;

	private static final String NO_POLICY_FOUND = "No policy '%s' found.";
	private static final String POLICY_NAME_NULL = "Policy name cannot be null or empty.";

	/**
	 * Returns the policy with the given name.
	 * 
	 * @param name
	 *            the policy {@link String} name.
	 * 
	 * @return the {@link ThreadingPolicy}.
	 * 
	 * @throws NullPointerException
	 *             if the specified name is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified name is empty; if no threading policy could
	 *             be found.
	 */
	public static ThreadingPolicy parsePolicy(String name) {
		name = name.trim();
		notEmpty(name, POLICY_NAME_NULL);
		ThreadingPolicy policy = findPolicy(name);
		isTrue(policy != null, NO_POLICY_FOUND, name);
		return policy;
	}

	private static ThreadingPolicy findPolicy(String value) {
		for (ThreadingPolicy policy : values()) {
			if (value.equalsIgnoreCase(policy.name())) {
				return policy;
			}
		}
		return null;
	}
}
