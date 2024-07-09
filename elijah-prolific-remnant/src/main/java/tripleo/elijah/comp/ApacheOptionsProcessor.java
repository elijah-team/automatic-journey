package tripleo.elijah.comp;

import org.apache.commons.cli.*;
import org.jetbrains.annotations.*;
import tripleo.elijah.comp.i.*;

import java.util.*;

public class ApacheOptionsProcessor implements OptionsProcessor {
	final Options options = new Options();
	final CommandLineParser clp = new DefaultParser();

	@Contract(pure = true)
	public ApacheOptionsProcessor() {
		options.addOption("s", true, "stage: E: parse; O: output");
		options.addOption("showtree", false, "show tree");
		options.addOption("out", false, "make debug files");
		options.addOption("silent", false, "suppress DeduceType output to console");
	}

	@Override
	public String[] process(@NotNull final Compilation c, @NotNull final List<String> args,
			@NotNull final ICompilationBus cb) throws Exception {
		final CommandLine cmd;

		cmd = clp.parse(options, args.toArray(new String[args.size()]));

		if (cmd.hasOption("s")) {
			cb.option(new CC_SetStage(cmd.getOptionValue('s')));
		}
		if (cmd.hasOption("showtree")) {
			cb.option(new CC_SetShowTree(true));
		}
		if (cmd.hasOption("out")) {
			cb.option(new CC_SetDoOut(true));
		}

		if (Compilation.isGitlab_ci() || cmd.hasOption("silent")) {
			cb.option(new CC_SetSilent(true));
		}

		return cmd.getArgs();
	}
}
