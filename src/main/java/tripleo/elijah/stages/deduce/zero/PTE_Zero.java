package tripleo.elijah.stages.deduce.zero;

import org.jdeferred2.*;
import org.jdeferred2.impl.*;
import org.jetbrains.annotations.*;
import tripleo.elijah.comp.*;
import tripleo.elijah.diagnostic.*;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.deduce.*;
import tripleo.elijah.stages.gen_fn.*;

public class PTE_Zero {
	private final ProcTableEntry                                   procTableEntry;
	private final DeferredObject<IElementHolder, Diagnostic, Void> _foundCounstructorDef2Promise = new DeferredObject<>();

	public PTE_Zero(final ProcTableEntry aProcTableEntry) {
		procTableEntry = aProcTableEntry;
	}

	public void foundCounstructorDef(final @NotNull GeneratedConstructor constructorDef,
	                                 final @NotNull IdentTableEntry ite,
	                                 final @NotNull DeduceTypes2 deduceTypes2,
	                                 final @NotNull ErrSink errSink) {
		@NotNull final BaseFunctionDef ele = constructorDef.getFD();

		try {
			final LookupResultList     lrl  = DeduceLookupUtils.lookupExpression(ite.getIdent(), ele.getContext(), deduceTypes2);
			@Nullable final OS_Element best = lrl.chooseBest(null);
			ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(best));
		} catch (final ResolveError aResolveError) {
//            aResolveError.printStackTrace();
			errSink.reportDiagnostic(aResolveError);
		}
	}

	public Promise<IElementHolder, Diagnostic, Void> foundCounstructorPromise() {
		return _foundCounstructorDef2Promise.promise();
	}

	public void calculateConstructor(@NotNull final GeneratedConstructor constructorDef, @NotNull final IdentTableEntry ite, @NotNull final DeduceTypes2 deduceTypes2) {
		if (_foundCounstructorDef2Promise.isResolved()) return;

		@NotNull final BaseFunctionDef ele = constructorDef.getFD();

		try {
			final LookupResultList     lrl  = DeduceLookupUtils.lookupExpression(ite.getIdent(), ele.getContext(), deduceTypes2);
			@Nullable final OS_Element best = lrl.chooseBest(null);
//            ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(best));
			final GenericElementHolder elementHolder = new GenericElementHolder(best);
			_foundCounstructorDef2Promise.resolve(elementHolder);
		} catch (final ResolveError aResolveError) {
			_foundCounstructorDef2Promise.reject(aResolveError);
		}
	}
}
