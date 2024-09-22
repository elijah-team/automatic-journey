package tripleo.elijah.stages.gen_c;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.i.ErrSink;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang.types.OS_FuncExprType;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah_fluffy.util.NotImplementedException;

class GetTypeName {
    static String forVTE(@NotNull final VariableTableEntry input) {
        final OS_Type attached = input.type.getAttached();
        if (attached == null)
            return Emit.emit("/*390*/") + "Z__Unresolved*"; // TODO remove this ASAP
        //
        // special case
        //
        if (input.type.genType.getNode() != null)
            return Emit.emit("/*395*/") + getTypeNameForGenClass(input.type.genType.getNode()) + "*";
        //
        if (input.getStatus() == BaseTableEntry.Status.UNCHECKED)
            return "Error_UNCHECKED_Type";
        switch (attached.getType()) {
            case USER_CLASS:
                return attached.getClassOf().name();
            case USER:
                final TypeName typeName = attached.getTypeName();
                final String name;
                if (typeName instanceof NormalTypeName)
                    name = ((NormalTypeName) typeName).getName();
                else
                    name = typeName.toString();
                return String.format(Emit.emit("/*543*/") + "Z<%s>*", name);
            default:
                throw new NotImplementedException();
        }
    }

    static String getTypeNameForGenClass(@NotNull final GeneratedNode aGenClass) {
        final String ty;
        if (aGenClass instanceof GeneratedClass)
            ty = forGenClass((GeneratedClass) aGenClass);
        else if (aGenClass instanceof GeneratedNamespace)
            ty = forGenNamespace((GeneratedNamespace) aGenClass);
        else
            ty = "Error_Unknown_GenClass";
        return ty;
    }

    static String forGenClass(@NotNull final GeneratedClass aGeneratedClass) {
        final String z;
        z = String.format("Z%d", aGeneratedClass.getCode());
        return z;
    }

    static String forGenNamespace(@NotNull final GeneratedNamespace aGeneratedNamespace) {
        final String z;
        z = String.format("Z%d", aGeneratedNamespace.getCode());
        return z;
    }

    static @NotNull String forTypeTableEntry(@NotNull final TypeTableEntry tte) {
        final GeneratedNode res = tte.resolved();
        if (res instanceof final GeneratedContainerNC nc) {
            final int code = nc.getCode();
            return "Z" + code;
        } else
            return "Z<-1>";
    }

    @Deprecated
    static String forOSType(final @NotNull OS_Type ty, final ElLog LOG) {
        if (ty == null)
            throw new IllegalArgumentException("ty is null");
        //
        final String z;
        switch (ty.getType()) {
            case USER_CLASS:
                final ClassStatement el = ty.getClassOf();
                final String name;
                if (ty instanceof NormalTypeName)
                    name = ((NormalTypeName) ty).getName();
                else
                    name = el.getName();
                z = Emit.emit("/*443*/") + String.format("Z%d/*%s*/", el._a.getCode(), name);// .getName();
                break;
            case FUNCTION:
                z = "<function>";
                break;
            case FUNC_EXPR: {
                z = "<function>";
                final OS_FuncExprType fe = (OS_FuncExprType) ty;
                final int             y  = 2;
            }
            break;
            case USER:
                final TypeName typeName = ty.getTypeName();
                LOG.err("Warning: USER TypeName in GenerateC " + typeName);
                final String s = typeName.toString();
                if (s.equals("Unit"))
                    z = "void";
                else
                    z = String.format("Z<Unknown_USER_Type /*%s*/>", s);
                break;
            case BUILT_IN:
                LOG.err("Warning: BUILT_IN TypeName in GenerateC");
                z = "Z" + ty.getBType().getCode(); // README should not even be here, but look at .name() for other code
                // gen schemes
                break;
            case UNIT_TYPE:
                z = "void";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + ty.getType());
        }
        return z;
    }

    @Deprecated
    static String forTypeName(final @NotNull TypeName typeName, final @NotNull ErrSink errSink) {
        if (typeName instanceof RegularTypeName) {
            final String name = ((RegularTypeName) typeName).getName(); // TODO convert to Z-name

            return String.format("Z<%s>/*kklkl*/", name);
//			    return getTypeName(new OS_Type(typeName));
        }
        errSink.reportError("Type is not fully deduced " + typeName);
        return String.format("Z<%s>/*kllkk*/", typeName); // TODO type is not fully deduced
    }
}
