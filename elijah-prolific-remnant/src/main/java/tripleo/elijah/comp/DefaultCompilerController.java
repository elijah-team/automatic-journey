package tripleo.elijah.comp;

import java.util.List;

public class DefaultCompilerController extends _CompilerControllerBase
		implements tripleo.elijah.comp.i.CompilerController {
	@Override
	public void setInputs(Compilation aCompilation, List<String> aArgumentList) {
		c = aCompilation;
		args = aArgumentList;
	}
}
