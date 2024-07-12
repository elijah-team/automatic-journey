package tripleo.elijah_prolific.v;

import tripleo.elijah.stages.gen_generic.*;
import com.google.gson.*;
import org.apache.commons.lang3.tuple.*;
import tripleo.elijah.ci.*;
import tripleo.elijah.comp.*;
import tripleo.elijah.nextgen.inputtree.*;
import tripleo.elijah.nextgen.outputstatement.*;
import tripleo.elijah.nextgen.outputtree.*;
import tripleo.elijah.stages.gen_generic.*;

import java.io.*;
import java.util.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.stream.*;

import static tripleo.elijah_fluffy.util.Helpers.List_of;

public class V {
	private static final List<String> logs = new ArrayList<>();
	private static List<_JsonLog> jsonLogs = new ArrayList<>();

	public static void asv(final e aE, final String aKey) {
		final String x = "{{V.asv}} " + aE + " " + aKey;
		addLog(x);
		addJsonLog("asv", ""+aE, List_of(aKey));
		//System.err.println("[debug] "+x);
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
			addJsonLog("gr", "", List_of(ty, ou, ns));
		}
	}

	public static void exit(Compilation c) {
		final String x = "{{V.exit}}";
		addLog(x);
		addJsonLog("exit", "", List_of(""));

		finishJsonLog(c);
	}

	public record _JsonLog(String headCode, String second, List<String> stringList) {}

	public static class _JsonLog_TypeAdapter {}

	private static void addJsonLog(final String aHeadCode, final String aSecond, final List<String> aStringList) {
		jsonLogs.add(new _JsonLog(aHeadCode, aSecond, aStringList));
	}

	private static void finishJsonLog(final Compilation c) {
		//final Gson gson = new Gson();
		//final String jsonString  = gson.toJson(logs);

		final Gson gson = new GsonBuilder()
				.registerTypeAdapter(_JsonLog.class, new _JsonLog_TypeAdapter())
				.enableComplexMapKeySerialization()
				.serializeNulls()
				.setDateFormat(DateFormat.LONG)
				.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
				.setPrettyPrinting()
				.setVersion(1.0)
				.create();
		final String jsonString  = gson.toJson(jsonLogs);


		final List<EIT_Input>                     inputs          = List_of();
		final List<EG_Statement>                  sts             = logs.stream().map(EG_SingleStatement::new).collect(Collectors.toList());
		final EG_SequenceFactory._SequenceBuilder sequenceBuilder = EG_SequenceFactory.newSequence().addParts(sts);
		final EG_Statement                        seq             = sequenceBuilder.build();
		final EOT_OutputFile                      off             = new EOT_OutputFile(c, inputs, "error-report.txt", EOT_OutputType.ERROR_REPORT, seq);
		c.getOutputTree().add(off);

		final EG_Statement                        seq2             = new EG_SingleStatement(jsonString);
		final EOT_OutputFile                      off2             = new EOT_OutputFile(c, inputs, "error-report.json", EOT_OutputType.ERROR_REPORT, seq2);
		c.getOutputTree().add(off2);
	}

	private static void addLog(final String x) {
		logs.add(x);
//		System.err.println(x);
	}

	public enum e {
		f202_writing_logs, _putSeq, DT2_1785, d399_147, DT2_2304, DT2_2163
	}
}
