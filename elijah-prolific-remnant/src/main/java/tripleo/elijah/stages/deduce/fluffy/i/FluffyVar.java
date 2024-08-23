package tripleo.elijah.stages.deduce.fluffy.i;

import tripleo.elijah.diagnostic.*;
import tripleo.elijah.nextgen.composable.*;

public interface FluffyVar {
	String name();

	Locatable nameLocatable();

	IComposable nameComposable();

	FluffyVarTarget target();
}
