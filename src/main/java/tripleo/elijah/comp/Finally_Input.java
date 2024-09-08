package tripleo.elijah.comp;

import tripleo.elijah.comp.i.CompFactory;

public class Finally_Input {
	private final Finally_Nameable nameable;
	private final Finally.Out2     ty;

	public Finally_Input(final Finally_Nameable aNameable, final Finally.Out2 aTy) {
		nameable = aNameable;
		ty       = aTy;
	}

	public Finally_Input(final CompilerInput aInp, final Finally.Out2 aTy) {
		nameable = new Finally_Nameable() {
			@Override
			public String getName() {
				return aInp.getInp();
			}
		};
		ty       = aTy;
	}

	public Finally_Input(final CompFactory.InputRequest aInp, final Finally.Out2 aTy) {
		nameable = new Finally_Nameable() {
			@Override
			public String getName() {
				return aInp.file().toString();
			}
		};
		ty       = aTy;
	}

	@Override
	public String toString() {
		return "Input{" + "name=" + nameable.getName() + ", ty=" + ty + '}';
	}

	public String name() {
		return nameable.getName();
	}
}
