package tripleo.elijah.stages.gen_c;

import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedConstructor;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.work.WorkList;
import tripleo.elijah_fluffy.util.SimplePrintLoggerToRemoveSoon;

class GernNodeConstructor extends AbstractGernNode {
	public GernNodeConstructor(final GeneratedNode aGeneratedNode) {
		setNode(aGeneratedNode);
	}

	public GeneratedConstructor asConstructor() {
		return (GeneratedConstructor) generatedNode;
	}

	@Override
	public boolean isAttached() {
		return generatedNode instanceof GeneratedConstructor;
	}

	@Override
	public void generateCodeForMethod(final BaseGeneratedFunction gf0,
									  final GenerateResult gr,
									  final WorkList aWorkList,
									  final GenerateC aGenerateC,
									  final ElLog LOG) {
		if (gf0 instanceof GeneratedFunction) {
			LOG.err("Should not pass Function to GernNodeConstructor::generateCodeForMethod " + gf0.identityString());
			return;
		}

		if (!this.isAttached()) {
			SimplePrintLoggerToRemoveSoon.println_out("** Skipping generateCodeForMethod for " + this.identityString());
			return;
		}

		final Generate_Code_For_Method gcfm = new Generate_Code_For_Method(aGenerateC, LOG);
		gcfm.generateCodeForConstructor(this, gr, aWorkList);
	}
}
