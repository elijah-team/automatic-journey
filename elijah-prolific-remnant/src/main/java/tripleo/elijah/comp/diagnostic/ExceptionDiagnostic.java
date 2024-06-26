package tripleo.elijah.comp.diagnostic;

import org.jetbrains.annotations.*;
import tripleo.elijah.diagnostic.*;

import java.io.*;
import java.util.*;

public class ExceptionDiagnostic implements Diagnostic {
	private final Exception e;

	public ExceptionDiagnostic(final Exception aE) {
		e = aE;
	}

	@Override
	public String code() {
		return "9003";
	}

	@Override
	public Severity severity() {
		return Severity.ERROR;
	}

	@Override
	public @NotNull Locatable primary() {
		return null;
	}

	@Override
	public @NotNull List<Locatable> secondary() {
		return null;
	}

	@Override
	public void report(final PrintStream stream) {
		stream.println(code() + " Some exception " + e);
	}
}
