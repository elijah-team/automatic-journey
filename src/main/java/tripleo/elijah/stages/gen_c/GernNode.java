package tripleo.elijah.stages.gen_c;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.work.WorkList;

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

	default boolean isAttached() {
		return false;
	}

	default String identityString() {
		throw new RuntimeException("identityString");
	}

	default void generateCodeForMethod(BaseGeneratedFunction aGf, GenerateResult aGr, WorkList aWorkList, GenerateC aGenerateC, ElLog aLOG) {
		throw new RuntimeException("generateCodeForMethod");
	}
}
