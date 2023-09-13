package tripleo.elijah.comp.diagnostic;

import org.jetbrains.annotations.*;
import tripleo.elijah.diagnostic.*;

import java.io.*;
import java.util.*;

class TooManyEz_UseFirst implements Diagnostic {
	final String message = "Too many .ez files, using first.";

	@Override
	public String code() {
		return "9998";
	}

	@Override
	public Severity severity() {
		return Severity.WARN;
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
		stream.printf("%s %s%n", code(), message);
	}
}
