package tripleo.elijah.stages.deduce;

import tripleo.elijah.*;

public class DT2_Worker {
	private final Eventual<DeduceTypes2> trigger = new Eventual<>();

	public void addWork(DT2_Work job) {
		trigger.then(result -> job.run(result, DT2_Worker.this));
	}

	public void asvErr(final int aI, final boolean aB) {
		trigger.then(d -> d.asvErr(aI, aB));
	}

	public interface DT2_Work {
		void run(DeduceTypes2 dt2, DT2_Worker worker);
	}
}
