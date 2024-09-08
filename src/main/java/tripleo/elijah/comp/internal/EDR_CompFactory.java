package tripleo.elijah.comp.internal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.ci.LibraryStatementPart;
import tripleo.elijah.comp.i.CompFactory;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.CompilationEnclosure;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.Qualident;
import tripleo.elijah.nextgen.inputtree.EIT_ModuleInput;
import tripleo.elijah.world.i.WorldModule;
import tripleo.elijah.world.impl.DefaultWorldModule;
import tripleo.elijah_fluffy.util.Helpers;

import java.io.File;
import java.util.List;

class EDR_CompFactory implements CompFactory {
	private final Compilation compilation;

	public EDR_CompFactory(Compilation compilation) {
		this.compilation = compilation;
	}

	@Override
	public @NotNull EIT_ModuleInput createModuleInput(final OS_Module aModule) {
		return new EIT_ModuleInput(aModule, compilation);
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

	@SuppressWarnings("UnnecessaryLocalVariable")
	@Override
	public @NotNull WorldModule createWorldModule(final OS_Module m) {
		final CompilationEnclosure ce = null;//compilation.getCompilationEnclosure();
		final WorldModule          R  = new DefaultWorldModule(m, ce);
		return R;
	}
}
