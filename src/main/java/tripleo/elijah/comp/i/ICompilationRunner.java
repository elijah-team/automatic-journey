package tripleo.elijah.comp.i;

import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.comp.internal.EDR_CompilationRunner;
import tripleo.elijah.comp.Operation;
import tripleo.elijah.comp.specs.EzCache;
import tripleo.elijah.comp.specs.EzSpec;

import java.util.List;

public interface ICompilationRunner {
	void start(CompilerInstructions ci, boolean do_out);

	Operation<CompilerInstructions> realParseEzFile(EzSpec spec, EzCache cache);

	EzCache ezCache();

	public interface CR_Process {
		List<ICompilationBus.CB_Action> steps();
	}

	public interface CR_Action {
		void attach(EDR_CompilationRunner cr);

		void execute(CR_State st);

		String name();
	}
}
