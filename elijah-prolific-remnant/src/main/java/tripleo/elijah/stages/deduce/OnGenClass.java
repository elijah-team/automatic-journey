package tripleo.elijah.stages.deduce;

import tripleo.elijah.stages.gen_fn.*;

@FunctionalInterface
public interface OnGenClass {
	void accept(final GeneratedClass aGeneratedClass);
}
