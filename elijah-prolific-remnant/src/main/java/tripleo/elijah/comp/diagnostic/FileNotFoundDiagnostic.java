package tripleo.elijah.comp.diagnostic;

import org.jetbrains.annotations.*;
import tripleo.elijah.diagnostic.*;

import java.io.*;
import java.util.*;

public class FileNotFoundDiagnostic implements Diagnostic {
	private final File f;

	public FileNotFoundDiagnostic(final File aLocal_prelude) {
		f = aLocal_prelude;
	}

	@Override
	public String code() {
		return "9004";
	}

	@Override
	public Severity severity() {
		return Severity.INFO;
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
		stream.println(code() + " File not found " + f.toString());
	}
}
