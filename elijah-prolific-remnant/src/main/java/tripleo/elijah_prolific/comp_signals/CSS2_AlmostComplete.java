package tripleo.elijah_prolific.comp_signals;

import tripleo.elijah.comp.*;

public class CSS2_AlmostComplete implements CSS2_Signal {
	@Override
	public void trigger(Compilation compilation, Object payload) {
		compilation._cis.almostComplete(compilation._cio);
	}
}
