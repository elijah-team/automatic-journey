/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import org.jdeferred2.DoneCallback;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.junit.Test;
import tripleo.elijah.comp.AccessBus;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.PipelineLogic;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.deduce.*;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.instructions.InstructionArgument;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.test_help.Boilerplate;
import tripleo.elijah_remnant.rosetta.FakeRosetta3;

import java.util.List;

import static org.easymock.EasyMock.*;

/**
 * Created 3/4/21 3:53 AM
 */
public class TestIdentNormal {

	//	@Test(expected = IllegalStateException.class) // TODO proves nothing
	public void test() {
		final Boilerplate b = new Boilerplate();
		b.get();
		final Compilation comp = b.comp;
		final OS_Module mod = comp.moduleBuilder()
				.addToCompilation()
				.build();
		final FunctionDef fd   = mock(FunctionDef.class);
		final Context     ctx1 = mock(Context.class);
		final Context     ctx2 = mock(Context.class);

		final ElLog.Verbosity verbosity1    = Compilation.gitlabCIVerbosity();
		final AccessBus       ab            = new AccessBus(comp);
		final PipelineLogic   pl            = new PipelineLogic(ab);
		final GeneratePhase   generatePhase = pl.getGeneratePhase();
//		GenerateFunctions generateFunctions = new GenerateFunctions(generatePhase, mod, pl);
		final GenerateFunctions generateFunctions = generatePhase.getGenerateFunctions(mod);
		final GeneratedFunction generatedFunction = new GeneratedFunction(fd);
		final VariableSequence  seq               = new VariableSequence(ctx1);
		final VariableStatement vs                = new VariableStatement(seq);
		final IdentExpression   x                 = IdentExpression.forString("x");
		vs.setName(x);
		final IdentExpression         foo = IdentExpression.forString("foo");
		final ProcedureCallExpression pce = new ProcedureCallExpression();
		pce.setLeft(new DotExpression(x, foo));

		final InstructionArgument s = generateFunctions.simplify_expression(pce, generatedFunction, ctx2);
		@NotNull final List<InstructionArgument> l = BaseGeneratedFunction._getIdentIAPathList(s);
		System.out.println(l);
//      System.out.println(generatedFunction.getIdentIAPathNormal());

		//
		//
		//

		final LookupResultList lrl = new LookupResultList();
		lrl.add("x", 1, vs, ctx2);
		expect(ctx2.lookup("x")).andReturn(lrl);

		replay(ctx2);

		//
		//
		//

		final IdentIA identIA = new IdentIA(1, generatedFunction);

		FakeRosetta3.mkDeduceTypes2(mod, pl.getDp(), new DoneCallback<DeduceTypes2>() {
			@Override
			public void onDone(final DeduceTypes2 d2) {
				final List<InstructionArgument> ss = BaseGeneratedFunction._getIdentIAPathList(identIA);
				d2.resolveIdentIA2_(ctx2, null, ss/* identIA */, generatedFunction, new FoundElement(pl.getDp()) {
					@Override
					public void foundElement(final OS_Element e) {
						System.out.println("[240914 0090] " + e);
					}

					@Override
					public void noFoundElement() {
						final int y = 2;
					}
				});
			}
		});
	}

	@Ignore
	@Test // TODO just a mess
	public void test2() {
		final Boilerplate b = new Boilerplate();
		b.get();
		final Compilation comp = b.comp;
		final OS_Module   mod  = b.defaultMod();

//		FunctionDef fd = mock(FunctionDef.class);
		final Context ctx2 = mock(Context.class);

		final AccessBus     ab            = new AccessBus(comp);
		final PipelineLogic pl            = new PipelineLogic(ab);
		final GeneratePhase generatePhase = pl.getGeneratePhase();
		final DeducePhase   phase         = pl.getDp();

		final GenerateFunctions generateFunctions = generatePhase.getGenerateFunctions(mod);

		//
		//
		//

		final ClassStatement  cs       = new ClassStatement(mod, mod.getContext());
		final IdentExpression capitalX = IdentExpression.forString("X");
		cs.setName(capitalX);
		final FunctionDef fd   = new FunctionDef(cs, cs.getContext());
		final Context     ctx1 = fd.getContext();
		fd.setName(IdentExpression.forString("main"));
		final FunctionDef fd2 = new FunctionDef(cs, cs.getContext());
		fd2.setName(IdentExpression.forString("foo"));

//		GeneratedFunction generatedFunction = new GeneratedFunction(fd);
//		TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, new OS_Type(cs));
//		generatedFunction.addVariableTableEntry("x", VariableTableType.VAR, tte, cs);

		//
		//
		//

		final VariableSequence  seq = new VariableSequence(ctx1);
		final VariableStatement vs  = seq.next();
		final IdentExpression   x   = IdentExpression.forString("x");
		vs.setName(x);
		final ProcedureCallExpression pce2 = new ProcedureCallExpression();
		pce2.setLeft(capitalX);
		vs.initial(pce2);
		final IBinaryExpression e = ExpressionBuilder.build(x, ExpressionKind.ASSIGNMENT, pce2);

		final IdentExpression         foo = IdentExpression.forString("foo");
		final ProcedureCallExpression pce = new ProcedureCallExpression();
		pce.setLeft(new DotExpression(x, foo));

		fd.scope(new Scope3(fd));
		fd.add(seq);
		fd.add(new StatementWrapper(pce2, ctx1, fd));
		fd2.scope(new Scope3(fd2));
		fd2.add(new StatementWrapper(pce, ctx2, fd2));

		ClassInvocation ci = new ClassInvocation(cs, null);
		ci = phase.registerClassInvocation(ci);
		final ProcTableEntry     pte2 = null;
		final FunctionInvocation fi   = new FunctionInvocation(fd, pte2, ci, generatePhase);
//		expect(fd.returnType()).andReturn(null);
		final FormalArgList formalArgList = new FormalArgList();
//		expect(fd.fal()).andReturn(formalArgList);
//		expect(fd.fal()).andReturn(formalArgList);
//		expect(fd2.returnType()).andReturn(null);
		final GeneratedFunction generatedFunction = generateFunctions.generateFunction(fd, cs, fi);

/*
		InstructionArgument es = generateFunctions.simplify_expression(e, generatedFunction, ctx2);

		InstructionArgument s = generateFunctions.simplify_expression(pce, generatedFunction, ctx2);
*/

		//
		//
		//

		final LookupResultList lrl = new LookupResultList();
		lrl.add("foo", 1, fd2, ctx2);

		expect(ctx2.lookup("foo")).andReturn(lrl);

		final LookupResultList lrl2 = new LookupResultList();
		lrl2.add("X", 1, cs, ctx1);

		expect(ctx2.lookup("X")).andReturn(lrl2);

		//
		//
		//

		ClassInvocation invocation2 = new ClassInvocation(cs, null);
		invocation2 = phase.registerClassInvocation(invocation2);
		final ProcTableEntry     pte3               = null;
		final FunctionInvocation fi2                = new FunctionInvocation(fd2, pte3, invocation2, generatePhase);
		final GeneratedFunction  generatedFunction2 = generateFunctions.generateFunction(fd2, fd2.getParent(), fi2);// new
		// GeneratedFunction(fd2);
//		generatedFunction2.addVariableTableEntry("self", VariableTableType.SELF, null, null);
//		final TypeTableEntry type = null;
//		int res = generatedFunction2.addVariableTableEntry("Result", VariableTableType.RESULT, type, null);

		//
		//
		//

		replay(ctx2);

		//
		//
		//

		final IdentIA identIA = new IdentIA(0, generatedFunction);

		FakeRosetta3.mkDeduceTypes2(mod, phase, new DoneCallback<DeduceTypes2>() {
			@Override
			public void onDone(final DeduceTypes2 d2) {
				generatedFunction.getVarTableEntry(0).setConstructable(generatedFunction.getProcTableEntry(0));
				identIA.getEntry().setCallablePTE(generatedFunction.getProcTableEntry(1));

				d2.resolveIdentIA2_(ctx2, identIA, generatedFunction, new FoundElement(phase) {
					@Override
					public void foundElement(final OS_Element e) {
						assert e == fd2;
					}

					@Override
					public void noFoundElement() {
						assert false;
					}
				});

				verify(ctx2);
			}
		});
	}

}

//
//
//
