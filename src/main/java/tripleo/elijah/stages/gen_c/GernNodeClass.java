package tripleo.elijah.stages.gen_c;

import tripleo.elijah.stages.gen_fn.GeneratedClass;
import tripleo.elijah.stages.gen_fn.GeneratedNode;

class GernNodeClass extends AbstractGernNode {
	public GernNodeClass(final GeneratedNode aGeneratedNode) {
		setNode(aGeneratedNode);
	}

	@Override
	public GeneratedClass asClass() {
		return (GeneratedClass) generatedNode;
	}
}
