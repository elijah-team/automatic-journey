package tripleo.elijah_remnant.startup;

import org.jdeferred2.DoneCallback;
import tripleo.elijah.comp.*;
import tripleo.elijah.comp.i.ICompilationBus;
import tripleo.elijah.comp.internal.DefaultCompilationAccess;
import tripleo.elijah.comp.internal.ProcessRecord;
import tripleo.elijah_fluffy.util.Eventual;
import tripleo.elijah_fluffy.util.EventualExtract;

public class ProlificStartup2 {
    private final Compilation                  _compilation;
    private final Eventual<ProcessRecord>      _p_ProcessRecord     = new Eventual<>();
    private final Eventual<ICompilationAccess> _p_CompilationAccess = new Eventual<>();
    private final Eventual<CompilationRunner>  _p_CompilationRunner = new Eventual<>();
    private final Eventual<ICompilationBus>    _p_CompilationBus    = new Eventual<>();
    private final Eventual<PipelineLogic>      _p_PipelineLogic     = new Eventual<>();
    @SuppressWarnings("FieldCanBeLocal")
    private       ProcessRecord                __processRecord;
    private boolean                  _f_processRecord;
    @SuppressWarnings("FieldCanBeLocal")
    private DefaultCompilationAccess __compilationAccess;
    private boolean                  _f_CompilationAccess;
    @SuppressWarnings("FieldCanBeLocal")
    private       CompilationRunner            __CompilationRunner;
    private       boolean                      _f_CompilationRunner;
    @SuppressWarnings("FieldCanBeLocal")
    private       ICompilationBus              __CompilationBus;
    private       boolean                      _f_CompilationBus;
    @SuppressWarnings("FieldCanBeLocal")
    private       PipelineLogic                __PipelineLogic;
    private       boolean                      _f_PipelineLogic;

    public ProlificStartup2(final Compilation aCompilation) {
        _compilation = aCompilation;
    }

    public Eventual<ICompilationAccess> getCompilationAccess() {
        if (!_f_CompilationAccess) {
            _f_CompilationAccess = true;
            __compilationAccess  = new DefaultCompilationAccess(getCompilation(), this);
            _p_CompilationAccess.resolve(__compilationAccess);
        }
        return _p_CompilationAccess;
    }

    private Compilation getCompilation() {
        return _compilation;
    }

    public Eventual<ProcessRecord> getProcessRecord() {
        if (!_f_processRecord) {
            _f_processRecord = true;
            __processRecord  = new ProcessRecord(__compilationAccess);
            _p_ProcessRecord.resolve(__processRecord);
        }
        return _p_ProcessRecord;
    }

    public Eventual<CompilationRunner> getCompilationRunner() {
        if (!_f_CompilationRunner) {
            _f_CompilationRunner = true;
            __CompilationRunner  = new CompilationRunner(_compilation, EventualExtract.of(getCompilationBus()));
            _p_CompilationRunner.resolve(__CompilationRunner);
        }
        return _p_CompilationRunner;
    }

    public Eventual<ICompilationBus> getCompilationBus() {
        // Let ControllerBase resolve this
        /*
         * if (!_f_CompilationBus) { _f_CompilationBus = true; __CompilationBus = new
         * CompilationBus(_compilation); _p_CompilationBus.resolve(__CompilationBus); }
         */
        _p_CompilationBus.then(new DoneCallback<ICompilationBus>() {
            @Override
            public void onDone(final ICompilationBus result) {
                _f_CompilationBus = true;
                __CompilationBus  = result;
            }
        });
        return _p_CompilationBus;
    }

    public Eventual<PipelineLogic> getPipelineLogic() {
        /*
         * if (!_f_PipelineLogic) { _f_PipelineLogic = true; __PipelineLogic = null;
         * _p_PipelineLogic.resolve(__PipelineLogic); }
         */
        return _p_PipelineLogic;
    }

    public void resolvePipelineLogic(final PipelineLogic aX) {
        __PipelineLogic = aX;
        _p_PipelineLogic.resolve(__PipelineLogic);
    }
}
