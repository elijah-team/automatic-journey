package tripleo.elijah_prolific.gen_c;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;
import tripleo.elijah.stages.instructions.Instruction;
import tripleo.elijah.stages.logging.ElLog;

import java.util.List;

public class PRC_Instruction {
    private final Instruction inst;
    private final BaseGeneratedFunction gf;
    private final ElLog LOG;

    public PRC_Instruction(final Instruction aInst, final @NotNull BaseGeneratedFunction aGf, final ElLog aLOG) {
        inst = aInst;
        gf = aGf;
        LOG = aLOG;
    }

    public List<String> getAssignmentValueArgsList() {
        return GetAssignmentValue.getAssignmentValueArgs(inst, gf, LOG);
    }
}
