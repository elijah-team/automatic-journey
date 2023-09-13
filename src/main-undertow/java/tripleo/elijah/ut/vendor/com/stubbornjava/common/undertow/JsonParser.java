package tripleo.elijah.ut.vendor.com.stubbornjava.common.undertow;

import com.fasterxml.jackson.core.type.*;
import io.undertow.server.*;
import tripleo.elijah.ut.vendor.com.stubbornjava.common.*;

public interface JsonParser {

	default <T> T parseJson(HttpServerExchange exchange, TypeReference<T> typeRef) {
		return Json.serializer().fromInputStream(exchange.getInputStream(), typeRef);
	}
}
