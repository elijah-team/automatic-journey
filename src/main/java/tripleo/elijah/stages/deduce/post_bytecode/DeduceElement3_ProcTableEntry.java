package tripleo.elijah.stages.deduce.post_bytecode;

import org.jetbrains.annotations.*;
import tripleo.elijah.comp.*;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang.types.*;
import tripleo.elijah.stages.deduce.*;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.instructions.*;
import tripleo.elijah.util.*;
import tripleo.elijah_prolific.deduce.DT_Element3;

public class DeduceElement3_ProcTableEntry implements IDeduceElement3 {
	private final ProcTableEntry        principal;
	private final DeduceTypes2          deduceTypes2;
	private final BaseGeneratedFunction generatedFunction;
	private       Instruction           instruction;

	public DeduceElement3_ProcTableEntry(final ProcTableEntry aProcTableEntry, final DeduceTypes2 aDeduceTypes2, final BaseGeneratedFunction aGeneratedFunction) {
		principal         = aProcTableEntry;
		deduceTypes2      = aDeduceTypes2;
		generatedFunction = aGeneratedFunction;
	}

	@Override
	public void resolve(final IdentIA aIdentIA, final Context aContext, final FoundElement aFoundElement) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void resolve(final Context aContext, final DeduceTypes2 dt2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public OS_Element getPrincipal() {
		//return principal.getDeduceElement3(deduceTypes2, generatedFunction).getPrincipal(); // README infinite loop

		return principal.getResolvedElement();//getDeduceElement3(deduceTypes2, generatedFunction).getPrincipal();
	}

	@Override
	public DED elementDiscriminator() {
		return new DED.DED_PTE(principal);
	}

	@Override
	public DeduceTypes2 deduceTypes2() {
		return deduceTypes2;
	}

	@Override
	public BaseGeneratedFunction generatedFunction() {
		return generatedFunction;
	}

	@Override
	public GenType genType() {
		throw new UnsupportedOperationException("no type for PTE");
	} // TODO check correctness

	@Override
	public DeduceElement3_Kind kind() {
		return DeduceElement3_Kind.GEN_FN__PTE;
	}

	public ProcTableEntry getTablePrincipal() {
		return principal;
	}

	public BaseGeneratedFunction getGeneratedFunction() {
		return generatedFunction;
	}

	public Instruction getInstruction() {
		return instruction;
	}

	public void setInstruction(final Instruction aInstruction) {
		instruction = aInstruction;
	}

	public void doFunctionInvocation() {
		final FunctionInvocation fi = principal.getFunctionInvocation();

		if (fi == null) {
			if (principal.expression instanceof final ProcedureCallExpression exp) {
				final IExpression left = exp.getLeft();

				if (left instanceof final DotExpression dotleft) {

					if (dotleft.getLeft() instanceof final IdentExpression rl && dotleft.getRight() instanceof final IdentExpression rr) {

						if (rl.getText().equals("a1")) {
							final GeneratedClass[] gc = new GeneratedClass[1];

							final InstructionArgument vrl = generatedFunction.vte_lookup(rl.getText());

							assert vrl != null;

							final VariableTableEntry vte = ((IntegerIA) vrl).getEntry();

							vte.typePromise().then(left_type -> {
								final ClassStatement cs = left_type.resolved.getClassOf(); // TODO we want a DeduceClass here. GeneratedClass may suffice

								final ClassInvocation ci = deduceTypes2._phase().registerClassInvocation(cs);
								ci.resolvePromise().then(gc2 -> {
									gc[0] = gc2;
								});

								final LookupResultList     lrl  = cs.getContext().lookup(rr.getText());
								@Nullable final OS_Element best = lrl.chooseBest(null);

								if (best != null) {
									final FunctionDef fun = (FunctionDef) best;

									final FunctionInvocation fi2 = new FunctionInvocation(fun, null, ci, deduceTypes2._phase().generatePhase); // TODO pte??

									principal.setFunctionInvocation(fi2); // TODO pte above

									final WlGenerateFunction j = fi2.generateFunction(deduceTypes2, best);
									j.run(null);

									final @NotNull IdentTableEntry ite      = ((IdentIA) principal.expression_num).getEntry();
									final OS_Type                  attached = ite.type.getAttached();

									fi2.generatePromise().then(gf -> {
										final int y4 = 4;
									});

									if (attached instanceof final OS_FuncType funcType) {

										final GeneratedClass x = gc[0];

										fi2.generatePromise().then(gf -> {
											final int y4 = 4;
										});

										final int y = 2;
									}
									final int yy = 2;
								}
							});
							final int y = 2;
						} else {
							System.err.println("=== 399-147 ================================");
						}
					}
				}
			}
		}
	}

	private void action_002_1(final @NotNull ProcTableEntry pte,
	                          final @NotNull IdentTableEntry ite,
	                          final boolean setClassInvocation,
	                          final DeducePhase phase,
	                          final DeduceTypes2.DeduceClient3 dc) {
		final OS_Element resolvedElement = ite.getResolvedElement();

		assert resolvedElement != null;

		ClassInvocation ci = null;

		if (pte.getFunctionInvocation() == null) {
			@NotNull final FunctionInvocation fi;

			if (resolvedElement instanceof ClassStatement) {
				// assuming no constructor name or generic parameters based on function syntax
				ci = new ClassInvocation((ClassStatement) resolvedElement, null);
				ci = phase.registerClassInvocation(ci);
				fi = new FunctionInvocation(null, pte, ci, phase.generatePhase);
			} else if (resolvedElement instanceof final FunctionDef functionDef) {
				final IInvocation invocation = dc.getInvocation((GeneratedFunction) generatedFunction);
				fi = new FunctionInvocation(functionDef, pte, invocation, phase.generatePhase);
				if (functionDef.getParent() instanceof ClassStatement) {
					final ClassStatement classStatement = (ClassStatement) fi.getFunction().getParent();
					ci = new ClassInvocation(classStatement, null); // TODO generics
					ci = phase.registerClassInvocation(ci);
				}
			} else {
				throw new IllegalStateException();
			}

			if (setClassInvocation) {
				if (ci != null) {
					pte.setClassInvocation(ci);
				} else
					SimplePrintLoggerToRemoveSoon.println_err2("542 Null ClassInvocation");
			}

			pte.setFunctionInvocation(fi);
		}

//        el   = resolvedElement;
//        ectx = el.getContext();
	}


	public void _action_002_no_resolved_element(final Resolve_Ident_IA._action_002_no_resolved_element action) {
		final @NotNull ProcTableEntry  pte       = action.pte();
		final @NotNull IdentTableEntry ite       = action.ite();
		final InstructionArgument      _backlink = ite.getBacklink();

		if (_backlink instanceof ProcIA) {
			final @NotNull ProcIA         backlink_ = (ProcIA) _backlink;
			final @NotNull ProcTableEntry backlink  = generatedFunction.getProcTableEntry(backlink_.getIndex());
			final Resolve_Ident_IA        ria       = action.ria();
			final IdentTableEntry         ite1      = action.ite();
			final ErrSink                 errSink1   = ria.getDc()._dt2()._errSink();

			backlink.onResolvedElement(
					(DT_Element3 dtel) -> {
						final ErrSink                 errSink   = dtel.getErrSink();
						assert errSink1 == errSink;
						final DeducePhase             phase     = ria.getDc().getPhase();

						//dtel.setDeduceTypes2(ria.getDc()._dt2()); //??

						final OS_Element resolvedElement = dtel.getResolvedElement();

						if (resolvedElement == null) {
							dtel.op_fail(DT_Element3.DTEL.d999_163);
							return; //throw new AssertionError(); // TODO feb 20
						}

						try {
							// TODO 24/01/25 do this next
							final IdentExpression      ident = ite1.getIdent();
							if (resolvedElement instanceof OS_Element2 named) {
								assert ident.sameName(named);
							}

							final LookupResultList     lrl2 = ria.getDc().lookupExpression(ident, resolvedElement.getContext());
							@Nullable final OS_Element best  = lrl2.chooseBest(null);
							assert best != null;
							ite1.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(best));
						} catch (final ResolveError aResolveError) {
							errSink.reportDiagnostic(aResolveError);
							assert false;
						}

						action_002_1(principal, ite1, false, phase, ria.getDc());
					}
			);
		} else assert false;
	}
}
