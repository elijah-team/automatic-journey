package tripleo.elijah.ut.vendor.com.stubbornjava.undertow.handlers;

import io.undertow.server.*;
import io.undertow.server.handlers.*;
import io.undertow.util.*;

// {{start:handler}}
public class StrictTransportSecurityHandlers {

	public static HttpHandler hsts(HttpHandler next, long maxAge) {
		return new SetHeaderHandler(next, Headers.STRICT_TRANSPORT_SECURITY_STRING, "max-age=" + maxAge);
	}

	public static HttpHandler hstsIncludeSubdomains(HttpHandler next, long maxAge) {
		return new SetHeaderHandler(next, Headers.STRICT_TRANSPORT_SECURITY_STRING, "max-age=" + maxAge + "; includeSubDomains");
	}
}
// {{end:handler}}
