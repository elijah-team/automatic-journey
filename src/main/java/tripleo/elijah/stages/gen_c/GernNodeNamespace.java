package tripleo.elijah.stages.gen_c;

import org.jetbrains.annotations.Nullable;
import tripleo.elijah.stages.gen_fn.GeneratedNamespace;
import tripleo.elijah.stages.gen_fn.GeneratedNode;

class GernNodeNamespace extends AbstractGernNode {
public GernNodeNamespace(final GeneratedNode aGeneratedNode) {
	setNode(aGeneratedNode);
}

@Override
public @Nullable GeneratedNamespace asNamespace() {
	return (GeneratedNamespace) generatedNode;
}
}
