package tripleo.elijah.comp;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.comp.internal.EDR_CIS;
import tripleo.elijah_fluffy.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class CompilerInstructionsObserver implements Observer<CompilerInstructions> {
    private final List<CompilerInstructions> l = new ArrayList<>();
    private final Compilation compilation;

    public CompilerInstructionsObserver(final Compilation aCompilation, final EDR_CIS cis) {
        compilation = aCompilation;
        compilation.set_cio(this);

        cis.subscribe(this);
    }

    @Override
    public void onSubscribe(@NonNull final Disposable d) {}

    @Override
    public void onNext(@NonNull final CompilerInstructions aCompilerInstructions) {
        l.add(aCompilerInstructions);
    }

    @Override
    public void onError(@NonNull final Throwable e) {
        NotImplementedException.raise();
    }

    @Override
    public void onComplete() {
        throw new RuntimeException();
    }

    public void almostComplete() {
        try {
            compilation.hasInstructions(l);
        } catch (final Exception aE) {
            compilation.getErrSink().exception(aE);
            throw new RuntimeException(aE);
        }
    }
}
