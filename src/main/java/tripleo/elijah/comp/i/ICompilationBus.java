package tripleo.elijah.comp.i;

import java.util.List;

public interface ICompilationBus {
    void option(CompilationChange aChange);

    void inst(ILazyCompilerInstructions aLazyCompilerInstructions, Instergram reason, Runnable cheat);

    void add(CB_Action aCBAction);

    void add(CB_Process aProcess);

    void add(CB_Process aCBProcess, Object aPayload);

    enum Instergram {
        EZ,
        ZERO,
        TWO_MANY,
        ONE
    }

    interface CB_Action {
        String name();

        void execute();

        OutputString[] outputStrings();
    }

    interface OutputString {
        String getText();
    }

    interface CB_Process {
        //		void execute();

        List<CB_Action> steps();
    }

    class COutputString implements OutputString {

        private final String _text;

        public COutputString(final String aText) {
            _text = aText;
        }

        @Override
        public String getText() {
            return _text;
        }
    }
}
