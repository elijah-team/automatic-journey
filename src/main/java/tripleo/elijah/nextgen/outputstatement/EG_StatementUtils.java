package tripleo.elijah.nextgen.outputstatement;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public enum EG_StatementUtils {
    ;

    public static @NotNull List<EG_Statement> mapStringListToSingleStatementList(
            final @NotNull List<@NotNull String> aStringList) {
        return aStringList.stream().map(EG_SingleStatement::new).collect(Collectors.toList());
    }
}
