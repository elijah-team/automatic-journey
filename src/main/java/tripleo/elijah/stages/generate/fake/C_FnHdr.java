package tripleo.elijah.stages.generate.fake;

import java.util.List;

import static tripleo.elijah_fluffy.util.Helpers.List_of;

public class C_FnHdr {
    private final String _returnType;
    private final String _fnName;
    private final List<C_FnArg> l;

    public C_FnHdr(final EL_Hdr aEh) {
        l = List_of(new C_FnArg());
        _returnType = "void";
        _fnName = "z100main";
    }

    public String returnType() {
        return _returnType;
    }

    public C_FnArg args(final int aI) {
        return l.get(aI);
    }

    public String fnName() {
        return _fnName;
    }
}
