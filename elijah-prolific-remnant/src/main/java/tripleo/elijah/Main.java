/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah;

import tripleo.elijah.comp.Compilation;
import tripleo.elijah.factory.comp.CompilationFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

	public static void main(final String[] args) throws Exception {
		final Compilation cc = CompilationFactory.mkCompilation();
		final List<String> ls = new ArrayList<>();
		ls.addAll(Arrays.asList(args));
		cc.feedCmdLine(ls);
	}
}

//
//
//
