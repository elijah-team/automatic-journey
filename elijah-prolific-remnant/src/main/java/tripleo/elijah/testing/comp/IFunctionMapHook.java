package tripleo.elijah.testing.comp;

import tripleo.elijah.lang.*;
import tripleo.elijah.stages.gen_fn.*;

import java.util.*;

public interface IFunctionMapHook {
	boolean matches(FunctionDef aFunctionDef);

	void apply(Collection<GeneratedFunction> aGeneratedFunctions);
}
