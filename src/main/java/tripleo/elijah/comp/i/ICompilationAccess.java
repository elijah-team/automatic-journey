package tripleo.elijah.comp.i;

import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.PipelineLogic;
import tripleo.elijah.comp.Stages;
import tripleo.elijah.stages.deduce.FunctionMapHook;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah_remnant.startup.ProlificStartup2;

import java.util.List;

public interface ICompilationAccess {
	void setPipelineLogic(final PipelineLogic pl);

//	void addPipeline(final PipelineMember pl);

	ElLog.Verbosity testSilence();

	Compilation getCompilation();

	void writeLogs();

	List<FunctionMapHook> functionMapHooks();

//	Pipeline pipelines();

	Stages getStage();

	ProlificStartup2 getStartup();
}
