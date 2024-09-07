package tripleo.elijah.comp;

import io.activej.inject.annotation.Inject;
import io.activej.inject.annotation.Provides;
import io.activej.launcher.Launcher;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.i.CompilerController;
import tripleo.elijah_prolific.v.V;

import java.util.List;

class ProlificCompilationLauncher extends Launcher {
    private final @NotNull Compilation        compilation;
    private final @NotNull List<String>       args;
    private final @NotNull CompilerController controller;

    @Inject
    String message;

    public ProlificCompilationLauncher(final @NotNull Compilation aCompilation,
                                       final @NotNull List<String> aStringList,
                                       final @NotNull CompilerController aController) {
        compilation = aCompilation;
        args        = aStringList;
        controller  = aController;
    }

    @Provides
    String message() {
        return "Hello, world!";
    }

    @Override
    protected void run() {
        controller.setInputs(compilation, args);
        controller.processOptions();
        controller.runner();

        V.exit(compilation);
    }

    public void launch0() {
        try {
            var launcher = this;
            launcher.launch(new String[0]);
        } catch (Exception aE) {
            compilation.getErrSink().exception(aE);
        }
    }
}
