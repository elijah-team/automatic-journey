package tripleo.elijah.stages.gen_c;

import com.google.common.base.Preconditions;
import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedConstructor;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.work.WorkList;
import tripleo.elijah_fluffy.util.SimplePrintLoggerToRemoveSoon;

import java.util.Objects;

class GernNodeFunction extends AbstractGernNode {
    public GernNodeFunction(final GeneratedNode aGeneratedNode) {
        setNode(aGeneratedNode);
    }

    public GeneratedFunction asFunction() {
        return (GeneratedFunction) generatedNode;
    }

    @Override
    public boolean isAttached() {
        final GeneratedFunction gf = asFunction();

        Preconditions.checkNotNull((gf));

        // NOTE defensive
        // noinspection ConstantValue
        return gf.getFD() != null;
    }

    @Override
    public String identityString() {
        return Objects.requireNonNull(asFunction()).identityString();
    }

    @Override
    public void generateCodeForMethod(final BaseGeneratedFunction aGf, final GenerateResult gr, final WorkList aWorkList, final GenerateC aGenerateC, final ElLog LOG) {
        if (aGf instanceof GeneratedConstructor) {
            LOG.err("Should not pass Constructor to GernNodeFunction::generateCodeForMethod " + aGf.identityString());
            return;
        }

        if (!this.isAttached()) {
            SimplePrintLoggerToRemoveSoon.println_out("** Skipping generateCodeForMethod for " + this.identityString());
            return;
        }

        final Generate_Code_For_Method gcfm = new Generate_Code_For_Method(aGenerateC, LOG);
        gcfm.generateCodeForMethod(this, gr, aWorkList);
    }
}
