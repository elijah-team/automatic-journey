package tripleo.elijah.stages.deduce;

import org.junit.Ignore;
import org.junit.Test;
import tripleo.elijah.comp.AccessBus;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.PipelineLogic;
import tripleo.elijah.contexts.FunctionContext;
import tripleo.elijah.factory.comp.CompilationFactory;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang.types.OS_BuiltinType;
import tripleo.elijah.lang.types.OS_UserClassType;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.instructions.Instruction;
import tripleo.elijah.stages.instructions.VariableTableType;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah_fluffy.util.Helpers;
import tripleo.elijah_remnant.rosetta.FakeRosetta3;

import java.util.ArrayList;

import static org.easymock.EasyMock.*;

public class DoAssignCall_ArgsIdent1_Test {
	/*
	 * model and test
	 *
	 * var f1 = factorial(b1)
	 */

	@Ignore
	@Test
	public void f1_eq_factorial_b1() {
		final Compilation   c             = CompilationFactory.mkCompilation();
		final OS_Module       mod           = mock(OS_Module.class);
		final PipelineLogic   pipelineLogic = new PipelineLogic(new AccessBus(c));


		// HACK
		final ElLog.Verbosity verbosity = ElLog.Verbosity.SILENT;


		final GeneratePhase generatePhase = new GeneratePhase(verbosity, pipelineLogic, c);
		final DeducePhase   phase         = new DeducePhase(generatePhase, pipelineLogic, verbosity, c);

		expect(mod.getCompilation()).andReturn(c);
		expect(mod.getFileName()).andReturn("foo.elijah");
//		expect(mod.getCompilation()).andReturn(c);
//		expect(mod.getCompilation()).andReturn(c);
//		expect(mod.getCompilation()).andReturn(c);

		replay(mod);

		FakeRosetta3.mkDeduceTypes2(mod, phase, (final DeduceTypes2 d)->{
			final FunctionDef fd = mock(FunctionDef.class);
			// final GeneratedFunction generatedFunction = mock(GeneratedFunction.class);
			final GeneratedFunction generatedFunction = new GeneratedFunction(fd);
			final TypeTableEntry self_type = generatedFunction.newTypeTableEntry(
					TypeTableEntry.Type.SPECIFIED,
					new OS_UserClassType(mock(ClassStatement.class)));
			final int index_self = generatedFunction.addVariableTableEntry("self", VariableTableType.SELF, self_type,
			                                                               null);
			final TypeTableEntry    result_type       = null;
			final int index_result = generatedFunction.addVariableTableEntry("Result", VariableTableType.RESULT,
			                                                                 result_type, null);
			final OS_Type           sts_int           = new OS_BuiltinType(BuiltInTypes.SystemInteger);
			final TypeTableEntry    b1_type           = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, sts_int);
			final OS_Type           b1_attached       = sts_int;
			b1_type.setAttached(sts_int);
			final int             index_b1 = generatedFunction.addVariableTableEntry("b1", VariableTableType.VAR, b1_type, null);
			final FunctionContext ctx      = mock(FunctionContext.class);

			final LookupResultList  lrl_b1 = new LookupResultList();
			final VariableSequence  vs     = new VariableSequence();
			final VariableStatement b1_var = new VariableStatement(vs);
			b1_var.setName(Helpers.string_to_ident("b1"));
			final Context b1_ctx = mock(Context.class);
			lrl_b1.add("b1", 1, b1_var, b1_ctx);

			expect(ctx.lookup("b1")).andReturn(lrl_b1);

			replay(fd, /*generatedFunction,*/ ctx, b1_ctx);

			final TypeTableEntry vte_tte = null;
			final OS_Element     el      = null;

			final VariableTableEntry vte              = generatedFunction.getVarTableEntry(index_self);
			final int                instructionIndex = -1;
			final ProcTableEntry     pte              = new ProcTableEntry(-2, null, null, new ArrayList()/*List_of()*/);
			final int                i                = 0;
			final TypeTableEntry     tte              = new TypeTableEntry(-3, TypeTableEntry.Type.SPECIFIED, null, null, null);
			final IdentExpression    identExpression  = Helpers.string_to_ident("b1"); // TODO ctx

			d.do_assign_call_args_ident(generatedFunction, ctx, vte, instructionIndex, pte, i, tte, identExpression);

			final Instruction instruction = null;
			final Context     fd_ctx      = fd.getContext();
			final Context     context     = generatedFunction.getContextFromPC(instruction.getIndex());

			final _DT_Deducer1 d1 = new _DT_Deducer1(generatedFunction, fd, fd_ctx);
			final _DT_Deducer2 d2 = new _DT_Deducer2(d1, instruction, context);

			d.onExitFunction(d2, generatedFunction, ctx, ctx, new DCC(d, d._phase(), d._errSink()));

			verify(mod, fd, /*generatedFunction,*/ ctx);
		});

		FakeRosetta3.ensure_all();
	}

	public class _DT_Deducer2 implements DeduceTypes2._DT_Deducer {
		public _DT_Deducer2(final _DT_Deducer1 aDeducer1, final Instruction aInstruction, final Context aContext) {
		}
	}

	public class _DT_Deducer1 implements DeduceTypes2._DT_Deducer {
		public _DT_Deducer1(final GeneratedFunction aGeneratedFunction, final FunctionDef aFd, final Context aFdCtx) {
		}
	}

}
