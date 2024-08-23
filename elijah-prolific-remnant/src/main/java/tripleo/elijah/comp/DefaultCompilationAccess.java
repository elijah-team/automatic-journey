package tripleo.elijah.comp;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.functionality.f202.F202;
import tripleo.elijah.stages.deduce.FunctionMapHook;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah_remnant.startup.ProlificStartup2;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DefaultCompilationAccess implements ICompilationAccess {
	protected final Compilation compilation;
	private final ProlificStartup2 _startup;

	public DefaultCompilationAccess(final Compilation aCompilation, final ProlificStartup2 aStartup) {
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
		final boolean isSilent = compilation.cfg.silent;
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

		writeLogs(silent, compilation.elLogs);
	}

	@Override
	public List<FunctionMapHook> functionMapHooks() {
		return compilation.getDeducePhase().functionMapHooks;
	}

	@Override
	public Stages getStage() {
		return getCompilation().cfg.stage;
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
