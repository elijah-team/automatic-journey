/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp.internal;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.ci.LibraryStatementPart;
import tripleo.elijah.comp.*;
import tripleo.elijah.comp.functionality.f202.F202;
import tripleo.elijah.comp.i.*;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Package;
import tripleo.elijah.lang.Qualident;
import tripleo.elijah.nextgen.outputtree.EOT_OutputTree;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.stages.deduce.FunctionMapHook;
import tripleo.elijah.stages.deduce.fluffy.i.FluffyComp;
import tripleo.elijah.stages.deduce.fluffy.impl.FluffyCompImpl;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.testing.comp.IFunctionMapHook;
import tripleo.elijah.world.impl.DefaultLivingRepo;
import tripleo.elijah_fluffy.comp.CM_Prelude;
import tripleo.elijah_fluffy.comp.CM_Preludes;
import tripleo.elijah_fluffy.util.Eventual;
import tripleo.elijah_fluffy.util.EventualExtract;
import tripleo.elijah_fluffy.util.NotImplementedException;
import tripleo.elijah_prolific.comp_signals.CSS2_Signal;
import tripleo.elijah_remnant.startup.ProlificStartup2;
import tripleo.elijah_remnant.value.ElValue;

import java.io.File;
import java.util.*;

public class EDR_Compilation implements Compilation {
	private final @NotNull FluffyCompImpl               _fluffyComp;
	private final          ProlificStartup2             _startup;
	private final @NotNull EOT_OutputTree               _output_tree;
	private final          List<ElLog>                  elLogs;
	private final          Eventual<File>               _m_comp_dir_promise;
	private final          CompilationConfig            cfg;
	private final Finally           _f;
	private final CompFactory       _con;
	private final DefaultLivingRepo _repo;
	private final          EDR_CIS                      _cis;
	private final          EDR_MOD                      mod;
	private final          EDR_USE                      use;
	private final          CompilationBusElValue        __cb;
	private final          Pipeline                     pipelines;
	private final int               _compilationNumber;
	private final ErrSink           errSink;
	private final IO                io;
	private                PipelineLogic                pipelineLogic;
	private                CompilerInstructionsObserver _cio;
	private                CompilationRunner            __cr;
	private                CompilerInstructions         rootCI;
	private                World                        world;


	public EDR_Compilation(final @NotNull ErrSink aErrSink, final IO aIO) {
		errSink             = aErrSink;
		io                  = aIO;
		_compilationNumber  = new Random().nextInt(Integer.MAX_VALUE);
		pipelines           = new Pipeline(aErrSink);
		elLogs              = new LinkedList<>();
		__cb                = new CompilationBusElValue();
		_m_comp_dir_promise = new Eventual<>();
		cfg                 = new CompilationConfig();
		_f                  = new Finally();
		_con                = new DefaultCompFactory(this);
		_repo               = new DefaultLivingRepo();
		_cis                = new EDR_CIS();
		mod                 = new EDR_MOD();
		use                 = new EDR_USE(this);
		_fluffyComp         = new FluffyCompImpl(this);
		_startup            = new ProlificStartup2(this);
		_output_tree        = new EOT_OutputTree();
	}

	@Override
	public void testMapHooks(final List<IFunctionMapHook> aMapHooks) {
		throw new NotImplementedException();
	}

//	@Override
//	public @NotNull EOT_OutputTree getOutputTree() {
//		return _output_tree;
//	}
//
//	@Override
//	public @NotNull FluffyComp getFluffy() {
//		return _fluffyComp;
//	}

	@Override
	public ProlificStartup2 getStartup() {
		return _startup;
	}

	@Override
	public ElValue<CompilationBus> get_cb() {
		return null;
	}

	@Override
	public ICompilationAccess _access() {
		final Eventual<ICompilationAccess> cap = _startup.getCompilationAccess();
		return EventualExtract.of(cap);
	}

	@Override
	public void feedCmdLine(final @NotNull List<String> args) {
		feedCmdLine(args, new DefaultCompilerController());
	}

	@Override
	public void feedCmdLine(final @NotNull List<String> args1, final CompilerController ctl) {
		if (args1.isEmpty()) {
			ctl.printUsage();
		} else {
			this.__cb.set(ctl.getCB());
			final var launcher = new ProlificCompilationLauncher(this, args1, ctl);
			launcher.launch0();
		}
	}

	@Override
	public String getProjectName() {
		return rootCI.getName();
	}

	@Override
	public CM_Module realParseElijjahFile(final String f, final @NotNull File file, final boolean do_out) {
		CM_Module res = new CM_Module();
		res.advise(this, use);
		res.advise(f, file, do_out);
		res.action();
		final Operation<OS_Module> osModuleOperation = res.getOperation();
		return res;
	}

	@Override
	public void pushItem(final CompilerInstructions aci) {
		_cis.onNext(aci);
	}

	@Override
	public List<ClassStatement> findClass(final String string) {
		final List<ClassStatement> l = new ArrayList<>();
		for (final OS_Module module : mod.modules) {
			if (module.hasClass(string)) {
				l.add((ClassStatement) module.findClass(string));
			}
		}
		return l;
	}

	@Override
	public void use(final @NotNull CompilerInstructions compilerInstructions, final boolean do_out) throws Exception {
		use.use(compilerInstructions, do_out); // NOTE Rust
	}

	@Override
	public int errorCount() {
		return errSink.errorCount();
	}

	@Override
	public void writeLogs(final @NotNull List<ElLog> aLogs) {
		final Multimap<String, ElLog> logMap = ArrayListMultimap.create();
		for (final ElLog deduceLog : aLogs) {
			logMap.put(deduceLog.getFileName(), deduceLog);
		}
		for (final Map.Entry<String, Collection<ElLog>> stringCollectionEntry : logMap.asMap().entrySet()) {
			final F202 f202 = new F202(getErrSink(), this);
			f202.processLogs(stringCollectionEntry.getValue());
		}
	}

	@Override
	public ErrSink getErrSink() {
		return errSink;
	}

	@Override
	public IO getIO() {
		return io;
	}

	@Override
	public void addModule(final OS_Module module, final String fn) {
		mod.addModule(module, fn);
	}

	@Override
	public OS_Module fileNameToModule(final String fileName) {
		if (mod.containsKey(fileName)) {
			return mod.get(fileName);
		}
		return null;
	}

	@Override
	public boolean getSilence() {
		return cfg.silent;
	}

	@Override
	public tripleo.elijah.util.Operation2<OS_Module> findPrelude(final String prelude_name) {
		return use.findPrelude(prelude_name);
	}

	@Override
	public void addFunctionMapHook(final FunctionMapHook aFunctionMapHook) {
		getDeducePhase().addFunctionMapHook(aFunctionMapHook);
	}

	@Override
	public @NotNull DeducePhase getDeducePhase() {
		// TODO subscribeDeducePhase??
		return pipelineLogic.getDp();
	}

	@Override
	public int nextClassCode() {
		return _repo.nextClassCode();
	}

	@Override
	public int nextFunctionCode() {
		return _repo.nextFunctionCode();
	}

	@Override
	public OS_Package getPackage(final @NotNull Qualident pkg_name) {
		return _repo.getPackage(pkg_name.toString());
	}

	@Override
	public OS_Package makePackage(final Qualident pkg_name) {
		return _repo.makePackage(pkg_name);
	}

	@Override
	public int compilationNumber() {
		return _compilationNumber;
	}

	@Override
	public String getCompilationNumberString() {
		return String.format("%08x", _compilationNumber);
	}

	@Deprecated
	@Override
	public int modules_size() {
		return mod.size();
	}

	@Override
	public @NotNull EOT_OutputTree getOutputTree() {
		return this._output_tree;
	}

	@Override
	public @NotNull FluffyComp getFluffy() {
		return this._fluffyComp;
	}

	@Override
	public @NotNull List<GeneratedNode> getLGC() {
		return getDeducePhase().getGeneratedClasses().copy();
	}

	@Override
	public boolean isPackage(final String aPackageName) {
		return _repo.isPackage(aPackageName);
	}

	@Override
	public Pipeline getPipelines() {
		return pipelines;
	}

	@Override
	public ModuleBuilder moduleBuilder() {
		return new ModuleBuilder(this);
	}

	@Override
	public Finally reports() {
		return _f;
	}

	@Override
	public void signal(@NotNull CSS2_Signal signal, Object payload) {
		signal.trigger(this, payload);
	}

	@Override
	public void register(Object registerable) {
		if (registerable instanceof CompilationRunner cr) {
			this.__cr = cr;
		}
	}

	@Override
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

	public Eventual<File> comp_dir_promise() {
		return _m_comp_dir_promise;
	}

	@Override
	public ICompilationAccess _compilationAccess() {
		final Eventual<ICompilationAccess> e = getStartup().getCompilationAccess();
		return EventualExtract.of(e);
	}

	@SuppressWarnings("UnusedReturnValue")
	@Override
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

	@Override
	public List<ElLog> getElLogs() {
		return elLogs;
	}

	@Override
	public CompilationConfig getCfg() {
		return cfg;
	}

	@Override
	public EDR_CIS get_cis() {
		return _cis;
	}

	@Override
	public DefaultLivingRepo get_repo() {
		return _repo;
	}

	@Override
	public EDR_MOD getMod() {
		return mod;
	}

	@Override
	public PipelineLogic getPipelineLogic() {
		return pipelineLogic;
	}

	@Override
	public void setPipelineLogic(PipelineLogic aPipelineLogic) {
		pipelineLogic = aPipelineLogic;
	}

	@Override
	public CompilerInstructionsObserver get_cio() {
		return _cio;
	}

	@Override
	public void set_cio(CompilerInstructionsObserver a_cio) {
		_cio = a_cio;
	}

	@Override
	public CompilationRunner get__cr() {
		return __cr;
	}

	@Override
	public void set__cr(CompilationRunner a__cr) {
		__cr = a__cr;
	}

	@Override
	public void hasInstructions(final @NotNull List<CompilerInstructions> cis) {
		assert !cis.isEmpty();

		rootCI = cis.get(0);

		__cr.start(rootCI, cfg.do_out);
	}
}

//
//
//
