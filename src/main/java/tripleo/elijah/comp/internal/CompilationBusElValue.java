package tripleo.elijah.comp.internal;

import tripleo.elijah_remnant.value.ElValue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class CompilationBusElValue implements ElValue<EDR_CompilationBus> {
	private       boolean                            isSet;
	private       EDR_CompilationBus                 value;
	private final List<Consumer<EDR_CompilationBus>> consumers = new ArrayList<>();

	@Override
	public boolean isSet() {
		return this.isSet;
	}

	@Override
	public boolean isClosed() {
		return false;
	}

	@Override
	public EDR_CompilationBus value() {
		//noinspection RedundantIfStatement
		if (false) {
			assert isSet();
		}
		return null;
	}

	@Override
	public void whenSet(Consumer<EDR_CompilationBus> consumer) {
		this.consumers.add(consumer);
	}

	@Override
	public void set(EDR_CompilationBus compilationBus) {
		this.isSet = true;
		this.value = compilationBus;

		for (final Consumer<EDR_CompilationBus> consumer : this.consumers) {
			consumer.accept(this.value);
		}
	}
}
