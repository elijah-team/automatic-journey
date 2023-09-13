package tripleo.elijah.world.impl;

import org.jetbrains.annotations.*;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.world.i.*;

public class DefaultLivingClass implements LivingClass {
	private final ClassStatement _element;
	private final GeneratedClass _gc;

	public DefaultLivingClass(final ClassStatement aElement) {
		_element = aElement;
		_gc      = null;
	}

	public DefaultLivingClass(final @NotNull GeneratedClass aClass) {
		_element = aClass.getKlass();
		_gc      = aClass;
	}

	@Override
	public ClassStatement getElement() {
		return _element;
	}

	@Override
	public int getCode() {
		return _gc.getCode();
	}
}
