package tripleo.elijah_prolific.comp_signals;

import tripleo.elijah.ci.*;
import tripleo.elijah.comp.*;

class CSS2_CCI_Next implements CSS2_Signal {
	@Override
	public void trigger(Compilation compilation, Object payload) {
		assert payload instanceof CompilerInstructions;

		compilation._cis.onNext((CompilerInstructions) payload);
	}
}
