package tripleo.elijah.comp.internal;

import io.activej.eventloop.Eventloop;
import io.activej.inject.annotation.Inject;
import io.activej.inject.module.Module;
import io.activej.launcher.Launcher;
import io.activej.service.Service;
import io.activej.service.ServiceGraphModule;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;
import java.util.List;

import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.i.CompilerController;
import tripleo.elijah_prolific.v.V;

class ProlificCompilationLauncher extends Launcher {
    private final @NotNull Compilation  compilation;
    private final @NotNull List<String> args;
    private final @NotNull CompilerController controller;

    public ProlificCompilationLauncher(final @NotNull Compilation aCompilation,
                                       final @NotNull List<String> aStringList,
                                       final @NotNull CompilerController aController) {
        compilation = aCompilation;
        args        = aStringList;
        controller  = aController;
    }

    @Inject
    CustomService customService;

    @Override
    protected Module getModule() {
        return ServiceGraphModule.create();
    }

    @Inject
    private class CustomService implements Service {
        @Override
        public CompletableFuture<?> start() {
            logProgress("|SERVICE STARTING|");
            {
                controller.setInputs(compilation, args);
                controller.processOptions();
                controller.runner();

                V.exit(compilation);
            }
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public CompletableFuture<?> stop() {
            logProgress("|SERVICE STOPPING|");
            return CompletableFuture.completedFuture(null);
        }
    }

    @Override
    protected void run() {
        logProgress("|RUNNING|");
    }

    private static void logProgress(final String x) {
        System.out.println(x);
    }

    public void launch0() {
        try {
            Eventloop.builder().withCurrentThread().build();
            var launcher = this;
            launcher.launch(new String[0]);
        } catch (Exception aE) {
            compilation.getErrSink().exception(aE);
        }
    }
}
