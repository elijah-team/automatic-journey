package tripleo.elijah.nextgen.outputstatement;

import java.util.*;

public class EG_SequenceFactory {

	public static class _SequenceBuilder {
		private List<EG_Statement> statementList;
		private EG_Statement       result;

		public _SequenceBuilder addParts(final List<EG_Statement> aStatementList) {
			if (result != null) throw new AssertionError();
			statementList = aStatementList;
			return this;
		}

		public EG_Statement build() {
			if (result != null) throw new AssertionError();
			result = new EG_SequenceStatement("", "", statementList);
			return result;
		}
	}

	public static _SequenceBuilder newSequence() {
		return new _SequenceBuilder();
	}
}
