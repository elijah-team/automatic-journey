package tripleo.elijah_prolific.comp_signals;

import tripleo.elijah.ci.*;
import tripleo.elijah.comp.*;
import tripleo.elijah.comp.i.*;
import tripleo.elijah.stages.deduce.post_bytecode.*;

public class CSS2_CCI_Accept implements CSS2_Signal {
	@Override
	public void trigger(Compilation compilation, Object payload) {
//		if (payload instanceof Maybe<ILazyCompilerInstructions>) {
			Maybe<ILazyCompilerInstructions> mcci = (Maybe<ILazyCompilerInstructions>) payload;
			if (mcci.isException()) return;

			final ILazyCompilerInstructions cci = mcci.o;
			final CompilerInstructions ci = cci.get();

//	    		_ps.note(131, ProgressSinkComponent.CCI, -1, new Object[]{ci.getName()});

			CSS2_CCI_Next css2_cciNext = new CSS2_CCI_Next();
			compilation.signal(css2_cciNext, ci);
//		} else {
//			assert false;
//		}
	}
}
