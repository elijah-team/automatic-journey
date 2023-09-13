package tripleo.elijah.ut.vendor.com.stubbornjava.common;

import okhttp3.*;

import java.util.*;

public class HttpUrls {

	public static HttpUrl host(HttpUrl url) {
		List<String>    pathSegments = url.pathSegments();
		HttpUrl.Builder urlBuilder   = url.newBuilder();
		for (int i = pathSegments.size() - 1; i >= 0; i--) {
			urlBuilder.removePathSegment(i);
		}
		urlBuilder.query(null);
		return urlBuilder.build();
	}
}
