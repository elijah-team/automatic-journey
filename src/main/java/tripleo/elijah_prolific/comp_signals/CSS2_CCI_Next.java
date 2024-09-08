package tripleo.elijah_prolific.comp_signals;

import tripleo.elijah.ci.*;
import tripleo.elijah.comp.*;

public class CSS2_CCI_Next implements CSS2_Signal {
	@Override
	public void trigger(Compilation compilation, Object payload) {
		assert payload instanceof CompilerInstructions;

		compilation.get_cis().onNext((CompilerInstructions) payload);
	}
}
