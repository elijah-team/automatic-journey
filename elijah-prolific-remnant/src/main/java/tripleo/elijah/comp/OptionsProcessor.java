package tripleo.elijah.comp;

import org.jetbrains.annotations.*;
import tripleo.elijah.comp.i.*;

import java.util.*;

//@FunctionalInterface
public interface OptionsProcessor {
	String[] process(@NotNull Compilation c, @NotNull List<String> args, @NotNull ICompilationBus cb) throws Exception;
}
