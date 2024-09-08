/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp;

import org.jdeferred2.DoneCallback;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.comp.i.CompilerController;
import tripleo.elijah.comp.i.ErrSink;
import tripleo.elijah.comp.i.ICompilationAccess;
import tripleo.elijah.comp.i._Inputter2;
import tripleo.elijah.comp.internal.CompilationBus;
import tripleo.elijah.comp.internal.EDR_CIS;
import tripleo.elijah.comp.internal.EDR_MOD;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Package;
import tripleo.elijah.lang.Qualident;
import tripleo.elijah.nextgen.outputtree.EOT_OutputTree;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.stages.deduce.FunctionMapHook;
import tripleo.elijah.stages.deduce.fluffy.i.FluffyComp;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.testing.comp.IFunctionMapHook;
import tripleo.elijah.world.impl.DefaultLivingRepo;
import tripleo.elijah_fluffy.comp.CM_Prelude;
import tripleo.elijah_fluffy.comp.CM_Preludes;
import tripleo.elijah_fluffy.util.Eventual;
import tripleo.elijah_prolific.comp_signals.CSS2_Signal;
import tripleo.elijah_remnant.startup.ProlificStartup2;
import tripleo.elijah_remnant.value.ElValue;

import java.io.File;
import java.util.List;

public interface Compilation {

	static ElLog.Verbosity gitlabCIVerbosity() {
		final boolean gitlab_ci = isGitlab_ci();
		return gitlab_ci ? ElLog.Verbosity.SILENT : ElLog.Verbosity.VERBOSE;
	}

	static boolean isGitlab_ci() {
		return System.getenv("GITLAB_CI") != null;
	}

	void testMapHooks(List<IFunctionMapHook> aMapHooks);

	ICompilationAccess _access();

	void feedCmdLine(@NotNull List<String> args);

	void feedCmdLine(@NotNull List<String> args1, CompilerController ctl);

	String getProjectName();

	CM_Module realParseElijjahFile(String f, @NotNull File file, boolean do_out);

	void pushItem(CompilerInstructions aci);

	List<ClassStatement> findClass(String string);

	void use(@NotNull CompilerInstructions compilerInstructions, boolean do_out) throws Exception;

	int errorCount();

	ErrSink getErrSink();

	IO getIO();

	void addModule(OS_Module module, String fn);

	OS_Module fileNameToModule(String fileName);

	boolean getSilence();

	tripleo.elijah.util.Operation2<OS_Module> findPrelude(String prelude_name);

	void addFunctionMapHook(FunctionMapHook aFunctionMapHook);

	@NotNull DeducePhase getDeducePhase();

	int nextClassCode();

	int nextFunctionCode();

	OS_Package getPackage(@NotNull Qualident pkg_name);

	OS_Package makePackage(Qualident pkg_name);

	int compilationNumber();

	String getCompilationNumberString();

	@Deprecated
	int modules_size();

	@NotNull EOT_OutputTree getOutputTree();

	@NotNull FluffyComp getFluffy();

	ProlificStartup2 getStartup();

	ElValue<CompilationBus> get_cb();

	@NotNull List<GeneratedNode> getLGC();

	boolean isPackage(String aPackageName);

	Pipeline getPipelines();

	ModuleBuilder moduleBuilder();

	Finally reports();

	void signal(@NotNull CSS2_Signal signal, Object payload);

	void register(Object registerable);

	World world();

	Operation<CM_Prelude> findPrelude2(@NotNull CM_Preludes aPreludeTag);

	Eventual<File> comp_dir_promise();

	ICompilationAccess _compilationAccess();

	@SuppressWarnings("UnusedReturnValue")
	<T, U> File inputFile(File aDirectory, String aFileName, _Inputter2<CompilerInstructions> func);

	List<ElLog> getElLogs();

	CompilationConfig getCfg();

	EDR_CIS get_cis();

	DefaultLivingRepo get_repo();

	EDR_MOD getMod();

	PipelineLogic getPipelineLogic();

	void setPipelineLogic(PipelineLogic aPipelineLogic);

	CompilerInstructionsObserver get_cio();

	void set_cio(CompilerInstructionsObserver a_cio);

	CompilationRunner get__cr();

	void set__cr(CompilationRunner a__cr);

	void writeLogs(final @NotNull List<ElLog> aLogs);

	void hasInstructions(List<CompilerInstructions> l);

	class World {
		public void subscribeLgc(DoneCallback<CWS_LGC> consumer) {
//		lgcsub.
		}
	}
}

//
//
//
