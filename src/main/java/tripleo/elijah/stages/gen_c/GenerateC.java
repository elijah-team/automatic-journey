/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_c;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.comp.i.ErrSink;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang.types.OS_FuncExprType;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.lang2.SpecialVariables;
import tripleo.elijah.nextgen.model.SM_ClassDeclaration;
import tripleo.elijah.nextgen.model.SM_Node;
import tripleo.elijah.nextgen.outputstatement.EG_SingleStatement;
import tripleo.elijah.nextgen.outputstatement.EG_Statement;
import tripleo.elijah.nextgen.outputstatement.EX_Explanation;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.deduce.FunctionInvocation;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.gen_generic.CodeGenerator;
import tripleo.elijah.stages.gen_generic.GenerateFiles;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.stages.gen_generic.OutputFileFactoryParams;
import tripleo.elijah.stages.instructions.*;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.work.WorkJob;
import tripleo.elijah.work.WorkList;
import tripleo.elijah.work.WorkManager;
import tripleo.elijah_fluffy.util.*;
import tripleo.util.buffer.Buffer;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static tripleo.elijah.stages.deduce.DeduceTypes2.to_int;

/**
 * Created 10/8/20 7:13 AM
 */
public class GenerateC implements CodeGenerator, GenerateFiles {
    private static final String PHASE = "GenerateC";
    final ErrSink errSink;
    private final ElLog LOG;
    Map<GeneratedNode, GernNode> gerns = new HashMap<>();

    public GenerateC(final @NotNull OutputFileFactoryParams p) {
        errSink = p.getErrSink();
        LOG = new ElLog(p.getModFileName(), p.getVerbosity(), PHASE);
        //
        p.addLog(LOG);
    }

    public static @NotNull String getRealTargetName(final BaseGeneratedFunction gf,
                                                    final @NotNull VariableTableEntry varTableEntry) {
        final String vte_name = varTableEntry.getName();
        if (varTableEntry.vtt == VariableTableType.TEMP) {
            if (varTableEntry.getName() == null) {
                return "vt" + varTableEntry.tempNum;
            } else {
                return "vt" + varTableEntry.getName();
            }
        } else if (varTableEntry.vtt == VariableTableType.ARG) {
            return "va" + vte_name;
        } else if (SpecialVariables.contains(vte_name)) {
            return SpecialVariables.get(vte_name);
        } else if (isValue(gf, vte_name)) {
            return "vsc->vsv";
        } else {
            return Emit.emit("/*879*/") + "vv" + vte_name;
        }
    }

    private static boolean isValue(final BaseGeneratedFunction gf, final @NotNull String name) {
        if (!name.equals("Value"))
            return false;
        //
        final FunctionDef fd = (FunctionDef) gf.getFD();
        switch (fd.getSpecies()) {
            case REG_FUN:
            case DEF_FUN:
                if (!(fd.getParent() instanceof ClassStatement))
                    return false;
                for (final AnnotationPart anno : ((ClassStatement) fd.getParent()).annotationIterable()) {
                    if (anno.annoClass().equals(Helpers.string_to_qualident("Primitive"))) {
                        return true;
                    }
                }
                return false;
            case PROP_GET:
            case PROP_SET:
                return true;
            default:
                throw new IllegalStateException("Unexpected value: " + fd.getSpecies());
        }
    }

    private static EG_Statement _CALL_stmt(final GetAssignmentValue gav,
                                           final @NotNull BaseGeneratedFunction gf,
                                           final ElLog LOG,
                                           final ProcTableEntry pte,
                                           final Instruction inst) {
        final CALL_stmt r = new CALL_stmt();
        r.set(gav, gf, LOG, pte, inst);
        return r.getStatement();
    }

    private static @NotNull EG_Statement _CALLS_stmt(final GetAssignmentValue b, final @NotNull BaseGeneratedFunction gf, final ElLog LOG, final ProcTableEntry pte, final StringBuilder sb, final Instruction inst) {
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
            final List<String> sll = b.getAssignmentValueArgs(inst, gf, LOG);
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
        if (true /* reference == null */) {
            sb.append(Emit.emit("/*810*/") + "(");
            {
                final List<String> sll = b.getAssignmentValueArgs(inst, gf, LOG);
                sb.append(Helpers.String_join(", ", sll));
            }
            sb.append(");");
        }
        EG_Statement st = new EG_SingleStatement(sb.toString(), EX_Explanation.withMessage("_CALLS_stmt"));
        return st;
    }

    public GenerateResult generateCode1(final Collection<GernNode> nodes, final Collection<GeneratedNode> lgn, final WorkManager wm) {
        final GenerateResult gr = new GenerateResult();
        if (nodes != null) for (GernNode gerN : nodes) {
            if (gerN.asFunction() != null) {
                final GeneratedFunction generatedFunction = gerN.asFunction();
                final WorkList wl = new WorkList();
                generate_function(generatedFunction, gr, wl);
                if (!wl.isEmpty())
                    wm.addJobs(wl);
            } else if (gerN.asContainer() != null) {
                final GeneratedContainerNC containerNC = gerN.asContainer();
                assert containerNC != null;
                containerNC.generateCode(this, gr);
            } else if (gerN.asConstructor() != null) {
                final GeneratedConstructor generatedConstructor = gerN.asConstructor();
                final WorkList wl = new WorkList();
                generate_constructor(generatedConstructor, gr, wl);
                if (!wl.isEmpty())
                    wm.addJobs(wl);
            } else assert false;
        }
        else {
            for (final GeneratedNode generatedNode : lgn) {
                if (generatedNode instanceof final GeneratedFunction generatedFunction) {
                    final WorkList wl = new WorkList();
                    generate_function(generatedFunction, gr, wl);
                    if (!wl.isEmpty())
                        wm.addJobs(wl);
                } else if (generatedNode instanceof final GeneratedContainerNC containerNC) {
                    containerNC.generateCode(this, gr);
                } else if (generatedNode instanceof final GeneratedConstructor generatedConstructor) {
                    final WorkList wl = new WorkList();
                    generate_constructor(generatedConstructor, gr, wl);
                    if (!wl.isEmpty())
                        wm.addJobs(wl);
                }
            }
        }
        return gr;
    }

    @NotNull
    public String getTypeName(final GeneratedNode aNode) {
        if (aNode instanceof GeneratedClass)
            return getTypeName((GeneratedClass) aNode);
        if (aNode instanceof GeneratedNamespace)
            return getTypeName((GeneratedNamespace) aNode);
        throw new IllegalStateException("Must be class or namespace.");
    }

    String getTypeName(@NotNull final GeneratedClass aGeneratedClass) {
        return GetTypeName.forGenClass(aGeneratedClass);
    }

    String getTypeName(@NotNull final GeneratedNamespace aGeneratedNamespace) {
        return GetTypeName.forGenNamespace(aGeneratedNamespace);
    }

    public void generate_function(final GeneratedFunction aGeneratedFunction, final GenerateResult gr,
                                  final WorkList wl) {
        generateCodeForMethod(aGeneratedFunction, gr, wl);
        for (final IdentTableEntry identTableEntry : aGeneratedFunction.idte_list) {
            if (identTableEntry.isResolved()) {
                final GeneratedNode x = identTableEntry.resolvedType();

                if (x instanceof GeneratedClass) {
                    generate_class((GeneratedClass) x, gr);
                } else if (x instanceof GeneratedFunction) {
                    wl.addJob(new WlGenerateFunctionC((GeneratedFunction) x, gr, wl, this));
                } else {
                    LOG.err(x.toString());
                    throw new NotImplementedException();
                }
            }
        }
        for (final ProcTableEntry pte : aGeneratedFunction.prte_list) {
//			ClassInvocation ci = pte.getClassInvocation();
            final FunctionInvocation fi = pte.getFunctionInvocation();
            if (fi == null) {
                // TODO constructor
                final int y = 2;
                assert false;
            } else {
                final BaseGeneratedFunction gf = fi.getGenerated();
                if (gf != null) {
                    wl.addJob(new WlGenerateFunctionC(gf, gr, wl, this));
                }
            }
        }
    }

    public void generate_constructor(final GeneratedConstructor aGeneratedConstructor, final GenerateResult gr,
                                     final WorkList wl) {
        generateCodeForConstructor(aGeneratedConstructor, gr, wl);
        for (final IdentTableEntry identTableEntry : aGeneratedConstructor.idte_list) {
            if (identTableEntry.isResolved()) {
                final GeneratedNode x = identTableEntry.resolvedType();

                if (x instanceof GeneratedClass) {
                    generate_class((GeneratedClass) x, gr);
                } else if (x instanceof GeneratedFunction) {
                    wl.addJob(new WlGenerateFunctionC((GeneratedFunction) x, gr, wl, this));
                } else {
                    LOG.err(x.toString());
                    throw new NotImplementedException();
                }
            }
        }
        for (final ProcTableEntry pte : aGeneratedConstructor.prte_list) {
//			ClassInvocation ci = pte.getClassInvocation();
            final FunctionInvocation fi = pte.getFunctionInvocation();
            if (fi == null) {
                // TODO constructor
                final int y = 2;
            } else {
                final BaseGeneratedFunction gf = fi.getGenerated();
                if (gf != null) {
                    wl.addJob(new WlGenerateFunctionC(gf, gr, wl, this));
                }
            }
        }
    }

    @Override
    public void generate_namespace(final GeneratedNamespace x, final GenerateResult gr) {
        if (x.generatedAlready)
            return;
        // TODO do we need `self' parameters for namespace?
        final BufferTabbedOutputStream tosHdr = new BufferTabbedOutputStream();
        final BufferTabbedOutputStream tos = new BufferTabbedOutputStream();
        try {
            if (x.varTable.size() > 0) {
                tosHdr.put_string_ln("typedef struct {");
                tosHdr.incr_tabs();
//				tosHdr.put_string_ln("int _tag;");
                for (final GeneratedNamespace.VarTableEntry o : x.varTable) {
                    final String typeName = getTypeNameGNCForVarTableEntry(o);

                    tosHdr.put_string_ln(
                            String.format("%s* vm%s;", o.varType == null ? "void " : typeName, o.nameToken));
                }

                final String class_name = getTypeName(x);
                final int class_code = x.getCode();

                tosHdr.dec_tabs();
                tosHdr.put_string_ln("");
                tosHdr.put_string_ln(String.format("} %s; // namespace `%s'", class_name, x.getName()));
                // TODO "instance" namespaces
                tosHdr.put_string_ln("");
                tosHdr.put_string_ln(
                        String.format("Z%d zN%s_instance; // namespace `%s'", class_code, class_name, x.getName()));
                tosHdr.put_string_ln("");

                tosHdr.put_string_ln("");
                tosHdr.put_string_ln("");
                tos.put_string_ln(String.format("%s* ZN%d() {", class_name, class_code));
                tos.incr_tabs();
                // TODO multiple calls of namespace function (need if/else statement)
                tos.put_string_ln(String.format("%s* R = GC_malloc(sizeof(%s));", class_name, class_name));
//				tos.put_string_ln(String.format("R->_tag = %d;", class_code));
                for (final GeneratedNamespace.VarTableEntry o : x.varTable) {
//					final String typeName = getTypeNameForVarTableEntry(o);
                    tosHdr.put_string_ln(String.format("R->vm%s = 0;", o.nameToken));
                }
                tos.put_string_ln("");
                tos.put_string_ln(String.format("zN%s_instance = R;", class_name));
                tos.put_string_ln("return R;");
                tos.dec_tabs();
                tos.put_string_ln(String.format("} // namespace `%s'", x.getName()));
                tos.put_string_ln("");
                tos.flush();
            }
        } finally {
            tos.close();
            tosHdr.close();
            if (x.varTable.size() > 0) { // TODO should we let this through?
                final Buffer buf = tos.getBuffer();
//				LOG.info(buf.getText());
                gr.addNamespace(GenerateResult.TY.IMPL, x, buf, x.module().getLsp());
                final Buffer buf2 = tosHdr.getBuffer();
//				LOG.info(buf2.getText());
                gr.addNamespace(GenerateResult.TY.HEADER, x, buf2, x.module().getLsp());
            }
        }
        x.generatedAlready = true;
    }

    @Override
    public GenerateResult generateCode(final @NotNull Collection<GeneratedNode> aNodeCollection, final @NotNull WorkManager aWorkManager) {
        return generateCode1(null, aNodeCollection, aWorkManager);
    }

    @Override
    public void forNode(final SM_Node aNode) {
        final int y = 2;
        if (aNode instanceof final SM_ClassDeclaration classDecl) {
            // return classDecl;
        }
//		return null;
    }

    @NotNull
    public String getTypeNameGNCForVarTableEntry(final GeneratedContainer.VarTableEntry o) {
        final String typeName;
        if (o.resolvedType() != null) {
            final GeneratedNode xx = o.resolvedType();
            if (xx instanceof GeneratedClass) {
                typeName = getTypeName((GeneratedClass) xx);
            } else if (xx instanceof GeneratedNamespace) {
                typeName = getTypeName((GeneratedNamespace) xx);
            } else
                throw new NotImplementedException();
        } else {
            if (o.varType != null)
                typeName = getTypeName(o.varType);
            else
                typeName = "void*/*null*/";
        }
        return typeName;
    }

    public GenerateResult resultsFromNodes(final List<GeneratedNode> aNodes, final WorkManager wm) {
        final GenerateC ggc = this;

        final GenerateResult gr2 = new GenerateResult();

        for (final GeneratedNode generatedNode : aNodes) {
//			if (generatedNode.module() != mod) continue; // README curious

            if (generatedNode instanceof final GeneratedContainerNC nc) {

                nc.generateCode(ggc, gr2);
                final @NotNull Collection<GeneratedNode> gn1 = (nc.functionMap.values()).stream()
                        .map(x -> (GeneratedNode) x).collect(Collectors.toList());
                final GenerateResult gr3 = gern(gn1).generateCode(ggc, wm);
                gr2.results().addAll(gr3.results());
                final @NotNull Collection<GeneratedNode> gn2 = (nc.classMap.values()).stream()
                        .map(x -> (GeneratedNode) x).collect(Collectors.toList());
                final GenerateResult gr4 = gern(gn2).generateCode(ggc, wm);
                gr2.results().addAll(gr4.results());
            } else {
                SimplePrintLoggerToRemoveSoon.println2("2009 " + generatedNode.getClass().getName());
            }
        }

        return gr2;
    }

    public GenerateResult resultsFromNodes(final Gerns aNodes, final WorkManager wm) {
        final GenerateC ggc = this;

        final GenerateResult gr2 = new GenerateResult();

        for (GernNode gernNode : aNodes) {
            final GeneratedNode generatedNode = gernNode.carrier();

            //			if (generatedNode.module() != mod) continue; // README curious

            if (gernNode.asContainer() != null) {
                // FIXME idea bad
                @Nullable final GeneratedContainerNC nc = Objects.requireNonNull(gernNode.asContainer());
                final GernNodeContainer nc2 = ((GernNodeContainer) gernNode);

                nc2.generateCode(ggc, gr2);

                // FIXME Simplify and preserve these two
                final GenerateResult gr3 = nc2.generateCodeForFunctions(wm, nc, ggc, this);
                gr2.results().addAll(gr3.results());
                final GenerateResult gr4 = nc2.generateCodeForContainers(wm, nc, ggc, this);
                gr2.results().addAll(gr4.results());
            } else {

                SimplePrintLoggerToRemoveSoon.println2("2009 " + generatedNode.getClass().getName());
            }
        }

        return gr2;
    }

    private GenerateResult generateCode77(final Collection<GernNode> aGern, final WorkManager aWm) {
        final Collection<GeneratedNode> q = new ArrayList<>();
        for (GernNode gernNode : aGern) {
            q.add(gernNode.carrier());
        }
        return generateCode(q, aWm);
    }

    public @NotNull Gerns gern(final @NotNull Collection<GeneratedNode> aGeneratedNodes) {
        return new Gerns() {
            final Collection<GernNode> res = aGeneratedNodes.stream()
                    .map(generatedNode -> gern(generatedNode))
                    .toList();

            @NotNull
            @Override
            public Iterator<GernNode> iterator() {
                return res.iterator();
            }

            @Override
            public GenerateResult generateCode(final GenerateC aGgc, final WorkManager aWm) {
                return generateCode1(res, aGeneratedNodes, aWm);
            }
        };
    }

    @Contract(value = "_ -> new", pure = true)
    private @NotNull GernNode gern(final GeneratedNode aGeneratedNode) {
        return new GernNodeFunction(aGeneratedNode);
    }

    private void generateCodeForMethod(final @NotNull BaseGeneratedFunction gf,
                                       final GenerateResult gr,
                                       final WorkList aWorkList) {
        final GernNode gern = gern(gf);
        if (gern instanceof GernNodeFunction) {
            generateCodeForMethod(gern, gr, aWorkList);
        } else {
            throw new AssertionError();
        }
    }

    private void generateCodeForMethod(final @NotNull GernNode gn,
                                       final GenerateResult gr,
                                       final WorkList aWorkList) {
        BaseGeneratedFunction gf = (BaseGeneratedFunction) gn.carrier();
        gn.generateCodeForMethod(gf, gr, aWorkList, this, LOG);
    }

    private void generateCodeForConstructor(final GeneratedConstructor gf, final GenerateResult gr,
                                            final WorkList aWorkList) {
        if (gf.getFD() == null)
            return;
        final Generate_Code_For_Method gcfm = new Generate_Code_For_Method(this, LOG);
        gcfm.generateCodeForConstructor(gf, gr, aWorkList);
    }

    String getTypeNameForGenClass(@NotNull final GeneratedNode aGenClass) {
        return GetTypeName.getTypeNameForGenClass(aGenClass);
    }

    String getTypeNameForVariableEntry(@NotNull final VariableTableEntry input) {
        return GetTypeName.forVTE(input);
    }

    String getTypeName(@NotNull final TypeTableEntry tte) {
        return GetTypeName.forTypeTableEntry(tte);
    }

    @Override
    public void generate_class(final GeneratedClass x, final GenerateResult gr) {
        if (x.generatedAlready)
            return;
        switch (x.getKlass().getType()) {
            // Don't generate class definition for these three
            case INTERFACE:
            case SIGNATURE:
            case ABSTRACT:
                return;
        }
        final CClassDecl decl = new CClassDecl(x);
        decl.evaluatePrimitive();
        final BufferTabbedOutputStream tosHdr = new BufferTabbedOutputStream();
        final BufferTabbedOutputStream tos = new BufferTabbedOutputStream();
        try {
            tosHdr.put_string_ln("typedef struct {");
            tosHdr.incr_tabs();
            tosHdr.put_string_ln("int _tag;");
            if (!decl.prim) {
                for (final GeneratedClass.VarTableEntry o : x.varTable) {
                    final String typeName = getTypeNameGNCForVarTableEntry(o);
                    tosHdr.put_string_ln(String.format("%s vm%s;", typeName, o.nameToken));
                }
            } else {
                tosHdr.put_string_ln(String.format("%s vsv;", decl.prim_decl));
            }

            final String class_name = getTypeName(x);
            final int class_code = x.getCode();

            tosHdr.dec_tabs();
            tosHdr.put_string_ln("");
//			tosHdr.put_string_ln(String.format("} %s;", class_name));
            tosHdr.put_string_ln(
                    String.format("} %s;  // class %s%s", class_name, decl.prim ? "box " : "", x.getName()));

            tosHdr.put_string_ln("");
            tosHdr.put_string_ln("");

            // TODO remove this block when constructors are added in dependent functions,
            // etc in Deduce
            {
                // TODO what about named constructors and ctor$0 and "the debug stack"
                tos.put_string_ln(String.format("%s* ZC%d() {", class_name, class_code));
                tos.incr_tabs();
                tos.put_string_ln(String.format("%s* R = GC_malloc(sizeof(%s));", class_name, class_name));
                tos.put_string_ln(String.format("R->_tag = %d;", class_code));
                if (decl.prim) {
                    // TODO consider NULL, and floats and longs, etc
                    if (!decl.prim_decl.equals("bool"))
                        tos.put_string_ln("R->vsv = 0;");
                    else if (decl.prim_decl.equals("bool"))
                        tos.put_string_ln("R->vsv = false;");
                } else {
                    for (final GeneratedClass.VarTableEntry o : x.varTable) {
//					final String typeName = getTypeNameForVarTableEntry(o);
                        // TODO this should be the result of getDefaultValue for each type
                        tos.put_string_ln(String.format("R->vm%s = 0;", o.nameToken));
                    }
                }
                tos.put_string_ln("return R;");
                tos.dec_tabs();
                tos.put_string_ln(String.format("} // class %s%s", decl.prim ? "box " : "", x.getName()));
                tos.put_string_ln("");
            }
            tos.flush();
        } finally {
            tos.close();
            tosHdr.close();
            final Buffer buf = tos.getBuffer();
//			LOG.info(buf.getText());
            gr.addClass(GenerateResult.TY.IMPL, x, buf, x.module().getLsp());
            final Buffer buf2 = tosHdr.getBuffer();
//			LOG.info(buf2.getText());
            gr.addClass(GenerateResult.TY.HEADER, x, buf2, x.module().getLsp());
        }
        x.generatedAlready = true;
    }

    String getRealTargetName(final @NotNull BaseGeneratedFunction gf, final @NotNull IntegerIA target,
                             final Generate_Code_For_Method.AOG aog) {
        final VariableTableEntry varTableEntry = gf.getVarTableEntry(target.getIndex());
        return getRealTargetName(gf, varTableEntry);
    }

    @Deprecated
    String getTypeName(final @NotNull OS_Type ty) {
        return GetTypeName.forOSType(ty, LOG);
    }

    @NotNull
    List<String> getArgumentStrings(final BaseGeneratedFunction gf, final Instruction instruction) {
        final List<String> sl3 = new ArrayList<String>();
        final int args_size = instruction.getArgsSize();
        for (int i = 1; i < args_size; i++) {
            final InstructionArgument ia = instruction.getArg(i);
            if (ia instanceof IntegerIA) {
//				VariableTableEntry vte = gf.getVarTableEntry(DeduceTypes2.to_int(ia));
                final String realTargetName = getRealTargetName(gf, (IntegerIA) ia, Generate_Code_For_Method.AOG.GET);
                sl3.add(Emit.emit("/*669*/") + realTargetName);
            } else if (ia instanceof IdentIA) {
                final CReference reference = new CReference();
                reference.getIdentIAPath((IdentIA) ia, Generate_Code_For_Method.AOG.GET, null);
                final String text = reference.build();
                sl3.add(Emit.emit("/*673*/") + text);
            } else if (ia instanceof final ConstTableIA c) {
                final ConstantTableEntry cte = gf.getConstTableEntry(c.getIndex());
                final String s = GetAssignmentValue.const_to_string(cte.initialValue);
                sl3.add(s);
                final int y = 2;
            } else if (ia instanceof ProcIA) {
                LOG.err("740 ProcIA");
                throw new NotImplementedException();
            } else {
                LOG.err(ia.getClass().getName());
                throw new IllegalStateException("Invalid InstructionArgument");
            }
        }
        return sl3;
    }

    @Deprecated
    String getTypeName(final @NotNull TypeName typeName) {
        return GetTypeName.forTypeName(typeName, errSink);
    }

    String getAssignmentValue(final VariableTableEntry aSelf, final Instruction aInstruction,
                              final ClassInvocation aClsinv, final BaseGeneratedFunction gf) {
        final GetAssignmentValue gav = new GetAssignmentValue();
        return gav.forClassInvocation(aInstruction, aClsinv, gf, LOG);
    }

    @NotNull
    String getAssignmentValue(final VariableTableEntry value_of_this, final InstructionArgument value,
                              final BaseGeneratedFunction gf) {
        final GetAssignmentValue gav = new GetAssignmentValue();
        if (value instanceof final FnCallArgs fca) {
            return gav.FnCallArgs(fca, gf, LOG);
        }

        if (value instanceof @NotNull final ConstTableIA constTableIA) {
            return gav.ConstTableIA(constTableIA, gf);
        }

        if (value instanceof final IntegerIA integerIA) {
            return gav.IntegerIA(integerIA, gf);
        }

        if (value instanceof final IdentIA identIA) {
            return gav.IdentIA(identIA, gf);
        }

        LOG.err(String.format("783 %s %s", value.getClass().getName(), value));
        return String.valueOf(value);
    }

    @NotNull
    List<String> getArgumentStrings(final @NotNull Supplier<IFixedList<InstructionArgument>> instructionSupplier) {
        final @NotNull List<String> sl3 = new ArrayList<String>();
        final int args_size = instructionSupplier.get().size();
        for (int i = 1; i < args_size; i++) {
            final InstructionArgument ia = instructionSupplier.get().get(i);
            if (ia instanceof IntegerIA) {
//				VariableTableEntry vte = gf.getVarTableEntry(DeduceTypes2.to_int(ia));
                final String realTargetName = getRealTargetName((IntegerIA) ia, Generate_Code_For_Method.AOG.GET);
                sl3.add(Emit.emit("/*669*/") + realTargetName);
            } else if (ia instanceof IdentIA) {
                final CReference reference = new CReference();
                reference.getIdentIAPath((IdentIA) ia, Generate_Code_For_Method.AOG.GET, null);
                final String text = reference.build();
                sl3.add(Emit.emit("/*673*/") + text);
            } else if (ia instanceof final ConstTableIA c) {
                final ConstantTableEntry cte = c.getEntry();
                final String s = GetAssignmentValue.const_to_string(cte.initialValue);
                sl3.add(s);
                final int y = 2;
            } else if (ia instanceof ProcIA) {
                LOG.err("740 ProcIA");
                throw new NotImplementedException();
            } else {
                LOG.err(ia.getClass().getName());
                throw new IllegalStateException("Invalid InstructionArgument");
            }
        }
        return sl3;
    }

    String getRealTargetName(final @NotNull IntegerIA target, final Generate_Code_For_Method.AOG aog) {
        final BaseGeneratedFunction gf = target.gf;
        final VariableTableEntry varTableEntry = gf.getVarTableEntry(target.getIndex());
        return getRealTargetName(gf, varTableEntry);
    }

    String getRealTargetName(final @NotNull BaseGeneratedFunction gf, final @NotNull IdentIA target,
                             final Generate_Code_For_Method.AOG aog, final String value) {
        int state = 0, code = -1;
        final IdentTableEntry identTableEntry = gf.getIdentTableEntry(target.getIndex());
        final LinkedList<String> ls = new LinkedList<String>();
        // TODO in Deduce set property lookupType to denote what type of lookup it is:
        // MEMBER, LOCAL, or CLOSURE
        InstructionArgument backlink = identTableEntry.getBacklink();
        final String text = identTableEntry.getIdent().getText();
        if (backlink == null) {
            if (identTableEntry.getResolvedElement() instanceof final VariableStatement vs) {
                final OS_Element parent = vs.getParent().getParent();
                if (parent != gf.getFD()) {
                    // we want identTableEntry.resolved which will be a GeneratedMember
                    // which will have a container which will be either be a function,
                    // statement (semantic block, loop, match, etc) or a GeneratedContainerNC
                    final int y = 2;
                    final GeneratedNode er = identTableEntry.externalRef;
                    if (er instanceof final GeneratedContainerNC nc) {
                        assert nc instanceof GeneratedNamespace;
                        final GeneratedNamespace ns = (GeneratedNamespace) nc;
//						if (ns.isInstance()) {}
                        state = 1;
                        code = nc.getCode();
                    }
                }
            }
            switch (state) {
                case 0:
                    ls.add(Emit.emit("/*912*/") + "vsc->vm" + text); // TODO blindly adding "vm" might not always work, also
                    // put in loop
                    break;
                case 1:
                    ls.add(Emit.emit("/*845*/") + String.format("zNZ%d_instance->vm%s", code, text));
                    break;
                default:
                    throw new IllegalStateException("Can't be here");
            }
        } else
            ls.add(Emit.emit("/*872*/") + "vm" + text); // TODO blindly adding "vm" might not always work, also put in
        // loop
        while (backlink != null) {
            if (backlink instanceof final IntegerIA integerIA) {
                final String realTargetName = getRealTargetName(gf, integerIA, Generate_Code_For_Method.AOG.ASSIGN);
                ls.addFirst(Emit.emit("/*892*/") + realTargetName);
                backlink = null;
            } else if (backlink instanceof final IdentIA identIA) {
                final int identIAIndex = identIA.getIndex();
                final IdentTableEntry identTableEntry1 = gf.getIdentTableEntry(identIAIndex);
                final String identTableEntryName = identTableEntry1.getIdent().getText();
                ls.addFirst(Emit.emit("/*885*/") + "vm" + identTableEntryName); // TODO blindly adding "vm" might not
                // always be right
                backlink = identTableEntry1.getBacklink();
            } else
                throw new IllegalStateException("Invalid InstructionArgument for backlink");
        }
        final CReference reference = new CReference();
        reference.getIdentIAPath(target, aog, value);
        final String path = reference.build();
        LOG.info("932 " + path);
        final String s = Helpers.String_join("->", ls);
        LOG.info("933 " + s);
        if (identTableEntry.getResolvedElement() instanceof ConstructorDef
                || identTableEntry.getResolvedElement() instanceof PropertyStatement || value != null) {
            return path;
        } else {
            return s;
        }
    }

    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    public static class CALL_stmt implements EG_Statement {
        private final StringBuilder sb = new StringBuilder();
        private GetAssignmentValue gav;
        private BaseGeneratedFunction gf;
        private ElLog LOG;
        private ProcTableEntry pte;
        private Instruction inst;
        private CReference reference;

        @Override
        public String getText() {
            return sb.toString();
        }

        @Override
        public EX_Explanation getExplanation() {
            return EX_Explanation.withMessage("_CALL_stmt");
        }

        public void set(final GetAssignmentValue aGav,
                        final BaseGeneratedFunction aGf,
                        final ElLog aLOG,
                        final ProcTableEntry aPte,
                        final Instruction aInst) {
            gav = aGav;
            gf = aGf;
            LOG = aLOG;
            pte = aPte;
            inst = aInst;

            if (pte.expression_num == null) {
                calc_expression();
            } else {
                calc_expression_num();
            }
        }

        public @Nullable CReference getReference() {
            return reference;
        }

        private void calc_expression_num() {
            final IdentIA ia2 = (IdentIA) pte.expression_num; // this is a name
            final IdentTableEntry idte = ia2.getEntry();
            if (idte.getStatus() == BaseTableEntry.Status.KNOWN) {
                reference = new CReference();
                final FunctionInvocation functionInvocation = pte.getFunctionInvocation();
                if (functionInvocation == null
                        || functionInvocation.getFunction() == ConstructorDef.defaultVirtualCtor) {
                    reference.getIdentIAPath(ia2, Generate_Code_For_Method.AOG.GET, null);
                    final List<String> sll = gav.getAssignmentValueArgs(inst, gf, LOG);
                    reference.args(sll);
                    final String path = reference.build();
                    emit(("/*829*/"), path);
                } else {
                    final BaseGeneratedFunction pte_generated = functionInvocation.getGenerated();
                    if (idte.resolvedType() == null && pte_generated != null)
                        idte.resolveTypeToClass(pte_generated);
                    reference.getIdentIAPath(ia2, Generate_Code_For_Method.AOG.GET, null);
                    final List<String> sll = gav.getAssignmentValueArgs(inst, gf, LOG);
                    reference.args(sll);
                    final String path = reference.build();
                    emit(("/*827*/"), path);
                }
            } else {
                final String path = gf.getIdentIAPathNormal(ia2);
                emit(("/*828*/"), String.format("%s is UNKNOWN", path));
            }
        }

        private void emit(final String one, final String two) {
            sb.append(Emit.emit(one) + two);
        }

        private void calc_expression() {
            if (true) {
                final IdentExpression ptex = (IdentExpression) pte.expression;
                sb.append(ptex.getText());
                emit(("/*671*/"), "(");

                final List<String> sll = gav.getAssignmentValueArgs(inst, gf, LOG);
                sb.append(Helpers.String_join(", ", sll));

                sb.append(")");
            } else {
                throw new AssertionError(); // TODO synthetic methods
            }
        }

        public EG_Statement getStatement() {
            return this;
        }
    }

}

//
//
//
