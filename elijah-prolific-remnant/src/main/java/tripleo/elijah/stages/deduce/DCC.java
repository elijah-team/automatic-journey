package tripleo.elijah.stages.deduce;

import tripleo.elijah.comp.ErrSink;

public record DCC(
		DeduceTypes2 deduceTypes2,
		DeducePhase phase,
		ErrSink errSink
) { }
