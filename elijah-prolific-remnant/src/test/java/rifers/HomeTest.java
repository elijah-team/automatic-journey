package rifers;

import org.junit.*;
import rife.test.MockConversation;

import static org.junit.Assert.assertEquals;

public class HomeTest {
	@Test
	public void verifyHomePage() {
		var m = new MockConversation(new RifersSite());
		assertEquals(
				"RIFE2 Framework : Full-stack, no-declaration, framework to quickly and effortlessly create web applications with modern Java.",
				m.doRequest("/").getParsedHtml().getTitle());
	}
}
