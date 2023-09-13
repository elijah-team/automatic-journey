package tripleo.elijah.ut;

import tripleo.elijah.comp.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import static tripleo.elijah.util.Helpers.*;

class UT_Root {
	private final Map<String, ICompilationBus.CB_Action> dcs = new HashMap<>();
	List<Path> paths = List_of();

	public UT_Root() {
		final Path path = Paths.get("test");

		try {
			paths = listFiles(path);
		} catch (final IOException aE) {
//			throw new RuntimeException(aE);
			System.err.println(aE);
			return;
		}

		paths.forEach(x -> System.out.println(x));
	}

	public static List<Path> listFiles(final Path path) throws IOException {
		final List<Path> result;
		try (final Stream<Path> walk = Files.walk(path)) {
			result = walk.filter(Files::isRegularFile)
			             .filter(x -> x.toFile().getName().endsWith(".ez"))
			             .collect(Collectors.toList());
		}
		return result;
	}

	public void dcs(final String aF, final ICompilationBus.CB_Action aAction) {
		dcs.put(aF, aAction);
	}

	public ICompilationBus.CB_Action dcs(final String aF) {
		return dcs.get(aF);
	}
}
