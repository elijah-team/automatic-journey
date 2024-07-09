package tripleo.elijah.lang.types;

import org.jetbrains.annotations.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.gen_fn.GenType;
import tripleo.elijah_fluffy.deduce.*;

import java.text.*;
import java.util.function.*;
import java.text.MessageFormat;
import java.util.List;

public class OS_UserClassType extends __Abstract_OS_Type {
	private final ClassStatement _classStatement;

	public OS_UserClassType(final ClassStatement aClassStatement) {
		_classStatement = aClassStatement;
	}

	@Override
	public OS_Element getElement() {
		return _classStatement;
	}

	@Override
	public Type getType() {
		return Type.USER_CLASS;
	}

	@Override
	public String asString() {
		return MessageFormat.format("<OS_UserClassType {0}>", _classStatement);
	}

	@NotNull
	public ClassInvocation resolvedUserClass(final @NotNull GenType genType, final TypeName aGenericTypeName,
			final DCC dcc) {
		final ClassStatement best = _classStatement;
		@Nullable
		final String constructorName = null; // TODO what to do about this, nothing I guess

		final Supplier<IInvocation> supplier = () -> {
			@Nullable
			ClassInvocation clsinv1;
			clsinv1 = ClassInvocationMake.withGenericPart(best, constructorName, (NormalTypeName) aGenericTypeName,
					dcc);
			if (clsinv1 == null)
				return null;
			clsinv1 = dcc.phase().registerClassInvocation(clsinv1);
			return clsinv1;
		};

		final IInvocation ci = genType.getCi(supplier);
		if (ci instanceof ClassInvocation cci)
			return cci;
		throw new IllegalStateException("[9998-0052] `ci instanceof ClassInvocation cci` failed");
	}

	@Override
	public ClassStatement getClassOf() {
		return _classStatement;
	}

	protected boolean _isEqual(final OS_Type aType) {
		return aType.getType() == Type.USER_CLASS && _classStatement.equals(((OS_UserClassType) aType)._classStatement);
	}
}
