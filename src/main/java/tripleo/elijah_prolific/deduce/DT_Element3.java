package tripleo.elijah_prolific.deduce;

import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;

public interface DT_Element3 {
	OS_Element getResolvedElement();

	void setErrSink(ErrSink aErrSink);

	void setGeneratedFunction(BaseGeneratedFunction aGeneratedFunction);

	void setContext(Context aContext);

	void setDeduceTypes2(DeduceTypes2 aDeduceTypes2);

	void op_fail(DTEL aDTEL);

	ErrSink getErrSink();

	public enum DTEL {
		d999_163
	}
}
