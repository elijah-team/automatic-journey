package tripleo.elijah.comp;

import tripleo.elijah.stages.deduce.*;
import tripleo.elijah.stages.logging.*;
import tripleo.elijah_remnant.startup.*;

import java.util.*;

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
