/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.jdeferred2.DoneCallback;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.ci.LibraryStatementPart;
import tripleo.elijah.comp.functionality.f202.F202;
import tripleo.elijah.comp.i.CompilerController;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Package;
import tripleo.elijah.lang.Qualident;
import tripleo.elijah.nextgen.inputtree.EIT_ModuleInput;
import tripleo.elijah.nextgen.outputtree.EOT_OutputTree;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.stages.deduce.FunctionMapHook;
import tripleo.elijah.stages.deduce.fluffy.i.FluffyComp;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.world.i.WorldModule;
import tripleo.elijah.world.impl.DefaultLivingRepo;
import tripleo.elijah.world.impl.DefaultWorldModule;
import tripleo.elijah_fluffy.comp.CM_Prelude;
import tripleo.elijah_fluffy.comp.CM_Preludes;
import tripleo.elijah_fluffy.util.Eventual;
import tripleo.elijah_fluffy.util.EventualExtract;
import tripleo.elijah_fluffy.util.Helpers;
import tripleo.elijah_prolific.comp_signals.CSS2_Signal;
import tripleo.elijah_remnant.startup.ProlificStartup2;

import java.io.File;
import java.util.*;

public abstract class Compilation {
	private final List<ElLog>                  elLogs              = new LinkedList<>();
	private final CompilationConfig            cfg                 = new CompilationConfig();
	private final EDR_CIS                      _cis                = new EDR_CIS();
	private final DefaultLivingRepo            _repo               = new DefaultLivingRepo();
	private final EDR_MOD                      mod                 = new EDR_MOD();
	private final Pipeline                     pipelines;
	private final int                          _compilationNumber;
	private final ErrSink                      errSink;
	private final IO                           io;
	private final USE                          use                 = new USE(this);
	private final CompFactory                  _con                = new DefaultCompFactory();
	private final Eventual<File>               _m_comp_dir_promise = new Eventual<>();
	private final Finally                      _f                  = new Finally();
	private       PipelineLogic                pipelineLogic;
	private       CompilerInstructionsObserver _cio;
	private       CompilationRunner            __cr;
	private       CompilerInstructions         rootCI;
	private       World                        world;

	public Compilation(final @NotNull ErrSink aErrSink, final IO aIO) {
		errSink            = aErrSink;
		io                 = aIO;
		_compilationNumber = new Random().nextInt(Integer.MAX_VALUE);
		pipelines          = new Pipeline(aErrSink);
	}

	public static ElLog.Verbosity gitlabCIVerbosity() {
		final boolean gitlab_ci = isGitlab_ci();
		return gitlab_ci ? ElLog.Verbosity.SILENT : ElLog.Verbosity.VERBOSE;
	}

	public static boolean isGitlab_ci() {
		return System.getenv("GITLAB_CI") != null;
	}

	@Contract(pure = true)
	private @Nullable CompilationEnclosure getCompilationEnclosure() {
		return null; // new CompilationEnclosure();
	}

	void hasInstructions(final @NotNull List<CompilerInstructions> cis) {
		assert !cis.isEmpty();

		rootCI = cis.get(0);

		__cr.start(rootCI, cfg.do_out);
	}

	public void feedCmdLine(final @NotNull List<String> args) {
		feedCmdLine(args, new DefaultCompilerController());
	}

	private void feedCmdLine(final @NotNull List<String> args1, final CompilerController ctl) {
		if (args1.isEmpty()) {
			ctl.printUsage();
		} else {
			final var launcher = new ProlificCompilationLauncher(this, args1, ctl);
			launcher.launch0();
		}
	}

	public String getProjectName() {
		return rootCI.getName();
	}

	public CM_Module realParseElijjahFile(final String f, final @NotNull File file, final boolean do_out) {
		CM_Module res = new CM_Module();
		res.advise(this, use);
		res.advise(f, file, do_out);
		res.action();
		final Operation<OS_Module> osModuleOperation = res.getOperation();
		return res;
	}

	public void pushItem(final CompilerInstructions aci) {
		_cis.onNext(aci);
	}

	public List<ClassStatement> findClass(final String string) {
		final List<ClassStatement> l = new ArrayList<>();
		for (final OS_Module module : mod.modules) {
			if (module.hasClass(string)) {
				l.add((ClassStatement) module.findClass(string));
			}
		}
		return l;
	}

	public void use(final @NotNull CompilerInstructions compilerInstructions, final boolean do_out) throws Exception {
		use.use(compilerInstructions, do_out); // NOTE Rust
	}

	public int errorCount() {
		return errSink.errorCount();
	}

	void writeLogs(final @NotNull List<ElLog> aLogs) {
		final Multimap<String, ElLog> logMap = ArrayListMultimap.create();
		for (final ElLog deduceLog : aLogs) {
			logMap.put(deduceLog.getFileName(), deduceLog);
		}
		for (final Map.Entry<String, Collection<ElLog>> stringCollectionEntry : logMap.asMap().entrySet()) {
			final F202 f202 = new F202(getErrSink(), this);
			f202.processLogs(stringCollectionEntry.getValue());
		}
	}

	public ErrSink getErrSink() {
		return errSink;
	}

	public IO getIO() {
		return io;
	}

	public void addModule(final OS_Module module, final String fn) {
		mod.addModule(module, fn);
	}

	public OS_Module fileNameToModule(final String fileName) {
		if (mod.containsKey(fileName)) {
			return mod.get(fileName);
		}
		return null;
	}

	public boolean getSilence() {
		return cfg.silent;
	}

	public tripleo.elijah.util.Operation2<OS_Module> findPrelude(final String prelude_name) {
		return use.findPrelude(prelude_name);
	}

	public void addFunctionMapHook(final FunctionMapHook aFunctionMapHook) {
		getDeducePhase().addFunctionMapHook(aFunctionMapHook);
	}

	public @NotNull DeducePhase getDeducePhase() {
		// TODO subscribeDeducePhase??
		return pipelineLogic.getDp();
	}

	public int nextClassCode() {
		return _repo.nextClassCode();
	}

	public int nextFunctionCode() {
		return _repo.nextFunctionCode();
	}

	public OS_Package getPackage(final @NotNull Qualident pkg_name) {
		return _repo.getPackage(pkg_name.toString());
	}

	public OS_Package makePackage(final Qualident pkg_name) {
		return _repo.makePackage(pkg_name);
	}

	public int compilationNumber() {
		return _compilationNumber;
	}

	public String getCompilationNumberString() {
		return String.format("%08x", _compilationNumber);
	}

	@Deprecated
	public int modules_size() {
		return mod.size();
	}

	@NotNull
	public abstract EOT_OutputTree getOutputTree();

	public abstract @NotNull FluffyComp getFluffy();

	public @NotNull List<GeneratedNode> getLGC() {
		return getDeducePhase().generatedClasses.copy();
	}

	public boolean isPackage(final String aPackageName) {
		return _repo.isPackage(aPackageName);
	}

	public Pipeline getPipelines() {
		return pipelines;
	}

	public ModuleBuilder moduleBuilder() {
		return new ModuleBuilder(this);
	}

	public Finally reports() {
		return _f;
	}

	public void signal(@NotNull CSS2_Signal signal, Object payload) {
		signal.trigger(this, payload);
	}

	public void register(Object registerable) {
		if (registerable instanceof CompilationRunner cr) {
			this.__cr = cr;
		}
	}

	public World world() {
		if (this.world == null)
			this.world = new World();
		return this.world;
	}

	public Operation<CM_Prelude> findPrelude2(final @NotNull CM_Preludes aPreludeTag) {
		final CM_Prelude                                result;
		final tripleo.elijah.util.Operation2<OS_Module> x  = findPrelude(aPreludeTag.getName());
		final var                                       _c = this;
		result = new CM_Prelude() {
			@Override
			public OS_Module getModule() {
				if (x.mode() == tripleo.elijah.util.Mode.SUCCESS) {
					return x.success();
				} else {
					throw null;
				}
			}

			@Override
			public CM_Preludes getTag() {
				return aPreludeTag;
			}

			@Override
			public LibraryStatementPart getLsp() {
				return null;
			}

			@Override
			public Compilation getCompilation() {
				return _c;
			}
		};
		return Operation.success(result);
	}

	public abstract ProlificStartup2 getStartup();

	public Eventual<File> comp_dir_promise() {
		return _m_comp_dir_promise;
	}

	public ICompilationAccess _compilationAccess() {
		final Eventual<ICompilationAccess> e = getStartup().getCompilationAccess();
		return EventualExtract.of(e);
	}

	@SuppressWarnings("UnusedReturnValue")
	public <T, U> File inputFile(final File aDirectory, final String aFileName,
	                             final /* BiFunction<Consumer<T>, Consumer<U>, Void> */ _Inputter2<CompilerInstructions> func) {
		// noinspection unchecked
		final T[] t = (T[]) new Object[1];
		// noinspection unchecked
		final U[]  diag = (U[]) new Object[1];
		final File f    = new File(aDirectory, aFileName);
		func.acceptFile(f);
		// noinspection unchecked
		func.apply(tt -> t[0] = (T) tt, uu -> diag[0] = (U) uu);
		return f;
	}

	public List<ElLog> getElLogs() {
		return elLogs;
	}

	public CompilationConfig getCfg() {
		return cfg;
	}

	public EDR_CIS get_cis() {
		return _cis;
	}

	public DefaultLivingRepo get_repo() {
		return _repo;
	}

	public EDR_MOD getMod() {
		return mod;
	}

	public PipelineLogic getPipelineLogic() {
		return pipelineLogic;
	}

	public void setPipelineLogic(PipelineLogic aPipelineLogic) {
		pipelineLogic = aPipelineLogic;
	}

	public CompilerInstructionsObserver get_cio() {
		return _cio;
	}

	public void set_cio(CompilerInstructionsObserver a_cio) {
		_cio = a_cio;
	}

	public CompilationRunner get__cr() {
		return __cr;
	}

	public void set__cr(CompilationRunner a__cr) {
		__cr = a__cr;
	}

	static class CompilationConfig {
		public    boolean do_out;
		public    Stages  stage  = Stages.O; // Output
		protected boolean silent = false;
		boolean showTree = false;
	}

	public static class CompilationAlways {
		public static boolean     VOODOO          = false;
		public static CM_Preludes _defaultPrelude = CM_Preludes.C;

		/**
		 * Return the unquoted name
		 */
		@NotNull
		public static String defaultPrelude() {
			return _defaultPrelude.getName();
		}
	}

	public static class World {
		public void subscribeLgc(DoneCallback<CWS_LGC> consumer) {
//		lgcsub.
		}
	}

	private class DefaultCompFactory implements CompFactory {
		@Override
		public @NotNull EIT_ModuleInput createModuleInput(final OS_Module aModule) {
			return new EIT_ModuleInput(aModule, Compilation.this);
		}

		@Override
		public @NotNull Qualident createQualident(final @NotNull List<String> sl) {
			var R = new Qualident();
			for (String s : sl) {
				R.append(Helpers.string_to_ident(s));
			}
			return R;
		}

		@Override
		public @NotNull InputRequest createInputRequest(final File aFile, final boolean aDo_out,
		                                                final @Nullable LibraryStatementPart aLsp) {
			return new InputRequest(aFile, aDo_out, aLsp);
		}

		@Override
		public @NotNull WorldModule createWorldModule(final OS_Module m) {
			CompilationEnclosure ce = getCompilationEnclosure();
			final WorldModule    R  = new DefaultWorldModule(m, ce);

			return R;
		}
	}
}

//
//
//
