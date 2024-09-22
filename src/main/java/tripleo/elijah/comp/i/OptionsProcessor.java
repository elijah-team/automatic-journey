package tripleo.elijah.comp.i;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.Compilation;

import java.util.List;

// @FunctionalInterface
public interface OptionsProcessor {
    String[] process(@NotNull Compilation c, @NotNull List<String> args, @NotNull ICompilationBus cb) throws Exception;
}
