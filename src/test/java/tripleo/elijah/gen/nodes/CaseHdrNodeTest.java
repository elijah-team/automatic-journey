package tripleo.elijah.gen.nodes;

import org.junit.*;

public class CaseHdrNodeTest {

	@Test
	public void simpleGenText() {
		final VariableReferenceNode3 vr     = new VariableReferenceNode3("the", new ScopeNode(), null);
		final CaseHdrNode            chn    = new CaseHdrNode(vr);
		final String                 actual = chn.simpleGenText();
		Assert.assertEquals("vvthe", actual);
	}

	@Test
	public void setExpr() {
	}
}