/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp.internal;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.comp.ICompilationAccess;
import tripleo.elijah.comp.IO;
import tripleo.elijah.nextgen.outputtree.EOT_OutputTree;
import tripleo.elijah.stages.deduce.fluffy.i.FluffyComp;
import tripleo.elijah.stages.deduce.fluffy.impl.FluffyCompImpl;
import tripleo.elijah.testing.comp.IFunctionMapHook;
import tripleo.elijah_fluffy.util.Eventual;
import tripleo.elijah_fluffy.util.EventualExtract;
import tripleo.elijah_fluffy.util.NotImplementedException;
import tripleo.elijah_remnant.startup.ProlificStartup2;

import java.util.List;

public class CompilationImpl extends Compilation {
	private final @NotNull FluffyCompImpl _fluffyComp;
	private final ProlificStartup2 _startup;
	private final @NotNull EOT_OutputTree _output_tree;

	public CompilationImpl(final ErrSink aEee, final IO aIo) {
		super(aEee, aIo);
		_fluffyComp = new FluffyCompImpl(this);
		_startup = new ProlificStartup2(this);
		_output_tree = new EOT_OutputTree();
	}

	public void testMapHooks(final List<IFunctionMapHook> aMapHooks) {
		throw new NotImplementedException();
	}

	@Override
	public @NotNull EOT_OutputTree getOutputTree() {
		return _output_tree;
	}

	@Override
	public @NotNull FluffyComp getFluffy() {
		return _fluffyComp;
	}

	@Override
	public ProlificStartup2 getStartup() {
		return _startup;
	}

	public ICompilationAccess _access() {
		final Eventual<ICompilationAccess> cap = _startup.getCompilationAccess();
		return EventualExtract.of(cap);
	}
}

//
//
//
