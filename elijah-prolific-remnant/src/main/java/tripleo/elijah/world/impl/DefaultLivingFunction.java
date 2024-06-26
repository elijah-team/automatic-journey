package tripleo.elijah.world.impl;

import tripleo.elijah.lang.*;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.world.i.*;

public class DefaultLivingFunction implements LivingFunction {
	private final BaseFunctionDef       _element;
	private final BaseGeneratedFunction _gf;

	public DefaultLivingFunction(final BaseFunctionDef aElement) {
		_element = aElement;
		_gf      = null;
	}

	public DefaultLivingFunction(final BaseGeneratedFunction aFunction) {
		_element = aFunction.getFD();
		_gf      = aFunction;
	}

	@Override
	public BaseFunctionDef getElement() {
		return _element;
	}

	@Override
	public int getCode() {
		return _gf.getCode();
	}
}
