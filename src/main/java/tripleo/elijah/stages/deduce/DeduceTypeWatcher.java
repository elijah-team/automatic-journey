package tripleo.elijah.stages.deduce;

import org.jdeferred2.*;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.gen_fn.*;

public class DeduceTypeWatcher {
	private DoneCallback<GenType> typeCallback;
	private OS_Element            _element;

	public void onType(final DoneCallback<GenType> aTypeCallback) {
		typeCallback = aTypeCallback;
	}

	public void setType(final GenType gt) {
		typeCallback.onDone(gt);
	}

	public OS_Element element() {
		return _element;
	}

	public void element(final OS_Element e) {
		_element = e;
	}
}
