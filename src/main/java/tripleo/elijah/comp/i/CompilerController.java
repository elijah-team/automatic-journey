package tripleo.elijah.comp.i;

import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.internal.CompilationBus;

import java.util.List;

public interface CompilerController {
	void printUsage();

	void processOptions();

	void runner();

	void setInputs(Compilation aCompilation, List<String> aArgumentList);

	CompilationBus getCB();
}
