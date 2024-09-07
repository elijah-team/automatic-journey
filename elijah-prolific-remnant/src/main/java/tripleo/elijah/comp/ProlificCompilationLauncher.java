package tripleo.elijah.comp;

import io.activej.inject.annotation.Inject;
import io.activej.inject.annotation.Provides;
import io.activej.launcher.Launcher;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.i.CompilerController;

import java.util.List;

class ProlificCompilationLauncher extends Launcher {
    private final Compilation compilation;
    private final @NotNull List<String> args1;
    private final CompilerController controller;

    @Inject
    String message;

    @Provides
    String message() {
        return "Hello, world!";
    }

    public ProlificCompilationLauncher(final Compilation aCompilation, final @NotNull List<String> aStringList, final CompilerController aController) {
        compilation = aCompilation;
        args1 = aStringList;
        controller = aController;
    }

    @Override
    protected void run() {
        compilation._actual_feedCmdLine(args1, controller);
    }

    public void launch0() {
        try {
            var launcher = this;
            launcher.launch(new String[0]);
        } catch (Exception aE) {
            throw new RuntimeException(aE);
        }
    }
}
