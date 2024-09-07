package tripleo.elijah.comp.internal;

import net.bytebuddy.utility.nullability.NeverNull;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.CompilationChange;
import tripleo.elijah.comp.ILazyCompilerInstructions;
import tripleo.elijah.comp.i.ICompilationBus;
import tripleo.elijah_prolific.comp_signals.CSS2_Advisable;

public class CompilationBus implements ICompilationBus {
	private final Compilation c;

	public CompilationBus(final Compilation aC) {
		c = aC;
	}

	@Override
	public void option(final @NotNull CompilationChange aChange) {
		aChange.apply(c);
	}

	@Override
	public void inst(final ILazyCompilerInstructions aLazyCompilerInstructions, final Instergram reason, final Runnable cheat) {
		if (aLazyCompilerInstructions != null) {
			System.out.println("** [ci] " + aLazyCompilerInstructions.get().getFilename());
			cheat.run();
		} else {
			switch (reason) {
				case EZ -> {
					assert false; // should remove prob
				}
				case ZERO -> {
					// assert false; // for another reason
					int y=2;
				}
				case TWO_MANY -> {
					assert false; // for another nother reason
				}
				case ONE -> {
					assert false; // this should never be hit
//					throw new NeverReached();
				}
			}
			cheat.run();
		}
	}

	public void add(final CB_Action action) {
		action.execute();
	}

	@Override
	public void add(final CB_Process aProcess) {
//		throw new NotImplementedException();

		for (CB_Action action : aProcess.steps()) {
			action.execute();
		}
	}

	@Override
	public void add(final CB_Process aCBProcess, final Object aPayload) {
		if (aCBProcess instanceof CSS2_Advisable advisable) {
			advisable.adviseObject(aPayload);
		} else {
			throw new AssertionError();
		}
		add(aCBProcess);
	}
}
