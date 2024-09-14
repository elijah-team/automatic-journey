/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp;

import org.apache.commons.lang3.tuple.Pair;
import tripleo.elijah.comp.i.ErrSink;
import tripleo.elijah.diagnostic.Diagnostic;
import tripleo.elijah_fluffy.util.SimplePrintLoggerToRemoveSoon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created Mar 25, 2019 at 3:00:39 PM
 *
 * @author tripleo(sb)
 */
public class StdErrSink implements ErrSink {
    private       int       _errorCount;
    private final ErrorList errorList = new ErrorList();

    @Override
    public void exception(final Exception e) {
        _errorCount++;
        SimplePrintLoggerToRemoveSoon.println_err2("exception: " + e);
        e.printStackTrace(System.err);
        errorList.addException(e);
    }

    @Override
    public void reportError(final String message) {
        _errorCount++;
        if (CompilationAlways.VOODOO) {
            var s = String.format("ERROR: %s%n", message);
            SimplePrintLoggerToRemoveSoon.println_err2(s);
        }
        errorList.addWarning(message);
    }

    @Override
    public void reportWarning(final String message) {
        if (CompilationAlways.VOODOO) {
            var s = String.format("WARNING: %s%n", message);
            SimplePrintLoggerToRemoveSoon.println_err2(s);
        }
        errorList.addWarning(message);
    }

    @Override
    public int errorCount() {
        return _errorCount;
    }

    @Override
    public void info(final String message) {
        if (CompilationAlways.VOODOO) {
            var s = String.format("INFO: %s%n", message);
            SimplePrintLoggerToRemoveSoon.println_err2(s);
        }
        errorList.addInfo(message);
    }

    @Override
    public void reportDiagnostic(final Diagnostic diagnostic) {
        if (diagnostic.severity() == Diagnostic.Severity.ERROR)
            _errorCount++;
        if (CompilationAlways.VOODOO)
            diagnostic.report(System.err);
        errorList.addDiagnostic(diagnostic);
    }

    @Override
    public Pair<Desc, Object> _error(final int index) {
        return errorList.get(index - 1);
    }

    @Override
    public List<Pair<Desc, Object>> _errors() {
        return errorList.__errors();
    }

    public enum Desc {INFO_STRING, WARNING_STRING, EXCEPTION_STRING, DIAGNOSTIC}

    public static class ErrorList {
        List<Pair<Desc, Object>> backing = new ArrayList<>();

        public void addDiagnostic(final Diagnostic aDiagnostic) {
            backing.add(Pair.of(Desc.DIAGNOSTIC, aDiagnostic));
        }

        public void addInfo(final String aMessage) {
            backing.add(Pair.of(Desc.INFO_STRING, aMessage));
        }

        public void addWarning(final String aMessage) {
            backing.add(Pair.of(Desc.WARNING_STRING, aMessage));
        }

        public void addException(final Exception aE) {
            backing.add(Pair.of(Desc.EXCEPTION_STRING, "" + aE));
        }

        public Pair<Desc, Object> get(final int index) {
            return backing.get(index);
        }

        public List<Pair<Desc, Object>> __errors() {
            return backing;
        }
    }
}

//
//
//
