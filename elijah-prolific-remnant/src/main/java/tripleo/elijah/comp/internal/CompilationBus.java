package tripleo.elijah.comp.internal;

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
	public void inst(final @NotNull ILazyCompilerInstructions aLazyCompilerInstructions) {
		System.out.println("** [ci] " + aLazyCompilerInstructions.get().getFilename());
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
