/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah;

import org.junit.Assert;
import org.junit.Test;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.i.ErrSink;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;
import tripleo.elijah.factory.comp.CompilationFactory;

import java.io.File;
import java.util.List;

import static tripleo.elijah_fluffy.util.Helpers.List_of;

/**
 * @author Tripleo(envy)
 */
public class CompilationTest {

	@Test
	public final void testEz() throws Exception {
		final List<String> args = List_of("test/comp_test/main3", "-sE"/* , "-out" */);
		final ErrSink eee = new StdErrSink();
		final Compilation c = CompilationFactory.mkCompilation(eee, new IO());

		c.feedCmdLine(args);

		Assert.assertTrue(c.getIO().recordedRead(new File("test/comp_test/main3/main3.ez")));
		Assert.assertTrue(c.getIO().recordedRead(new File("test/comp_test/main3/main3.elijah")));
		Assert.assertTrue(c.getIO().recordedRead(new File("test/comp_test/fact1.elijah")));
//		Assert.assertTrue(c.cis.size() > 0);
		Assert.assertTrue(c.modules_size() > 2);
	}

}

//
//
//
