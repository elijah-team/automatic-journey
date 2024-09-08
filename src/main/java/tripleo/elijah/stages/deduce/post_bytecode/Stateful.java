package tripleo.elijah.stages.deduce.post_bytecode;

import org.jetbrains.annotations.*;

public interface Stateful {
	void mvState(State aO, @NotNull State aState);

	void setState(final State aState);
}
