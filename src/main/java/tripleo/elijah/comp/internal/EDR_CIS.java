package tripleo.elijah.comp.internal;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import io.reactivex.rxjava3.subjects.Subject;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.comp.CompilerInstructionsObserver;

public class EDR_CIS implements Observer<CompilerInstructions> {
	private final Subject<CompilerInstructions> compilerInstructionsSubject = ReplaySubject.create();

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
		throw new IllegalStateException(); // FIXME UUE("how'd you get here?")
	}

	public void almostComplete(@NotNull CompilerInstructionsObserver aCio) {
		aCio.almostComplete();
	}

	public void subscribe(final Observer<CompilerInstructions> aCio) {
		compilerInstructionsSubject.subscribe(aCio);
	}
}
