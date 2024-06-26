package tripleo.elijah.stages.deduce.zero;

import tripleo.elijah.lang.*;
import tripleo.elijah.stages.deduce.*;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.util.*;

class ZeroResolver {

	GenType gt;
	private DeduceTypes2 deduceTypes2;

	public Zero_Type resolve_type(final OS_Type ty) {
		try {
			gt = deduceTypes2.resolve_type(ty, ty.getTypeName().getContext());
			return new Zero_Type(gt);
		} catch (final ResolveError aE) {
			NotImplementedException.raise();
		}
		return null;
	}
}
