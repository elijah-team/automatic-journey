/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp.internal;

import org.jetbrains.annotations.*;
import tripleo.elijah.comp.*;
import tripleo.elijah.nextgen.outputtree.*;
import tripleo.elijah.stages.deduce.fluffy.i.*;
import tripleo.elijah.stages.deduce.fluffy.impl.*;
import tripleo.elijah.testing.comp.*;
import tripleo.elijah.util.*;
import tripleo.elijah_fluffy.util.*;
import tripleo.elijah_remnant.startup.*;

import java.util.*;

public class CompilationImpl extends Compilation {
	private final @NotNull FluffyCompImpl   _fluffyComp;
	private final          ProlificStartup2 _startup;
	private @Nullable      EOT_OutputTree   _output_tree = null;

	public CompilationImpl(final ErrSink aEee, final IO aIo) {
		super(aEee, aIo);
		_fluffyComp = new FluffyCompImpl(this);
		_startup    = new ProlificStartup2(this);
	}

	public void testMapHooks(final List<IFunctionMapHook> aMapHooks) {
		throw new NotImplementedException();
	}

	@Override
	public @NotNull EOT_OutputTree getOutputTree() {
		if (_output_tree == null) {
			_output_tree = new EOT_OutputTree();
		}

		assert _output_tree != null;

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
