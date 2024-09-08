package tripleo.elijah.comp;

import tripleo.elijah.nextgen.outputtree.EOT_OutputFile;

public class Finally_Output {
	private final EOT_OutputFile.FileNameProvider fileNameProvider;
	private final EOT_OutputFile                  off;

	public Finally_Output(final EOT_OutputFile.FileNameProvider aFileNameProvider, final EOT_OutputFile aOff) {
		fileNameProvider = aFileNameProvider;
		off              = aOff;
	}

	public String name() {
		return fileNameProvider.getFilename();
	}
}
