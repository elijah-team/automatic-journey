package tripleo.elijah.stages.deduce;

import io.reactivex.rxjava3.annotations.*;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.*;
import io.reactivex.rxjava3.subjects.*;
import org.jdeferred2.*;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.*;
import tripleo.elijah.diagnostic.*;
import tripleo.elijah.lang.*;
import tripleo.elijah.nextgen.*;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.work.*;

import java.util.*;

class Dependencies {
	final         WorkList     wl = new WorkList();
	final         WorkManager  wm;
	private final DeduceTypes2 deduceTypes2;

	Dependencies(final DeduceTypes2 aDeduceTypes2, final WorkManager aWm) {
		deduceTypes2 = aDeduceTypes2;
		wm           = aWm;
	}

	public void subscribeTypes(final Subject<GenType> aDependentTypesSubject) {
		aDependentTypesSubject.subscribe(new Observer<GenType>() {
			@Override
			public void onSubscribe(@NonNull final Disposable d) {

			}

			@Override
			public void onNext(final GenType aGenType) {
				action_type(aGenType);
			}

			@Override
			public void onError(final Throwable aThrowable) {

			}

			@Override
			public void onComplete() {

			}
		});
	}

	public void action_type(@NotNull final GenType genType) {
		// TODO work this out further, maybe like a Deepin flavor
		if (genType.resolvedn != null) {
			@NotNull final OS_Module           mod = genType.resolvedn.getContext().module();
			final @NotNull GenerateFunctions   gf  = deduceTypes2.phase.generatePhase.getGenerateFunctions(mod);
			final NamespaceInvocation          ni  = deduceTypes2.phase.registerNamespaceInvocation(genType.resolvedn);
			@NotNull final WlGenerateNamespace gen = new WlGenerateNamespace(gf, ni, deduceTypes2.phase.generatedClasses, deduceTypes2.phase.codeRegistrar);

			assert genType.ci == null || genType.ci == ni;
			genType.ci = ni;

			wl.addJob(gen);

			ni.resolvePromise().then(new DoneCallback<GeneratedNamespace>() {
				@Override
				public void onDone(final @NotNull GeneratedNamespace result) {
					genType.node = result;
					result.dependentTypes().add(genType);
				}
			});
		} else if (genType.resolved != null) {
			if (genType.functionInvocation != null) {
				action_function(genType.functionInvocation);
				return;
			}

			final ClassStatement             c   = genType.resolved.getClassOf();
			final @NotNull OS_Module         mod = c.getContext().module();
			final @NotNull GenerateFunctions gf  = deduceTypes2.phase.generatePhase.getGenerateFunctions(mod);
			@Nullable ClassInvocation        ci;
			if (genType.ci == null) {
				ci = new ClassInvocation(c, null);
				ci = deduceTypes2.phase.registerClassInvocation(ci);

				genType.ci = ci;
			} else {
				assert genType.ci instanceof ClassInvocation;
				ci = (ClassInvocation) genType.ci;
			}

			final Promise<ClassDefinition, Diagnostic, Void> pcd = deduceTypes2.phase.generateClass(gf, ci);

			pcd.then(new DoneCallback<ClassDefinition>() {
				@Override
				public void onDone(final ClassDefinition result) {
					final GeneratedClass genclass = result.getNode();

					genType.node = genclass;
					genclass.dependentTypes().add(genType);
				}
			});
		}
		//
		wm.addJobs(wl);
	}

	public void action_function(@NotNull final FunctionInvocation aDependentFunction) {
		final BaseFunctionDef    function = aDependentFunction.getFunction();
		final WorkJob            gen;
		final @NotNull OS_Module mod;
		if (function == ConstructorDef.defaultVirtualCtor) {
			final ClassInvocation ci = aDependentFunction.getClassInvocation();
			if (ci == null) {
				final NamespaceInvocation ni = aDependentFunction.getNamespaceInvocation();
				assert ni != null;
				mod = ni.getNamespace().getContext().module();

				ni.resolvePromise().then(new DoneCallback<GeneratedNamespace>() {
					@Override
					public void onDone(final GeneratedNamespace result) {
						result.dependentFunctions().add(aDependentFunction);
					}
				});
			} else {
				mod = ci.getKlass().getContext().module();
				ci.resolvePromise().then(new DoneCallback<GeneratedClass>() {
					@Override
					public void onDone(final GeneratedClass result) {
						result.dependentFunctions().add(aDependentFunction);
					}
				});
			}
			final @NotNull GenerateFunctions gf = deduceTypes2.getGenerateFunctions(mod);
			gen = new WlGenerateDefaultCtor(gf, aDependentFunction, deduceTypes2._phase().codeRegistrar);
		} else {
			mod = function.getContext().module();
			final @NotNull GenerateFunctions gf = deduceTypes2.getGenerateFunctions(mod);
			gen = new WlGenerateFunction(gf, aDependentFunction, deduceTypes2._phase().codeRegistrar);
		}
		wl.addJob(gen);
		final List<BaseGeneratedFunction> coll = new ArrayList<>();
		wl.addJob(new WlDeduceFunction(gen, coll, deduceTypes2));
		wm.addJobs(wl);
	}

	public void subscribeFunctions(final Subject<FunctionInvocation> aDependentFunctionSubject) {
		aDependentFunctionSubject.subscribe(new Observer<FunctionInvocation>() {
			@Override
			public void onSubscribe(@NonNull final Disposable d) {

			}

			@Override
			public void onNext(@NonNull final FunctionInvocation aFunctionInvocation) {
				action_function(aFunctionInvocation);
			}

			@Override
			public void onError(@NonNull final Throwable e) {

			}

			@Override
			public void onComplete() {

			}
		});
	}
}
