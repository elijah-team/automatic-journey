package tripleo.elijah.comp;

import tripleo.elijah.comp.i.*;

import java.util.*;

public class DefaultCompilerController extends _CompilerControllerBase
		implements tripleo.elijah.comp.i.CompilerController {
	@Override
	public void _set(Compilation aCompilation, List<String> aArgumentList) {
		c = aCompilation;
		args = aArgumentList;
	}
}
