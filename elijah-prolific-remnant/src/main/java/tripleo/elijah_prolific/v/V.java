package tripleo.elijah_prolific.v;

import tripleo.elijah.stages.gen_generic.*;
import tripleo.elijah.comp.*;
import tripleo.elijah.nextgen.inputtree.*;
import tripleo.elijah.nextgen.outputstatement.*;
import tripleo.elijah.nextgen.outputtree.*;
import tripleo.elijah.stages.gen_generic.*;

import java.io.*;
import java.util.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

import static tripleo.elijah_fluffy.util.Helpers.List_of;

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
			stream.println(ou);
			final String ns = ab.node.identityString();
//			stream.println(ns);
			final String bt = ab.buffer.getText();
//			stream.println(bt);
			final String x = "{{V.gr}} " + ty + " " + ou + " " + ns;
			addLog(x);
		}
	}

	public static void exit(Compilation c) {
		final String x = "{{V.exit}}";
		addLog(x);

		{
			final List<EIT_Input>                     inputs          = List_of();
			final List<EG_Statement>                  sts             = logs.stream().map(EG_SingleStatement::new).collect(Collectors.toList());
			final EG_SequenceFactory._SequenceBuilder sequenceBuilder = EG_SequenceFactory.newSequence().addParts(sts);
			final EG_Statement                        seq             = sequenceBuilder.build();
			final EOT_OutputFile                      off             = new EOT_OutputFile(c, inputs, "error-report.txt", EOT_OutputType.ERROR_REPORT, seq);
			c.getOutputTree().add(off);
		}
	}

	private static void addLog(final String x) {
		logs.add(x);
//		System.err.println(x);
	}

	public enum e {
		f202_writing_logs, _putSeq, DT2_1785, d399_147, DT2_2304, DT2_2163
	}
}
