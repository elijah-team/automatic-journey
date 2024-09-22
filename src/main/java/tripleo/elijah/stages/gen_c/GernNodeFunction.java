package tripleo.elijah.stages.gen_c;

import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedNode;

class GernNodeFunction extends AbstractGernNode {
	public GernNodeFunction(final GeneratedNode aGeneratedNode) {
		setNode(aGeneratedNode);
	}

	public GeneratedFunction asFunction() {
		return (GeneratedFunction) generatedNode;
	}
}
