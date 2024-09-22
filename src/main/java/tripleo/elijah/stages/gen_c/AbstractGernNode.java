package tripleo.elijah.stages.gen_c;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah_fluffy.anno.ElLateInit;

class AbstractGernNode implements GernNode {
	protected /*final*/ @ElLateInit GeneratedNode generatedNode;

	public void setNode(final GeneratedNode aGeneratedNode) {
		generatedNode = aGeneratedNode;
	}

	@Override
	public @NotNull GeneratedNode carrier() {
		return generatedNode;
	}

	@Override
	public @Nullable GeneratedFunction asFunction() {
		if (generatedNode instanceof GeneratedFunction)
			return (GeneratedFunction) generatedNode;
		return null;
	}

	@Override
	public @Nullable GeneratedContainerNC asContainer() {
		if (generatedNode instanceof GeneratedContainerNC)
			return (GeneratedContainerNC) generatedNode;
		return null;
	}
	@Override
	public @Nullable GeneratedClass asClass() {
		if (generatedNode instanceof GeneratedClass)
			return (GeneratedClass) generatedNode;
		return null;
	}
	@Override
	public @Nullable GeneratedNamespace asNamespace() {
		if (generatedNode instanceof GeneratedNamespace)
			return (GeneratedNamespace) generatedNode;
		return null;
	}

	@Override
	public @Nullable GeneratedConstructor asConstructor() {
		if (generatedNode instanceof GeneratedConstructor)
			return (GeneratedConstructor) generatedNode;
		return null;
	}
}
