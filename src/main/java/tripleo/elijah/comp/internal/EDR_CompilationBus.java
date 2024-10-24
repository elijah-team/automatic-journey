package tripleo.elijah.comp.internal;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.i.CompilationChange;
import tripleo.elijah.comp.i.ICompilationBus;
import tripleo.elijah.comp.i.ILazyCompilerInstructions;
import tripleo.elijah_prolific.comp_signals.CSS2_Advisable;

import java.util.ArrayList;
import java.util.List;

public class EDR_CompilationBus implements ICompilationBus {
    public final List<Pair<ILazyCompilerInstructions, Instergram>> cs = new ArrayList<>();
    private final Compilation c;

    public EDR_CompilationBus(final Compilation aC) {
        c = aC;
    }

    @Override
    public void option(final @NotNull CompilationChange aChange) {
        aChange.apply(c);
    }

    @Override
    public void inst(
            final ILazyCompilerInstructions aLazyCompilerInstructions,
            final @NotNull Instergram reason,
            final Runnable cheat) {
        this.cs.add(Pair.of(aLazyCompilerInstructions, reason));

        if (aLazyCompilerInstructions != null) {
            System.out.println("** [ci] " + aLazyCompilerInstructions.get().getFilename());
            cheat.run();
        } else {
            switch (reason) {
                case EZ -> {
                    assert false; // should remove prob
                }
                case ZERO -> {
                    // assert false; // for another reason
                    int y = 2;
                }
                case TWO_MANY -> {
                    assert false; // for another nother reason
                }
                case ONE -> {
                    assert false; // this should never be hit
                    //					throw new NeverReached();
                }
            }
            cheat.run();
        }
    }

    public void add(final CB_Action action) {
        action.execute();
    }

    @Override
    public void add(final CB_Process aProcess) {
        //		throw new NotImplementedException();

        for (CB_Action action : aProcess.steps()) {
            action.execute();
        }
    }

    @Override
    public void add(final CB_Process aCBProcess, final Object aPayload) {
        if (aCBProcess instanceof CSS2_Advisable advisable) {
            advisable.adviseObject(aPayload);
        } else {
            throw new AssertionError();
        }
        add(aCBProcess);
    }
}
