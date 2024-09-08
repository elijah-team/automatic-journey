package tripleo.elijah.nextgen.outputtree;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.DebugFlags;
import tripleo.elijah.nextgen.outputstatement.EG_Statement;
import tripleo.elijah_prolific.v.V;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author olu
 */
public class EOT_OutputTree {
	private List<EOT_OutputFile> list;

	public void set(final List<EOT_OutputFile> aOutputFileList) {
		if (list == null) {
			list = new ArrayList<>();
		}
		list.addAll(aOutputFileList);
	}

	public void _putSeq(final String aKey, final Path aPath, final EG_Statement aStatement) {
		if (true||DebugFlags.lgSep07) {System.err.printf("[_putSeq] %s %s {{%s}}%n", aKey, aPath, aStatement.getExplanation().getText());}
		V.asv(V.e._putSeq, aKey);
	}

	public int size() {
		return list == null ? 0 : list.size();
	}

	public @NotNull List<@NotNull EOT_OutputFile> list() {
		//noinspection ReplaceNullCheck
		if (list == null) {
			// noinspection unchecked
			return Collections.EMPTY_LIST;
		}
		return list;
	}

	public void add(final EOT_OutputFile aOutputFile) {
		if (list == null) list = new ArrayList<>();
		list.add(aOutputFile);
		// wanted: list().add(aOutputFile);
		// could: set(List_of(aOutputFile));
	}

	public @Nullable List<@NotNull EOT_OutputFile> getList() {
		return list;
	}
}
