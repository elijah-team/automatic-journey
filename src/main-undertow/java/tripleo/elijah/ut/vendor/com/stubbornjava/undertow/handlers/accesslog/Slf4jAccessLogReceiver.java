package tripleo.elijah.ut.vendor.com.stubbornjava.undertow.handlers.accesslog;

import io.undertow.server.handlers.accesslog.*;
import org.slf4j.*;

public class Slf4jAccessLogReceiver implements AccessLogReceiver {
	private final Logger logger;

	public Slf4jAccessLogReceiver(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void logMessage(String message) {
		logger.info("{}", message);
	}
}
