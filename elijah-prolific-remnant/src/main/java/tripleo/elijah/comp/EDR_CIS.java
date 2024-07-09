package tripleo.elijah.comp;

import io.reactivex.rxjava3.annotations.*;
import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.disposables.*;
import io.reactivex.rxjava3.subjects.*;
import tripleo.elijah.ci.*;

public class EDR_CIS implements Observer<CompilerInstructions> {
	private final Subject<CompilerInstructions> compilerInstructionsSubject = ReplaySubject.create();
	CompilerInstructionsObserver _cio;

	@Override
	public void onSubscribe(@NonNull final Disposable d) {
		compilerInstructionsSubject.onSubscribe(d);
	}

	@Override
	public void onNext(@NonNull final CompilerInstructions aCompilerInstructions) {
		compilerInstructionsSubject.onNext(aCompilerInstructions);
	}

	@Override
	public void onError(@NonNull final Throwable e) {
		compilerInstructionsSubject.onError(e);
	}

	@Override
	public void onComplete() {
		throw new IllegalStateException();
		//compilerInstructionsSubject.onComplete();
	}

	public void almostComplete(CompilerInstructionsObserver aCio) {
		aCio.almostComplete();
	}

	public void subscribe(final Observer<CompilerInstructions> aCio) {
		compilerInstructionsSubject.subscribe(aCio);
	}
}
