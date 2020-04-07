package com.kayakwise.gray.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Closer {

	private static final Logger log = LoggerFactory.getLogger(Closer.class);

	private Closer() {
	}

	public static void close(final AutoCloseable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				log.warn("Close [{}] throw exception!", closeable.getClass().getName());
			}
		}
	}

	public static void close(final AutoCloseable... closeables) {
		if (closeables != null && closeables.length > 0) {
			for (AutoCloseable closeable : closeables) {
				try {
					closeable.close();
				} catch (Exception e) {
					log.warn("Close [{}] throw exception!", closeable.getClass().getName());
				}
			}
		}
	}

	public static boolean closeSilently(final AutoCloseable closeable) {
		try {
			close(closeable);
			return true;
		} catch (final Exception ignored) {
			return false;
		}
	}

}
