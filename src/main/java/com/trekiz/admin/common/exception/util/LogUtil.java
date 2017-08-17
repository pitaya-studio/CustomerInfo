package com.trekiz.admin.common.exception.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility class for logging.
 * 
 * @author J&eacute;r&ocirc;me Charron
 */
public class LogUtil {

	private final static Log LOG = LogFactory.getLog(LogUtil.class);

	private static Method TRACE = null;
	private static Method DEBUG = null;
	private static Method INFO = null;
	private static Method WARN = null;
	private static Method ERROR = null;
	private static Method FATAL = null;

	static {
		try {
			TRACE = Log.class.getMethod("trace", new Class[] { Object.class });
			DEBUG = Log.class.getMethod("debug", new Class[] { Object.class });
			INFO = Log.class.getMethod("info", new Class[] { Object.class });
			WARN = Log.class.getMethod("warn", new Class[] { Object.class });
			ERROR = Log.class.getMethod("error", new Class[] { Object.class });
			FATAL = Log.class.getMethod("fatal", new Class[] { Object.class });
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("Cannot init log methods", e);
			}
		}
	}

	public static PrintStream getErrorStream(final Log logger) {
		return getLogStream(logger, ERROR);
	}

	/** Returns a stream that, when written to, adds log lines. */
	private static PrintStream getLogStream(final Log logger,
			final Method method) {
		return new PrintStream(new ByteArrayOutputStream() {
			private int scan = 0;

			private boolean hasNewline() {
				for (; scan < count; scan++) {
					if (buf[scan] == '\n')
						return true;
				}
				return false;
			}

			public void flush() throws IOException {
				if (!hasNewline())
					return;
				try {
					method.invoke(logger, new Object[] { toString().trim() });
				} catch (Exception e) {
					if (LOG.isFatalEnabled()) {
						LOG.fatal("Cannot log with method [" + method + "]", e);
					}
				}
				reset();
				scan = 0;
			}
		}, true);
	}

}
