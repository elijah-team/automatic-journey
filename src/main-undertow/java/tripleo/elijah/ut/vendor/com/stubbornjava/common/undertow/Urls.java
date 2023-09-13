package tripleo.elijah.ut.vendor.com.stubbornjava.common.undertow;

import io.undertow.server.*;
import okhttp3.*;
import tripleo.elijah.ut.vendor.com.stubbornjava.common.*;

public interface Urls {

	// {{start:currentUrl}}
	default HttpUrl currentUrl(HttpServerExchange exchange) {
		HttpUrl.Builder urlBuilder = HttpUrl.parse(exchange.getRequestURL()).newBuilder();

		if (!"".equals(exchange.getQueryString())) {
			urlBuilder = urlBuilder.encodedQuery(exchange.getQueryString());
		}
		return urlBuilder.build();
	}
	// {{end:currentUrl}}

	default HttpUrl host(HttpServerExchange exchange) {
		HttpUrl url = HttpUrl.parse(exchange.getRequestURL());
		return HttpUrls.host(url);
	}
}
