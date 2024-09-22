package tripleo.elijah.comp.internal;

import tripleo.elijah.comp.i.IProgressSink;
import tripleo.elijah.comp.i.ProgressSinkComponent;

public class EDR_ProgressSink implements IProgressSink {
    @Override
    public void note(final int code, final ProgressSinkComponent component, final int type, final Object[] params) {
        //		component.note(code, type, params);
        if (component.isPrintErr(code, type)) {
            final String s = component.printErr(code, type, params);
            System.err.println("[240914 0012] " + s);
        }
    }
}
