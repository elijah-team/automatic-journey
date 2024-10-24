package tripleo.elijah.comp.i;

import tripleo.elijah.comp.Compilation;

import java.util.List;

public interface CompilerController {
    void printUsage();

    void processOptions();

    void runner();

    void setInputs(Compilation aCompilation, List<String> aArgumentList);

    ICompilationBus getCB();
}
