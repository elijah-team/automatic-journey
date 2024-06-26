package tripleo.elijah_prolific.v;

import tripleo.elijah.stages.gen_generic.*;

import java.io.*;
import java.util.*;

public class V {
	private static final List<String> logs = new ArrayList<>();

	public static void asv(final e aE, final String aKey) {
		final String x = "{{V.asv}} " + aE + " " + aKey;
		addLog(x);
	}

	public static void gri(final GenerateResult gr) {
		final PrintStream stream = System.out;

		for (GenerateResultItem ab : gr.results()) {
//			stream.println(ab.counter);
			final String ty = "" + ab.ty;
//			stream.println(ty);
			final String ou = ab.output;
//			stream.println(ou);
			final String ns = ab.node.identityString();
//			stream.println(ns);
			final String bt = ab.buffer.getText();
//			stream.println(bt);
			final String x = "{{V.gr}} " + ty + " " + ou + " " + ns;
			addLog(x);
		}
	}

	public static void exit() {
		final String x = "{{V.exit}}";
		addLog(x);

		try {
			final FileOutputStream out1 = new FileOutputStream(new File("out"));
			final PrintStream      out  = new PrintStream(out1);
			for (String log : logs) {
				out.println(log);
			}
		} catch (IOException aE) {
			//throw new RuntimeException(aE);
			assert false;
		}
	}

	private static void addLog(final String x) {
		logs.add(x);
		System.err.println(x);
	}

	public enum e {f202_writing_logs, _putSeq, DT2_1785, d399_147, DT2_2304}
}
