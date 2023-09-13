package tripleo.elijah.stages.deduce.post_bytecode;

import org.jetbrains.annotations.*;
import tripleo.elijah.diagnostic.*;
import tripleo.elijah.util.*;

import java.io.*;
import java.util.*;

public class ZeroPotentialDiagnostic implements Diagnostic {
	@Override
	public String code() {
		NotImplementedException.raise();
		return null;
	}

	@Override
	public Severity severity() {
		NotImplementedException.raise();
		return null;
	}

	@Override
	public @NotNull Locatable primary() {
		NotImplementedException.raise();
		return null;
	}

	@Override
	public @NotNull List<Locatable> secondary() {
		NotImplementedException.raise();
		return null;
	}

	@Override
	public void report(final PrintStream stream) {
		NotImplementedException.raise();
		final int y = 2;
	}
}
