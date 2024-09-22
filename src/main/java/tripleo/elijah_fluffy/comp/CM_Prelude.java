package tripleo.elijah_fluffy.comp;

import tripleo.elijah.ci.LibraryStatementPart;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.lang.OS_Module;

public interface CM_Prelude {
    OS_Module getModule();

    CM_Preludes getTag();

    LibraryStatementPart getLsp();

    Compilation getCompilation();
    // ??
}
