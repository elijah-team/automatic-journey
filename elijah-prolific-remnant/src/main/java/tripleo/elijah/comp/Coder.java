package tripleo.elijah.comp;

import org.jetbrains.annotations.*;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.gen_generic.*;

import java.util.*;
import java.util.stream.*;

public class Coder {
	private final ICodeRegistrar codeRegistrar;

	public Coder(final ICodeRegistrar aCodeRegistrar) {
		codeRegistrar = aCodeRegistrar;
	}

	private static void extractNodes_toResolvedNodes(@NotNull final Map<FunctionDef, GeneratedFunction> aFunctionMap, @NotNull final List<GeneratedNode> resolved_nodes) {
		aFunctionMap.values().stream().map(generatedFunction -> (generatedFunction.idte_list)
		              .stream()
		              .filter(IdentTableEntry::isResolved)
		              .map(IdentTableEntry::resolvedType)
		              .collect(Collectors.toList()))
		            .forEach(resolved_nodes::addAll);
	}

	public void codeNodes(final OS_Module mod, final List<GeneratedNode> resolved_nodes, final GeneratedNode generatedNode) {
		if (generatedNode instanceof final GeneratedFunction generatedFunction) {
			codeNodeFunction(generatedFunction, mod);
		} else if (generatedNode instanceof final GeneratedClass generatedClass) {
			//			assert generatedClass.getCode() == 0;
			if (generatedClass.getCode() == 0)
				codeNodeClass(generatedClass, mod);

			setClassmapNodeCodes(generatedClass.classMap, mod);

			extractNodes_toResolvedNodes(generatedClass.functionMap, resolved_nodes);
		} else if (generatedNode instanceof final GeneratedNamespace generatedNamespace) {

			if (generatedNamespace.getCode() != 0)
				codeNodeNamespace(generatedNamespace, mod);

			setClassmapNodeCodes(generatedNamespace.classMap, mod);

			extractNodes_toResolvedNodes(generatedNamespace.functionMap, resolved_nodes);
		}
	}

	public void codeNodeFunction(@NotNull final BaseGeneratedFunction generatedFunction, final OS_Module mod) {
//		if (generatedFunction.getCode() == 0)
//			generatedFunction.setCode(mod.parent.nextFunctionCode());
		codeRegistrar.registerFunction(generatedFunction);
	}

	private void setClassmapNodeCodes(@NotNull final Map<ClassStatement, GeneratedClass> aClassMap, final OS_Module mod) {
		aClassMap.values().forEach(generatedClass -> codeNodeClass(generatedClass, mod));
	}

	public void codeNodeClass(@NotNull final GeneratedClass generatedClass, final OS_Module mod) {
//		if (generatedClass.getCode() == 0)
//			generatedClass.setCode(mod.parent.nextClassCode());
		codeRegistrar.registerClass(generatedClass);
	}

	public void codeNodeNamespace(@NotNull final GeneratedNamespace generatedNamespace, final OS_Module mod) {
//		if (generatedNamespace.getCode() == 0)
//			generatedNamespace.setCode(mod.parent.nextClassCode());
		codeRegistrar.registerNamespace(generatedNamespace);
	}

	public void codeNode(final GeneratedNode generatedNode, final OS_Module mod) {
		final Coder coder = this;

		if (generatedNode instanceof final GeneratedFunction generatedFunction) {
			coder.codeNodeFunction(generatedFunction, mod);
		} else if (generatedNode instanceof final GeneratedClass generatedClass) {
			coder.codeNodeClass(generatedClass, mod);
		} else if (generatedNode instanceof final GeneratedNamespace generatedNamespace) {
			coder.codeNodeNamespace(generatedNamespace, mod);
		}
	}
}
