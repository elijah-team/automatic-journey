package tripleo.elijah.stages.deduce;

import org.jetbrains.annotations.*;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.deduce.post_bytecode.*;
import tripleo.elijah.stages.gen_fn.*;

public class DeduceElement3Holder implements IElementHolder {
	private final IDeduceElement3 element;

	@Contract(pure = true)
	public DeduceElement3Holder(final IDeduceElement3 aElement) {
		element = aElement;
	}

	@Override
	public OS_Element getElement() {
		return element.getPrincipal();
	}

}
