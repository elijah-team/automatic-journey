package tripleo.elijah.comp;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.nextgen.outputtree.EOT_OutputFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Finally {
	private final Set<Outs>           outputOffs = new HashSet<>();
	private final List<Finally_Input>  inputs  = new ArrayList<>();
	private final List<Finally_Output> outputs = new ArrayList<>();
	private final List<_401bSpec>      specs   = new ArrayList<>();

	private       boolean         turnAllOutputOff;

	public void turnOutputOff(final Outs aOut) {
		outputOffs.add(aOut);
	}

	public boolean outputOn(final Outs aOuts) {
		return !turnAllOutputOff && !outputOffs.contains(aOuts);
	}

	public void addInput(final Finally_Nameable aNameable, final Out2 ty) {
		inputs.add(new Finally_Input(aNameable, ty));
	}

//	public void addInput(final CompilerInput aInp, final Out2 ty) {
//		inputs.add(new Input(aInp, ty));
//	}

	public boolean containsInput(final String aS) {
		return inputs.stream().anyMatch(i -> i.name().equals(aS));
	}

	public void turnAllOutputOff() {
		turnAllOutputOff = true;
	}

//	public void addInput(final CompFactory.InputRequest aInp, final Out2 ty) {
//		inputs.add(new Input(aInp, ty));
//	}

	public boolean containsCodeOutput(@NotNull final String s) {
		return outputs.stream().anyMatch(i -> i.name().equals(s));
	}

	public void addCodeOutput(final EOT_OutputFile.FileNameProvider aFileNameProvider, final EOT_OutputFile aOff) {
		outputs.add(new Finally_Output(aFileNameProvider, aOff));
	}

	public boolean contains401b(final _401bMatcher aBMatcher) {
		for (_401bSpec spec : specs) {
			assert false;
		}
		return false;
	}

	public void add401b(final _401bSpec spec) {
		specs.add(spec);
	}

	public enum Outs {
		Out_6262, Out_727, Out_350, Out_364, Out_252, Out_2121, Out_486, Out_5757, Out_1069, Out_141, Out_EVTE_159,
		Out_401b
	}

	public enum Out2 {
		EZ, ELIJAH
	}

	public interface _401bSpec {}

	public interface _401bMatcher {
		static _401bMatcher ofFull(final String aS) {
			final _401bMatcher res = new _401bMatcher() {
				@Override
				public boolean matches(final _401bSpec spec) {
					return false;
				}
			};
			return res;
		}

		boolean matches(_401bSpec spec);
	}
}
