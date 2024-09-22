package tripleo.elijah.stages.gen_c;

import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedConstructor;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.work.WorkJob;
import tripleo.elijah.work.WorkList;
import tripleo.elijah.work.WorkManager;

class WlGenerateFunctionC implements WorkJob {

    private final BaseGeneratedFunction gf;
    private final GenerateResult        gr;
    private final WorkList              wl;
    private final GenerateC             generateC;
    private       boolean               _isDone = false;

    public WlGenerateFunctionC(final BaseGeneratedFunction aGf, final GenerateResult aGr, final WorkList aWl,
                               final GenerateC aGenerateC) {
        gf        = aGf;
        gr        = aGr;
        wl        = aWl;
        generateC = aGenerateC;
    }

    @Override
    public void run(final WorkManager aWorkManager) {
        if (gf instanceof GeneratedFunction)
            generateC.generate_function((GeneratedFunction) gf, gr, wl);
        else
            generateC.generate_constructor((GeneratedConstructor) gf, gr, wl);
        _isDone = true;
    }

    @Override
    public boolean isDone() {
        return _isDone;
    }
}
