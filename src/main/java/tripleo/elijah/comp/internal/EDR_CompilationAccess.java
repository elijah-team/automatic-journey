package tripleo.elijah.comp.internal;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.i.ICompilationAccess;
import tripleo.elijah.comp.PipelineLogic;
import tripleo.elijah.comp.Stages;
import tripleo.elijah.comp.functionality.f202.F202;
import tripleo.elijah.stages.deduce.FunctionMapHook;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah_remnant.startup.ProlificStartup2;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class EDR_CompilationAccess implements ICompilationAccess {
	protected final Compilation      compilation;
	private final   ProlificStartup2 _startup;

	public EDR_CompilationAccess(final Compilation aCompilation, final ProlificStartup2 aStartup) {
		compilation = aCompilation;
		_startup = aStartup;
	}

	@Override
	public void setPipelineLogic(final PipelineLogic pl) {
		assert false;
	}

	@Override
	@NotNull
	public ElLog.Verbosity testSilence() {
		final boolean isSilent = compilation.getCfg().silent;
		if (isSilent) {
			return ElLog.Verbosity.SILENT;
		} else {
			return ElLog.Verbosity.VERBOSE;
		}
	}

	@Override
	public Compilation getCompilation() {
		return compilation;
	}

	@Override
	public void writeLogs() {
		final boolean silent = testSilence() == ElLog.Verbosity.SILENT;

		writeLogs(silent, compilation.getElLogs());
	}

	@Override
	public List<FunctionMapHook> functionMapHooks() {
		return compilation.getDeducePhase().getFunctionMapHooks();
	}

	@Override
	public Stages getStage() {
		return getCompilation().getCfg().stage;
	}

	@Override
	public ProlificStartup2 getStartup() {
		return _startup;
	}

	private void writeLogs(final boolean aSilent, final List<ElLog> aLogs) {
		final Multimap<String, ElLog> logMap = ArrayListMultimap.create();
		if (true) {
			for (final ElLog deduceLog : aLogs) {
				logMap.put(deduceLog.getFileName(), deduceLog);
			}
			for (final Map.Entry<String, Collection<ElLog>> stringCollectionEntry : logMap.asMap().entrySet()) {
				final F202 f202 = new F202(compilation.getErrSink(), compilation);
				f202.processLogs(stringCollectionEntry.getValue());
			}
		}
	}
}
