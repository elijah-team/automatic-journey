package tripleo.elijah_durable_prolific.deduce;

import tripleo.elijah.stages.deduce.*;

public record DCC(
		DeduceTypes2 deduceTypes2,
		DeducePhase deducePhase
) {
	public DeducePhase phase() {
		return this.deducePhase;
	}
}
