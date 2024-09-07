package tripleo.elijah.comp;

import tripleo.elijah.comp.i.CompilerController;

import java.util.List;

public class DefaultCompilerController extends _CompilerControllerBase implements CompilerController {
	@Override
	public void setInputs(Compilation aCompilation, List<String> aArgumentList) {
		c = aCompilation;
		args = aArgumentList;
	}
}
