package tripleo.elijah.stages.generate.fake;

import java.util.List;

public class EL_Hdr {

    private el_outName _writename;
    private el_genClass _declaring;
    private el_type_NoneType _rt;
    private List<el_Arg> _arg;
    private el_outClass _enclosing;
    private el_name _name;

    public void declaring(final el_genClass aMain) {
        _declaring = aMain;
    }

    public void rt(final el_type_NoneType aElTypeNoneType) {
        _rt = aElTypeNoneType;
    }

    public void args(final List<el_Arg> aArgs) {
        _arg = aArgs;
    }

    public void enclosing(final el_outClass aMain) {
        _enclosing = aMain;
    }

    public void name(final el_name aMain) {
        _name = aMain;
    }

    public void writename(final el_outName aMain) {
        _writename = aMain;
    }
}
