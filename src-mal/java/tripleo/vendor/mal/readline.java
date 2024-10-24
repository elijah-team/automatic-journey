package tripleo.vendor.mal;

import com.google.common.io.*;
import com.sun.jna.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

class readline {
	static Mode mode = Mode.JNA;
	static String HISTORY_FILE = null;
	static Boolean historyLoaded = false;

	static {
		HISTORY_FILE = System.getProperty("user.home") + "/.mal-history";
	}

	public static String readline(final String prompt) throws EOFException, IOException {
		if (mode == Mode.JNA) {
			return jna_readline(prompt);
		} else {
			return java_readline(prompt);
		}
	}

	public static String jna_readline(final String prompt) throws EOFException, IOException {
		if (!historyLoaded) {
			loadHistory(HISTORY_FILE);
		}
		final String line = RLLibrary.INSTANCE.readline(prompt);
		if (line == null) {
			throw new EOFException();
		}
		RLLibrary.INSTANCE.add_history(line);
		appendHistory(HISTORY_FILE, line);
		return line;
	}

	// Just java readline (no history, or line editing)
	public static String java_readline(final String prompt) throws EOFException, IOException {
		System.out.print(prompt);
		final BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		final String line = buffer.readLine();
		if (line == null) {
			throw new EOFException();
		}
		return line;
	}

	public static void loadHistory(final String filename) {
		final File file = new File(filename);
		try {
			final List<String> lines = Files.readLines(file, StandardCharsets.UTF_8);
			for (final String line : lines) {
				RLLibrary.INSTANCE.add_history(line);
			}
		} catch (final IOException e) {
			// ignore
		}
	}

	public static void appendHistory(final String filename, final String line) {
		try {
			final BufferedWriter w;
			w = new BufferedWriter(new FileWriter(filename, true));
			w.append(line + "\n");
			w.close();
		} catch (final IOException e) {
			// ignore
		}
	}

	public enum Mode {
		JNA, JAVA
	}

	public interface RLLibrary extends Library {
		// Select a library to use.
		// WARNING: GNU readline is GPL.

		// GNU readline (GPL)
		RLLibrary INSTANCE = (RLLibrary) Native.loadLibrary("readline", RLLibrary.class);
		// Libedit (BSD)
//            RLLibrary INSTANCE = (RLLibrary)
//                Native.loadLibrary("edit", RLLibrary.class);

		String readline(String prompt);

		void add_history(String line);
	}

	public static class EOFException extends Exception {
	}
}
