package tripleo.elijah.stages.gen_c;

import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.work.WorkManager;

public interface Gerns extends Iterable<GernNode> {
	GenerateResult generateCode(GenerateC aGgc, WorkManager aWm);
}
