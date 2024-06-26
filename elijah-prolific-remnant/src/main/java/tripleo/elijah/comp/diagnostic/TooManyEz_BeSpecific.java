package tripleo.elijah.comp.diagnostic;

import org.jetbrains.annotations.*;
import tripleo.elijah.diagnostic.*;

import java.io.*;
import java.util.*;

public class TooManyEz_BeSpecific implements Diagnostic {
	final String message = "Too many .ez files, be specific.";

	@Override
	public String code() {
		return "9997";
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
		stream.printf("%s %s%n", code(), message);
	}
}
