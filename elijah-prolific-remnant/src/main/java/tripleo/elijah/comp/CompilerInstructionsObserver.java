package tripleo.elijah.comp;

import io.reactivex.rxjava3.annotations.*;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.*;
import tripleo.elijah.ci.*;
import tripleo.elijah_fluffy.util.*;

import java.util.*;

public class CompilerInstructionsObserver implements Observer<CompilerInstructions> {
	private final List<CompilerInstructions> l = new ArrayList<>();
	private final Compilation                compilation;

	public CompilerInstructionsObserver(final Compilation aCompilation,
	                                    final Compilation.CIS cis) {
		compilation = aCompilation;
		compilation._cio    = this;

		cis.subscribe(this);
	}

	@Override
	public void onSubscribe(@NonNull final Disposable d) {

	}

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
