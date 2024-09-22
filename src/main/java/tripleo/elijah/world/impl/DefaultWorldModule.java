package tripleo.elijah.world.impl;

import tripleo.elijah.comp.CompilationEnclosure;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.nextgen.inputtree.EIT_ModuleInput;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.world.i.WorldModule;
import tripleo.elijah_fluffy.util.Eventual;

public class DefaultWorldModule implements WorldModule {
    private final Eventual<DeducePhase.GeneratedClasses> _p_GeneratedClasses = new Eventual<>();
    private final OS_Module mod;

    public DefaultWorldModule(final OS_Module aMod, final CompilationEnclosure aCe) {
        mod = aMod;
    }

    @Override
    public OS_Module module() {
        return mod;
    }

    @Override
    public EIT_ModuleInput input() {
        return null;
    }

    @Override
    public Eventual<DeducePhase.GeneratedClasses> getEventual() {
        return _p_GeneratedClasses;
    }
}
