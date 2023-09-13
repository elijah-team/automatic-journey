package tripleo.elijah.stages.gen_c;

import org.jetbrains.annotations.*;
import tripleo.elijah.nextgen.outputstatement.*;
import tripleo.elijah.stages.deduce.post_bytecode.*;

public class EX_ProcTableEntryExplanation implements EX_Explanation {
	private final @NotNull DeduceElement3_ProcTableEntry pte;

	public EX_ProcTableEntryExplanation(final @NotNull DeduceElement3_ProcTableEntry aPte) {
		pte = aPte;
	}

	@Override
	public String getText() {
		return "EX_ProcTableEntryExplanation "+pte.toString();
	}
}
