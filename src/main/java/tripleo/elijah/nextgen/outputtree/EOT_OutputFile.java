package tripleo.elijah.nextgen.outputtree;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.nextgen.inputtree.EIT_Input;
import tripleo.elijah.nextgen.inputtree.EIT_ModuleInput;
import tripleo.elijah.nextgen.outputstatement.*;
import tripleo.util.buffer.Buffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static tripleo.elijah_fluffy.util.Helpers.List_of;

public class EOT_OutputFile {
    // private final OS_Module module;
    private final Compilation c;

    private final List<EIT_Input> _inputs = new ArrayList<>();
    private final String _filename;
    private final EOT_OutputType _type;
    private final EG_Statement _sequence; // TODO List<?> ??

    public EOT_OutputFile(
            final Compilation c,
            final @NotNull List<EIT_Input> inputs,
            final String filename,
            final EOT_OutputType type,
            final EG_Statement sequence) {
        this.c = c;
        _filename = filename;
        _type = type;
        _sequence = sequence;
        _inputs.addAll(inputs);
    }

    public static @NotNull EOT_OutputFile bufferSetToOutputFile(
            final String aFilename,
            final @NotNull Collection<Buffer> aBuffers,
            final Compilation comp,
            final OS_Module aModule) {
        final List<EIT_Input> inputs = List_of(new EIT_ModuleInput(aModule, comp));

        final List<EG_Statement> statementStream = aBuffers.stream()
                .map(buffer ->
                        new EG_SingleStatement(buffer.getText(), EX_Explanation.withMessage("bufferSetToOutputFile")))
                .collect(Collectors.toList());
        final EG_SequenceStatement seq = new EG_SequenceStatement(new EG_Naming("yyy"), statementStream);

        final EOT_OutputFile eof = new EOT_OutputFile(comp, inputs, aFilename, EOT_OutputType.SOURCES, seq);

        comp.reports().addCodeOutput(() -> aFilename, eof);

        return eof;
    }

    public String getFilename() {
        return _filename;
    }

    public EOT_OutputType getType() {
        return _type;
    }

    public EG_Statement getStatementSequence() {
        return _sequence;
    }

    public List<EIT_Input> getInputs() {
        return _inputs;
    }

    // rules/constraints whatever

    @FunctionalInterface
    public interface FileNameProvider {
        String getFilename();
    }
}
