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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang.types.OS_FuncExprType;
import tripleo.elijah.lang.types.OS_FuncType;
import tripleo.elijah.lang.types.OS_UserClassType;
import tripleo.elijah.stages.deduce.*;
import tripleo.elijah.stages.deduce.zero.Zero_FuncExprType;
import tripleo.elijah_fluffy.util.SimplePrintLoggerToRemoveSoon;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Created 5/31/21 1:32 PM
 */
public class GenType {
	private NamespaceStatement resolvedn;
	private OS_Type            typeName; // TODO or just TypeName ??
	private TypeName           nonGenericTypeName;
	private OS_Type            resolved;
	private IInvocation        ci;
	private GeneratedNode      node;
	private FunctionInvocation functionInvocation;

	@Contract(pure = true)
	public GenType(final NamespaceStatement aNamespaceStatement) {
		setResolvedn((aNamespaceStatement));
	}

	public GenType(final @NotNull ClassStatement aClassStatement) {
		setResolved(aClassStatement.getOS_Type());
	}

	public GenType(final OS_Type aAttached, final OS_Type aOS_type, final boolean aB, final TypeName aTypeName,
			final DeduceTypes2 deduceTypes2, final ErrSink errSink, final DeducePhase phase) {
		setTypeName(aAttached);
		setResolved(aOS_type);
		if (aB) {
			setCi(genCI(aTypeName, deduceTypes2, errSink, phase));
		}
	}

	public ClassInvocation genCI(final TypeName aGenericTypeName, final DeduceTypes2 deduceTypes2,
			final ErrSink errSink, final DeducePhase phase) {
		final SetGenCI sgci = new SetGenCI();
		final ClassInvocation ci = sgci.call(this, aGenericTypeName, deduceTypes2, errSink, phase);
		return ci;
	}

	public GenType() {
	}

	@Override
	public boolean equals(final Object aO) {
		if (this == aO)
			return true;
		if (aO == null || getClass() != aO.getClass())
			return false;

		final GenType genType = (GenType) aO;

		if (!Objects.equals(getResolvedn(), genType.getResolvedn()))
			return false;
		if (!Objects.equals(getTypeName(), genType.getTypeName()))
			return false;
		if (!Objects.equals(getNonGenericTypeName(), genType.getNonGenericTypeName()))
			return false;
		if (!Objects.equals(getResolved(), genType.getResolved()))
			return false;
		if (!Objects.equals(getCi(), genType.getCi()))
			return false;
		if (!Objects.equals(getNode(), genType.getNode()))
			return false;
		return Objects.equals(getFunctionInvocation(), genType.getFunctionInvocation());
	}

	@Override
	public int hashCode() {
		int result = getResolvedn() != null ? getResolvedn().hashCode() : 0;
		result = 31 * result + (getTypeName() != null ? getTypeName().hashCode() : 0);
		result = 31 * result + (getNonGenericTypeName() != null ? getNonGenericTypeName().hashCode() : 0);
		result = 31 * result + (getResolved() != null ? getResolved().hashCode() : 0);
		result = 31 * result + (getCi() != null ? getCi().hashCode() : 0);
		result = 31 * result + (getNode() != null ? getNode().hashCode() : 0);
		result = 31 * result + (getFunctionInvocation() != null ? getFunctionInvocation().hashCode() : 0);
		return result;
	}

	public String asString() {
		final String sb = "GenType{" + "resolvedn=" + getResolvedn() + ", typeName=" + getTypeName() + ", nonGenericTypeName="
				+ getNonGenericTypeName() + ", resolved=" + getResolved() + ", ci=" + getCi() + ", node=" + getNode()
				+ ", functionInvocation=" + getFunctionInvocation() + '}';
		return sb;
	}

	public void set(final @NotNull OS_Type aType) {
		switch (aType.getType()) {
		case USER:
			setTypeName(aType);
			break;
		case USER_CLASS:
			setResolved(aType);
			break;
		default:
			SimplePrintLoggerToRemoveSoon.println_err2("48 Unknown in set: " + aType);
		}
	}

	public boolean isNull() {
		if (getResolvedn() != null)
			return false;
		if (getTypeName() != null)
			return false;
		if (getNonGenericTypeName() != null)
			return false;
		if (getResolved() != null)
			return false;
		if (getCi() != null)
			return false;
		return getNode() == null;
	}

	public void copy(final GenType aGenType) {
		if (getResolvedn() == null)
			setResolvedn(aGenType.getResolvedn());
		if (getTypeName() == null)
			setTypeName(aGenType.getTypeName());
		if (getNonGenericTypeName() == null)
			setNonGenericTypeName(aGenType.getNonGenericTypeName());
		if (getResolved() == null)
			setResolved(aGenType.getResolved());
		if (getCi() == null)
			setCi(aGenType.getCi());
		if (getNode() == null)
			setNode(aGenType.getNode());
	}

	public void genCIForGenType2(final DeduceTypes2 aDeduceTypes2) {
		genCI(getNonGenericTypeName(), aDeduceTypes2, aDeduceTypes2._errSink(), aDeduceTypes2._phase());
		final IInvocation invocation = getCi();
		if (invocation instanceof final NamespaceInvocation namespaceInvocation) {
			namespaceInvocation.resolveDeferred().then(new DoneCallback<GeneratedNamespace>() {
				@Override
				public void onDone(final GeneratedNamespace result) {
					setNode(result);
				}
			});
		} else if (invocation instanceof final ClassInvocation classInvocation) {
			classInvocation.resolvePromise().then(new DoneCallback<GeneratedClass>() {
				@Override
				public void onDone(final GeneratedClass result) {
					setNode(result);
				}
			});
		} else {
			if (getResolved() instanceof final OS_FuncExprType funcExprType) {

				final Zero_FuncExprType zfet = aDeduceTypes2.getZero(funcExprType);

				setNode(zfet.genCIForGenType2(aDeduceTypes2));
			} else if (getResolved() instanceof final OS_FuncType funcType) {
				final int y = 2;
			} else
				throw new IllegalStateException("invalid invocation");
		}
	}

	/**
	 * Sets the node for a GenType, invocation must already be set
	 */
	public void genNodeForGenType2() {
//		assert aGenType.nonGenericTypeName != null;

		final IInvocation invocation = getCi();

		if (invocation instanceof final NamespaceInvocation namespaceInvocation) {
			namespaceInvocation.resolveDeferred().then(new DoneCallback<GeneratedNamespace>() {
				@Override
				public void onDone(final GeneratedNamespace result) {
					setNode(result);
				}
			});
		} else if (invocation instanceof final ClassInvocation classInvocation) {
			classInvocation.resolvePromise().then(new DoneCallback<GeneratedClass>() {
				@Override
				public void onDone(final GeneratedClass result) {
					setNode(result);
				}
			});
		} else
			throw new IllegalStateException("invalid invocation");
	}

	public NamespaceStatement getResolvedn() {
		return resolvedn;
	}

	public void setResolvedn(NamespaceStatement aResolvedn) {
		resolvedn = aResolvedn;
	}

	public OS_Type getTypeName() {
		return typeName;
	}

	public void setTypeName(OS_Type aTypeName) {
		typeName = aTypeName;
	}

	public TypeName getNonGenericTypeName() {
		return nonGenericTypeName;
	}

	public void setNonGenericTypeName(TypeName aNonGenericTypeName) {
		nonGenericTypeName = aNonGenericTypeName;
	}

	public OS_Type getResolved() {
		return resolved;
	}

	public void setResolved(OS_Type aResolved) {
		resolved = aResolved;
	}

	public IInvocation getCi() {
		return ci;
	}

	public void setCi(IInvocation aCi) {
		ci = aCi;
	}

	public GeneratedNode getNode() {
		return node;
	}

	public void setNode(GeneratedNode aNode) {
		node = aNode;
	}

	public FunctionInvocation getFunctionInvocation() {
		return functionInvocation;
	}

	public void setFunctionInvocation(FunctionInvocation aFunctionInvocation) {
		functionInvocation = aFunctionInvocation;
	}

	public IInvocation getCi(final Supplier<IInvocation> aSupplier) {
		if (this.ci == null) {
			this.ci = aSupplier.get();
		}
		return this.ci;
	}

	static class SetGenCI {

		public ClassInvocation call(@NotNull final GenType genType, final TypeName aGenericTypeName,
				final @NotNull DeduceTypes2 deduceTypes2, final ErrSink errSink, final DeducePhase phase) {
			if (genType.getNonGenericTypeName() != null) {
				return nonGenericTypeName(genType, deduceTypes2, errSink, phase);
			}
			if (genType.getResolved() != null) {
				final OS_Type.Type type = genType.getResolved().getType();
				switch (type) {
					case USER_CLASS -> {
						return ((OS_UserClassType) genType.getResolved()).resolvedUserClass(genType, aGenericTypeName, deduceTypes2.dcc());
					}
					case FUNCTION -> {
						return ((OS_FuncType) genType.getResolved()).resolvedFunction(genType, aGenericTypeName, deduceTypes2, errSink, phase);
					}
					case FUNC_EXPR -> {
						// TODO what to do here?
						final int y = 2;
					}
				}
			}
			return null;
		}

		private @NotNull ClassInvocation nonGenericTypeName(final @NotNull GenType genType,
				final DeduceTypes2 deduceTypes2, final ErrSink errSink, final DeducePhase phase) {
			@NotNull
			final NormalTypeName aTyn1 = (NormalTypeName) genType.getNonGenericTypeName();
			@Nullable
			final String constructorName = null; // TODO this comes from nowhere

			switch (genType.getResolved().getType()) {
			case GENERIC_TYPENAME:
				final int y = 2; // TODO seems to not be necessary
				assert false;
				return null;
			case USER_CLASS:
				final @NotNull ClassStatement best = genType.getResolved().getClassOf();
				//
				ClassInvocation clsinv2 = ClassInvocationMake.withGenericPart(best, constructorName, aTyn1, deduceTypes2.dcc());
				clsinv2 = phase.registerClassInvocation(clsinv2);
				genType.setCi(clsinv2);
				return clsinv2;
			default:
				throw new IllegalStateException("Unexpected value: " + genType.getResolved().getType());
			}
		}

	}
}

//
//
//
