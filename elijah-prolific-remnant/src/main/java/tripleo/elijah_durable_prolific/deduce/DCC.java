package tripleo.elijah_durable_prolific.deduce;

import tripleo.elijah.comp.*;
import tripleo.elijah.stages.deduce.*;

public record DCC(
		DeduceTypes2 deduceTypes2,
		DeducePhase deducePhase,
		ErrSink errSink
) {
	public DeducePhase phase() {
		return this.deducePhase;
	}
}
