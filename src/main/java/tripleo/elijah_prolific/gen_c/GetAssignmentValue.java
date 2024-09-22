package tripleo.elijah_prolific.gen_c;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah_fluffy.util.NotImplementedException;
import tripleo.elijah_fluffy.util.Eventual;
import tripleo.elijah_fluffy.util.Helpers;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.stages.deduce.*;
import tripleo.elijah.stages.gen_c.*;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.instructions.*;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah_fluffy.util.*;

import java.util.*;

import static tripleo.elijah.stages.deduce.DeduceTypes2.to_int;

public class GetAssignmentValue {

    public static String const_to_string(final IExpression expression) {
        if (expression instanceof NumericExpression) {
            return String.valueOf(((NumericExpression) expression).getValue());
        }
        if (expression instanceof CharLitExpression) {
            return String.format("'%s'", expression);
        }
        if (expression instanceof StringExpression) {
            // TODO triple quoted strings and other escaping concerns
            return String.format("\"%s\"", ((StringExpression) expression).getText());
        }

        // FloatLitExpression
        // BooleanExpression
        throw new NotImplementedException();
    }

    private static Eventual<String> FnCallArgsP(
            final @NotNull FnCallArgs fca, final @NotNull BaseGeneratedFunction gf, final ElLog LOG) {
        final StringBuilder sb = new StringBuilder();
        final Instruction inst = fca.getExpression();
        //			LOG.err("9000 "+inst.getName());

        final Eventual<InstructionArgument> arg2 = inst.getArg2(0); // TODO register this where?
        final Eventual<String> result = new Eventual<>();

        arg2.then(x -> {
            assert x instanceof ProcIA;
            final ProcTableEntry pte = gf.getProcTableEntry(to_int(x));
            //				LOG.err("9000-2 "+pte);
            switch (inst.getName()) {
                case CALL: {
                    if (pte.expression_num == null) {
                        //					assert false; // TODO synthetic methods
                        final IdentExpression ptex = (IdentExpression) pte.expression;
                        sb.append(ptex.getText());
                        sb.append(Emit.emit("/*671*/") + "(");

                        final List<String> sll = /*gc.prcFactory().*/
                                new PRC_Instruction(inst, gf, LOG).getAssignmentValueArgsList();
                        sb.append(Helpers.String_join(", ", sll));

                        sb.append(")");
                    } else {
                        final IdentIA ia2 = (IdentIA) pte.expression_num;
                        final IdentTableEntry idte = ia2.getEntry();
                        if (idte.getStatus() == BaseTableEntry.Status.KNOWN) {
                            final CReference reference = new CReference();
                            final FunctionInvocation functionInvocation = pte.getFunctionInvocation();
                            if (functionInvocation == null
                                    || functionInvocation.getFunction() == ConstructorDef.defaultVirtualCtor) {
                                reference.getIdentIAPath(ia2, Generate_Code_For_Method.AOG.GET, null);
                                final List<String> sll = /*gc.prcFactory().*/
                                        new PRC_Instruction(inst, gf, LOG).getAssignmentValueArgsList();
                                reference.args(sll);
                                final String path = reference.build();
                                sb.append(Emit.emit("/*829*/") + path);
                            } else {
                                final BaseGeneratedFunction pte_generated = functionInvocation.getGenerated();
                                if (idte.resolvedType() == null && pte_generated != null)
                                    idte.resolveTypeToClass(pte_generated);
                                reference.getIdentIAPath(ia2, Generate_Code_For_Method.AOG.GET, null);
                                final List<String> sll = /*gc.prcFactory().*/
                                        new PRC_Instruction(inst, gf, LOG).getAssignmentValueArgsList();
                                reference.args(sll);
                                final String path = reference.build();
                                sb.append(Emit.emit("/*827*/") + path);
                            }
                        } else {
                            final String path = gf.getIdentIAPathNormal(ia2);
                            sb.append(Emit.emit("/*828*/") + String.format("%s is UNKNOWN", path));
                        }
                    }
                    result.resolve(sb.toString());
                }
                case CALLS: {
                    CReference reference = null;
                    if (pte.expression_num == null) {
                        final int y = 2;
                        final IdentExpression ptex = (IdentExpression) pte.expression;
                        sb.append(Emit.emit("/*684*/"));
                        sb.append(ptex.getText());
                    } else {
                        // TODO Why not expression_num?
                        reference = new CReference();
                        final IdentIA ia2 = (IdentIA) pte.expression_num;
                        reference.getIdentIAPath(ia2, Generate_Code_For_Method.AOG.GET, null);
                        final List<String> sll = /*gc.prcFactory().*/
                                new PRC_Instruction(inst, gf, LOG).getAssignmentValueArgsList();
                        reference.args(sll);
                        final String path = reference.build();
                        sb.append(Emit.emit("/*807*/") + path);

                        final IExpression ptex = pte.expression;
                        if (ptex instanceof IdentExpression) {
                            sb.append(Emit.emit("/*803*/"));
                            sb.append(((IdentExpression) ptex).getText());
                        } else if (ptex instanceof ProcedureCallExpression) {
                            sb.append(Emit.emit("/*806*/"));
                            sb.append(ptex.getLeft()); // TODO Qualident, IdentExpression, DotExpression
                        }
                    }
                    if (true /*reference == null*/) {
                        sb.append(Emit.emit("/*810*/") + "(");
                        {
                            final List<String> sll = /*gc.prcFactory().*/
                                    new PRC_Instruction(inst, gf, LOG).getAssignmentValueArgsList();
                            sb.append(Helpers.String_join(", ", sll));
                        }
                        sb.append(");");
                    }
                    result.resolve(sb.toString());
                }
                default:
                    throw new IllegalStateException("Unexpected value: " + inst.getName());
            }
        });

        //			if (inst.getName() != InstructionName.NOP)
        //				assert result.isResolved();
        return result;
    }

    @NotNull
    public static List<String> getAssignmentValueArgs(
            final @NotNull Instruction inst, final BaseGeneratedFunction gf, final ElLog LOG) {
        final int args_size = inst.getArgsSize();
        final List<String> sll = new ArrayList<String>();
        for (int i = 1; i < args_size; i++) {
            final InstructionArgument ia = inst.getArg(i);
            final int y = 2;
            //			LOG.err("7777 " +ia);
            if (ia instanceof ConstTableIA) {
                final ConstantTableEntry constTableEntry = gf.getConstTableEntry(((ConstTableIA) ia).getIndex());
                sll.add(const_to_string(constTableEntry.initialValue));
            } else if (ia instanceof IntegerIA) {
                final VariableTableEntry variableTableEntry = gf.getVarTableEntry(((IntegerIA) ia).getIndex());
                sll.add(Emit.emit("/*853*/") + GenerateC.getRealTargetName(gf, variableTableEntry));
            } else if (ia instanceof IdentIA) {
                final String path = gf.getIdentIAPathNormal((IdentIA) ia); // return x.y.z
                final IdentTableEntry ite = gf.getIdentTableEntry(to_int(ia));
                if (ite.getStatus() == BaseTableEntry.Status.UNKNOWN) {
                    sll.add(String.format("%s is UNKNOWN", path));
                } else {
                    final CReference reference = new CReference();
                    reference.getIdentIAPath((IdentIA) ia, Generate_Code_For_Method.AOG.GET, null);
                    final String path2 = reference.build(); // return ZP105get_z(vvx.vmy)
                    if (path.equals(path2)) {
                        // should always fail
                        // throw new AssertionError();
                        LOG.err(String.format("864 should always fail but didn't %s %s", path, path2));
                    }
                    //					assert ident != null;
                    //					IdentTableEntry ite = gf.getIdentTableEntry(((IdentIA) ia).getIndex());
                    //					sll.add(Emit.emit("/*748*/")+""+ite.getIdent().getText());
                    sll.add(Emit.emit("/*748*/") + path2);
                    LOG.info("743 " + path2 + " " + path);
                }
            } else if (ia instanceof ProcIA) {
                LOG.err("863 ProcIA");
                throw new NotImplementedException();
            } else {
                throw new IllegalStateException("Cant be here: Invalid InstructionArgument");
            }
        }
        return sll;
    }

    public String FnCallArgs(final @NotNull FnCallArgs fca, final @NotNull BaseGeneratedFunction gf, final ElLog LOG) {
        final Eventual<String> result = FnCallArgsP(fca, gf, LOG);

        if (result.isPending()) {
            return ""; // failure condition 1
        }
        if (result.isFailed()) {
            return ""; // failure condition 2
        }
        if (result.isResolved()) {
            return EventualExtract.of(result);
        }

        throw new ProgramIsLikelyWrong();
    }

    public String ConstTableIA(final @NotNull ConstTableIA constTableIA, final @NotNull BaseGeneratedFunction gf) {
        final ConstantTableEntry cte = gf.getConstTableEntry(constTableIA.getIndex());
        //			LOG.err(("9001-3 "+cte.initialValue));
        switch (cte.initialValue.getKind()) {
            case NUMERIC:
                return const_to_string(cte.initialValue);
            case STRING_LITERAL:
                return const_to_string(cte.initialValue);
            case IDENT:
                final String text = ((IdentExpression) cte.initialValue).getText();
                if (BuiltInTypes.isBooleanText(text)) return text;
                else throw new NotImplementedException();
            default:
                throw new NotImplementedException();
        }
    }

    public String IntegerIA(final @NotNull IntegerIA integerIA, final @NotNull BaseGeneratedFunction gf) {
        final VariableTableEntry vte = gf.getVarTableEntry(integerIA.getIndex());
        final String x = GenerateC.getRealTargetName(gf, vte);
        return x;
    }

    public String IdentIA(final @NotNull IdentIA identIA, final BaseGeneratedFunction gf) {
        assert gf == identIA.gf;
        final CReference reference = new CReference();
        reference.getIdentIAPath(identIA, Generate_Code_For_Method.AOG.GET, null);
        return reference.build();
    }

    public String forClassInvocation(
            final @NotNull Instruction aInstruction,
            final ClassInvocation aClsinv,
            final @NotNull BaseGeneratedFunction gf,
            final ElLog LOG) {
        final int y = 2;
        final InstructionArgument _arg0 = aInstruction.getArg(0);
        @NotNull final ProcTableEntry pte = gf.getProcTableEntry(((ProcIA) _arg0).getIndex());
        final CtorReference reference = new CtorReference();
        reference.getConstructorPath(pte.expression_num, gf);
        final List<String> sll = /*gc.prcFactory().*/
                new PRC_Instruction(aInstruction, gf, LOG).getAssignmentValueArgsList();
        reference.args(sll);
        return reference.build(aClsinv);
    }
}
