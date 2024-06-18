package tripleo.elijah.comp;

import tripleo.elijah.ci.*;

class CSS2_CCI_Next implements CSS2_Signal {
    @Override
    public void trigger(Compilation compilation, Object payload) {
        assert payload instanceof CompilerInstructions;

        compilation._cis.onNext((CompilerInstructions) payload);
    }
}
