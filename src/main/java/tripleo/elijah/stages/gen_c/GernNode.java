package tripleo.elijah.stages.gen_c;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.stages.gen_fn.*;

public interface GernNode {
	@NotNull
	GeneratedNode carrier();

	@Nullable
	GeneratedFunction asFunction();

	@Nullable
	GeneratedContainerNC asContainer();

	@Nullable
	GeneratedClass asClass();

	@Nullable
	GeneratedNamespace asNamespace();

	@Nullable
	GeneratedConstructor asConstructor();
}
