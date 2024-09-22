package tripleo.elijah.stages.gen_c;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.stages.gen_fn.GeneratedClass;
import tripleo.elijah.stages.gen_fn.GeneratedContainerNC;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.work.WorkManager;

import java.util.ArrayList;
import java.util.Collection;

class GernNodeContainer extends AbstractGernNode {
	public GernNodeContainer(final GeneratedNode aGeneratedNode) {
		setNode(aGeneratedNode);
	}

	@Override
	public @Nullable GeneratedContainerNC asContainer() {
		return (GeneratedContainerNC) generatedNode;
	}

	public void generateCode(final GenerateC aGenerateC, final GenerateResult aGenerateResult) {
		final GeneratedContainerNC container = asContainer();
		Preconditions.checkNotNull(container);
		container.generateCode(aGenerateC, aGenerateResult);
	}

	GenerateResult generateCodeForFunctions(final WorkManager wm,
	                                        final @NotNull GeneratedContainerNC nc,
	                                        final GenerateC ggc,
	                                        final GenerateC aGenerateC) {
		if (ggc != aGenerateC) {
			throw new AssertionError();
		}

		final Collection<GeneratedFunction> values = nc.functionMap.values();
		final @NotNull Collection<GeneratedNode> gn1 = new ArrayList<>();
		for (GeneratedFunction node : values) {
			//noinspection UseBulkOperation
			gn1.add(node);
		}
		//noinspection UnnecessaryLocalVariable
		final GenerateResult gr3 = aGenerateC.gern(gn1).generateCode(ggc, wm);
		return gr3;
	}

	GenerateResult generateCodeForContainers(final WorkManager wm, final @NotNull GeneratedContainerNC nc, final GenerateC ggc, final GenerateC aGenerateC) {
		final Collection<GeneratedClass> values = nc.classMap.values();
		final @NotNull Collection<GeneratedNode> gn2 = new ArrayList<>();
		for (GeneratedClass node : values) {
			//noinspection UseBulkOperation
			gn2.add(node);
		}
		//noinspection UnnecessaryLocalVariable
		final GenerateResult gr4 = aGenerateC.gern(gn2).generateCode(ggc, wm);
		return gr4;
	}
}
