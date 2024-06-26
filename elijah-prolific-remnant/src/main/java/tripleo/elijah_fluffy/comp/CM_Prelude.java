package tripleo.elijah_fluffy.comp;

import tripleo.elijah.ci.*;
import tripleo.elijah.comp.*;
import tripleo.elijah.lang.*;

public interface CM_Prelude {
	OS_Module getModule();
	CM_Preludes getTag();
	LibraryStatementPart getLsp();
	Compilation getCompilation();
	// ??
}
