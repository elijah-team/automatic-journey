package tripleo.elijah.comp;

import java.util.*;

public class DefaultCompilerController extends _CompilerControllerBase implements CompilerController {
	@Override
	public void _set(Compilation aCompilation, List<String> aArgumentList) {
		c    = aCompilation;
		args = aArgumentList;
	}
}
