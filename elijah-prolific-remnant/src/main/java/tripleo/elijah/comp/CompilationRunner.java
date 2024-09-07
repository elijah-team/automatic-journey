package tripleo.elijah.comp;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.DebugFlags;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.comp.caches.DefaultEzCache;
import tripleo.elijah.comp.diagnostic.TooManyEz_ActuallyNone;
import tripleo.elijah.comp.diagnostic.TooManyEz_BeSpecific;
import tripleo.elijah.comp.i.ICompilationBus;
import tripleo.elijah.comp.i.ICompilationBus.Instergram;
import tripleo.elijah.comp.i.IProgressSink;
import tripleo.elijah.comp.i.ProgressSinkComponent;
import tripleo.elijah.comp.internal.ProcessRecord;
import tripleo.elijah.comp.specs.EzCache;
import tripleo.elijah.comp.specs.EzSpec;
import tripleo.elijah.diagnostic.Diagnostic;
import tripleo.elijah.stages.deduce.post_bytecode.Maybe;
import tripleo.elijah.util.Mode;
import tripleo.elijah.util.Operation2;
import tripleo.elijah_prolific.comp_signals.CSS2_AlmostComplete;
import tripleo.elijah_prolific.comp_signals.CSS2_CCI_Accept;
import tripleo.elijah_prolific.comp_signals.CSS2_CCI_Next;
import tripleo.elijah_remnant.startup.ProlificStartup2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static tripleo.elijah_fluffy.util.Helpers.List_of;

public class CompilationRunner {
	private final Compilation         compilation;
	private final ProlificStartup2    _startup;
	private final ICompilationBus     cb;
	private final EzCache             ezCache;
	private final CSS2_AlmostComplete almostComplete;
	private final CSS2_CCI_Accept     cciAcceptSignal;

	@Contract(pure = true)
	public CompilationRunner(final Compilation aCompilation, final ICompilationBus aCb) {
		compilation     = aCompilation;
		cb              = aCb;
		_startup        = compilation.getStartup();
		ezCache         = new DefaultEzCache();
		almostComplete  = new CSS2_AlmostComplete();
		cciAcceptSignal = new CSS2_CCI_Accept();
	}

	void start(final CompilerInstructions ci, final boolean do_out) {
		final CR_State st1 = new CR_State(_startup);

		cb.add(new ICompilationBus.CB_Process() {
			@Override
			public List<ICompilationBus.CB_Action> steps() {
				// 1. find stdlib
				// -- question placement
				// -- ...
				final ICompilationBus.CB_Action a = new ICompilationBus.CB_Action() {
					private final CR_FindStdlibAction aa = new CR_FindStdlibAction();

					@Override
					public String name() {
						return "find stdlib";
					}

					@Override
					public void execute() {
						aa.attach(CompilationRunner.this);
						aa.execute(st1);
					}

					@Override
					public ICompilationBus.OutputString[] outputStrings() {
						return new ICompilationBus.OutputString[0];
					}
				};
				// 2. process the initial
				final ICompilationBus.CB_Action b = new ICompilationBus.CB_Action() {
					private final CR_ProcessInitialAction aa = new CR_ProcessInitialAction(ci, do_out);

					@Override
					public String name() {
						return "process initial action";
					}

					@Override
					public void execute() {
						aa.execute(st1);
					}

					@Override
					public ICompilationBus.OutputString[] outputStrings() {
						return new ICompilationBus.OutputString[0];
					}
				};
				// 3. do rest
				final ICompilationBus.CB_Action c = new ICompilationBus.CB_Action() {
					private final CR_RunBetterAction aa = new CR_RunBetterAction();

					@Override
					public String name() {
						return "run better";
					}

					@Override
					public void execute() {
						aa.attach(CompilationRunner.this);
						aa.execute(st1);
					}

					@Override
					public ICompilationBus.OutputString[] outputStrings() {
						return new ICompilationBus.OutputString[0];
					}
				};

				return List_of(a, b, c);
			}
		});
	}

	private void logProgress(final int number, final String text) {
		switch (number) {
			case 130:
				break;
			default: {

				// noinspection RedundantStringFormatCall
				System.err.println(String.format("%d %s", number, text));
			}
		}
	}

	/**
	 * - I don't remember what absolutePath is for - Cache doesn't add to QueryDB
	 * <p>
	 * STEPS ------
	 * <p>
	 * 1. Get absolutePath 2. Check cache, return early 3. Parse (Query is incorrect
	 * I think) 4. Cache new result
	 *
	 * @param spec
	 * @param cache
	 * @return
	 */
	public Operation<CompilerInstructions> realParseEzFile(final EzSpec spec, final EzCache cache) {
		final @NotNull File file = spec.file();

		final String absolutePath;
		try {
			absolutePath = file.getCanonicalFile().toString();
		} catch (final IOException aE) {
			return Operation.failure(aE);
		}

		final Optional<CompilerInstructions> early = cache.get(absolutePath);

		if (early.isPresent()) {
			return Operation.success(early.get());
		}

		final Operation<CompilerInstructions> cio = CX_ParseEzFile.parseAndCache(spec, ezCache(), absolutePath);
		return cio;
	}

	public EzCache ezCache() {
		return ezCache;
	}

	private @NotNull Operation<CompilerInstructions> findStdLib(final String prelude_name,
	                                                            final @NotNull Compilation c) {
		final ErrSink errSink = c.getErrSink();
		final IO      io      = c.getIO();

		// TODO CP_Paths.stdlib(...)
		final File local_stdlib = new File("lib_elijjah/lib-" + prelude_name + "/stdlib.ez");
		if (local_stdlib.exists()) {
			final EzSpec spec;
			try (final InputStream s = io.readFile(local_stdlib)) {
				spec = new EzSpec(local_stdlib.getName(), s, local_stdlib);
				final Operation<CompilerInstructions> oci = realParseEzFile(spec, ezCache());
				return oci;
			} catch (final Exception e) {
				return Operation.failure(e);
			}
		}

		return Operation.failure(new Exception() {
			public String message() {
				return "No stdlib found";
			}
		});
	}

	private void cci_accept(Maybe<ILazyCompilerInstructions> mcci) {
		compilation.signal(cciAcceptSignal, mcci);
	}

	private @NotNull List<CompilerInstructions> searchEzFiles(final @NotNull File directory, final ErrSink errSink,
	                                                          final IO io, final Compilation c) {
		final QuerySearchEzFiles                     q    = new QuerySearchEzFiles(c, errSink, io, this);
		final Operation2<List<CompilerInstructions>> olci = q.process(directory);

		if (olci.mode() == Mode.SUCCESS) {
			return olci.success();
		}

		errSink.reportDiagnostic(olci.failure());
		return List_of();
	}

	interface CR_Process {
		List<ICompilationBus.CB_Action> steps();
	}

	public interface CR_Action {
		void attach(CompilationRunner cr);

		void execute(CR_State st);

		String name();
	}

	public static class CR_State {
		@SuppressWarnings("FieldCanBeLocal")
		private final ProlificStartup2          _startup;
		public        ICompilationBus.CB_Action cur;
		ICompilationAccess ca;
		ProcessRecord      pr;
		RuntimeProcesses   rt;

		public CR_State(final ProlificStartup2 aStartup) {
			_startup = aStartup;
			var cap = _startup.getCompilationAccess();
			cap.then(ca1 -> ca = ca1);
			var prp = _startup.getProcessRecord();
			prp.then(pr1 -> pr = pr1);
		}

		public ICompilationAccess ca() {
			return ca;
		}
	}

	public class CR_AlmostComplete implements CR_Action {

		private CompilationRunner cr;

		@Override
		public void attach(final CompilationRunner cr) {
			assert cr != null;
			this.cr = cr;
		}

		@Override
		public void execute(final CR_State st) {
			cr.compilation.signal(almostComplete, null);
		}

		@Override
		public String name() {
			return "cis almostComplete";
		}

	}

	public class CR_FindCIs implements CR_Action {

		private final String[] args2;

		public CR_FindCIs(final String[] aArgs2) {
			args2 = aArgs2;
		}

		@Override
		public void attach(final CompilationRunner cr) {

		}

		@Override
		public void execute(final CR_State st) {
			final Compilation c = st.ca().getCompilation();

			final IProgressSink ps = new IProgressSink() {
				@Override
				public void note(final int aCode, final ProgressSinkComponent aCci, final int aType,
				                 final Object[] aParams) {

				}
			};

			find_cis(args2, c, c.getErrSink(), c.getIO(), cb, ps);
		}

		@Override
		public String name() {
			return "find cis";
		}

		protected void find_cis(final @NotNull String @NotNull [] args2,
		                        final @NotNull Compilation c,
		                        final @NotNull ErrSink errSink,
		                        final @NotNull IO io,
		                        final ICompilationBus cb,
		                        final IProgressSink ps) {
			CompilerInstructions ez_file;
			for (final String file_name : args2) {
				final File    f        = new File(file_name);
				final boolean matches2 = Pattern.matches(".+\\.ez$", file_name);
				if (matches2) {
					final ILazyCompilerInstructions ilci = ILazyCompilerInstructions.of(f, c);
					cb.inst(ilci, Instergram.EZ, () -> {cci_accept(new Maybe<>(ilci, null));});
				} else {
					// errSink.reportError("9996 Not an .ez file "+file_name);
					if (f.isDirectory()) {
						final List<CompilerInstructions> ezs = searchEzFiles(f, errSink, io, c);

						switch (ezs.size()) {
							case 0:
								final Diagnostic d_toomany = new TooManyEz_ActuallyNone();
								final Maybe<ILazyCompilerInstructions> m = new Maybe<>(null, d_toomany);
								cb.inst(null, Instergram.ZERO, () -> {cci_accept(m);});
								break;
							case 1:
								ez_file = ezs.get(0);
								final ILazyCompilerInstructions ilci = ILazyCompilerInstructions.of(ez_file);
								final CompilerInstructions finalEz_file = ez_file;
								cb.inst(ilci, Instergram.ONE, () -> {
									if (!DebugFlags.letsRemove_cci_accept) {
										cci_accept(new Maybe<>(ilci, null));
									} else {
										CSS2_CCI_Next css2_cciNext = new CSS2_CCI_Next();
										compilation.signal(css2_cciNext, finalEz_file);
									}
								});
								break;
							default:
								final Diagnostic d_toomany2 = new TooManyEz_BeSpecific();
								final Maybe<ILazyCompilerInstructions> m2 = new Maybe<>(null, d_toomany2);
								cb.inst(null, Instergram.TWO_MANY, () -> cci_accept(m2));
								break;
						}
					} else
						errSink.reportError("9995 Not a directory " + f.getAbsolutePath());
				}
			}
		}
	}

	private static class CR_RunBetterAction implements CR_Action {
		@Override
		public void attach(final CompilationRunner cr) {

		}

		@Override
		public void execute(final CR_State st) {
			st.rt = StageToRuntime.get(st.ca().getStage(), st.ca(), st.pr);

			try {
				st.rt.run_better();
			} catch (final Exception aE) {
				throw new RuntimeException(aE); // FIXME
			}
		}

		@Override
		public String name() {
			return "run better";
		}
	}

	private class CR_FindStdlibAction implements CR_Action {

		@Override
		public void execute(final CR_State st) {
			final Operation<CompilerInstructions> oci = findStdLib(Compilation.CompilationAlways.defaultPrelude(),
					compilation);
			switch (oci.mode()) {
				case SUCCESS -> compilation.pushItem(oci.success()); // caught twice!!
				case FAILURE -> compilation.getErrSink().exception(oci.failure());
				default -> throw new IllegalStateException("Unexpected value: " + oci.mode());
			}
		}

		@Override
		public String name() {
			return "run better";
		}

		@Override
		public void attach(final CompilationRunner cr) {

		}
	}

	private class CR_ProcessInitialAction implements CR_Action {
		private final CompilerInstructions ci;
		private final boolean              do_out;

		public CR_ProcessInitialAction(final CompilerInstructions aCi, final boolean aDo_out) {
			ci     = aCi;
			do_out = aDo_out;
		}

		@Override
		public void attach(final CompilationRunner cr) {

		}

		@Override
		public void execute(final CR_State st) {
			try {
				compilation.use(ci, do_out);
			} catch (final Exception aE) {
				compilation.getErrSink().exception(aE);
			}
		}

		@Override
		public String name() {
			return "process initial";
		}
	}
}
