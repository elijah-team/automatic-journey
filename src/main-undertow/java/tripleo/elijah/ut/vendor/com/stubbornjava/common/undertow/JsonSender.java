package tripleo.elijah.ut.vendor.com.stubbornjava.common.undertow;

import io.undertow.server.*;
import io.undertow.util.Headers;
import tripleo.elijah.ut.vendor.com.stubbornjava.common.*;

import java.nio.*;

public interface JsonSender {

	default void sendJson(HttpServerExchange exchange, Object obj) {
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
		exchange.getResponseSender().send(ByteBuffer.wrap(Json.serializer().toByteArray(obj)));
	}
}
