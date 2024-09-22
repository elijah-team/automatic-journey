package tripleo.elijah.comp.internal;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.Operation;
import tripleo.elijah.comp.i.ILazyCompilerInstructions;

import java.io.File;
import java.util.Objects;

public class ILazyCompilerInstructions_ {

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ILazyCompilerInstructions of(final CompilerInstructions aCompilerInstructions) {
        return () -> aCompilerInstructions;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull ILazyCompilerInstructions of(final File aFile, final Compilation c) {
        return new ILazyCompilerInstructions() {
            @Override
            public CompilerInstructions get() {
                try {
                    final Operation<CompilerInstructions> parsed =
                            CX_ParseEzFile.parseAndCache(aFile, c, c.get__cr().ezCache());
                    return Objects.requireNonNull(parsed).success();
                } catch (final Exception aE) {
                    throw new RuntimeException(aE); // TODO ugh
                }
            }
        };
    }
}
