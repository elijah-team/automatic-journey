package tripleo.elijah_prolific.comp_signals;

import org.apache.commons.lang3.tuple.*;
import tripleo.elijah.comp.*;
import tripleo.elijah.comp.ICompilationBus;
import tripleo.elijah.comp.i.*;

import java.util.*;

import static tripleo.elijah_fluffy.util.Helpers.*;

public class CSS2_doFindCIs implements CSS2_Signal {
	@Override
	public void trigger(Compilation compilation, Object payload) {
		if (payload instanceof Pair) {
			var payloadpair = (Pair<String[], ICompilationBus>) payload;
			var args2       = payloadpair.getLeft();
			var cb          = payloadpair.getRight();

			// TODO map + "extract"

			//var crap = compilation.getStartup().getCompilationRunner();
			//crap.then(cr1 -> {
				final CompilationRunner.CR_State st1 = new CompilationRunner.CR_State(compilation.getStartup());

				cb.add(new __CSS2_doFindCIs__CB_Process(), Triple.of(args2, cb, st1));
			//});
		}
	}

	private static class CB_FindCIs implements ICompilationBus.CB_Action {
		private final CompilationRunner.CR_Action a;
		private final CompilationRunner.CR_State  st1;
		private final CompilationRunner           cr;

		public CB_FindCIs(String[] args2, CompilationRunner.CR_State st1) {
			this.st1 = st1;
			cr       = st1.ca().getCompilation().__cr;
			a        = cr.new CR_FindCIs(args2);
		}

		@Override
		public String name() {
			return a.name();
		}

		@Override
		public void execute() {
			st1.cur = this;
			a.attach(cr);
			a.execute(st1);
			st1.cur = null;
		}

		@Override
		public ICompilationBus.OutputString[] outputStrings() {
			return new ICompilationBus.OutputString[0];
		}
	}

	private static class CB_AlmostComplete implements ICompilationBus.CB_Action {
		private       CompilationRunner.CR_Action a;
		private final CompilationRunner.CR_State  st;
		private       CompilationRunner           cr;

		public CB_AlmostComplete(CompilationRunner.CR_State aCRState) {
			st = aCRState;
			var crp = aCRState.ca().getStartup().getCompilationRunner();
			crp.then(cr1-> {
				//cr = aCRState.ca().getCompilation().__cr;
				cr = cr1;
				a  = cr.new CR_AlmostComplete();
			});
		}

		@Override
		public String name() {
			return a.name();
		}

		@Override
		public void execute() {
			a.attach(cr);
			a.execute(st);
		}

		@Override
		public ICompilationBus.OutputString[] outputStrings() {
			return new ICompilationBus.OutputString[0];
		}
	}

	private static class __CSS2_doFindCIs__CB_Process implements ICompilationBus.CB_Process, CSS2_Advisable {
		private ICompilationBus.CB_Action a;
		private ICompilationBus.CB_Action b;

		public void adviseObject(final String[] aArgs2, final CompilationRunner.CR_State aSt1) {
			a = new CB_FindCIs(aArgs2, aSt1);
			b = new CB_AlmostComplete(aSt1);
		}

		@Override
		public List<ICompilationBus.CB_Action> steps() {
			assert a != null;
			assert b != null;

			return List_of(a, b);
		}

		@Override
		public void adviseObject(final Object aPayload) {
			assert aPayload instanceof Triple;
			var payloadtriple = (Triple<String[], ICompilationBus, CompilationRunner.CR_State>) aPayload;
			var args2         = payloadtriple.getLeft();
			var cb            = payloadtriple.getMiddle();
			var st1           = payloadtriple.getRight();

			adviseObject(args2, st1);
		}
	}
}
