package tripleo.elijah.comp;

import java.util.*;

public interface CompilerController {
	void printUsage();

	void processOptions();

	void runner();

	void _set(Compilation aCompilation, List<String> aArgs);
}
