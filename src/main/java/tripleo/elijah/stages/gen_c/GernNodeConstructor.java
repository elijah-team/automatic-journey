package tripleo.elijah.stages.gen_c;

import tripleo.elijah.stages.gen_fn.GeneratedConstructor;
import tripleo.elijah.stages.gen_fn.GeneratedNode;

class GernNodeConstructor extends AbstractGernNode {
	public GernNodeConstructor(final GeneratedNode aGeneratedNode) {
		setNode(aGeneratedNode);
	}

	public GeneratedConstructor asConstructor() {
		return (GeneratedConstructor) generatedNode;
	}
}
