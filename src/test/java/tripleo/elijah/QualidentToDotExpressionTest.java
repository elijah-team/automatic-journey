package tripleo.elijah;

import org.junit.*;
import tripleo.elijah.lang.*;
import tripleo.elijah.util.*;

public class QualidentToDotExpressionTest {

	@Test
	public void qualidentToDotExpression2() {
		final Qualident q = new Qualident();
		q.append(tripleo.elijah.util.Helpers.string_to_ident("a"));
		q.append(tripleo.elijah.util.Helpers.string_to_ident("b"));
		q.append(tripleo.elijah.util.Helpers.string_to_ident("c"));
		final IExpression e = Helpers.qualidentToDotExpression2(q);
		System.out.println(e);
		Assert.assertEquals("a.b.c", e.toString());
	}
}