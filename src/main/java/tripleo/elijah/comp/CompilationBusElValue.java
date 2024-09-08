package tripleo.elijah.comp;

import tripleo.elijah.comp.internal.CompilationBus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class CompilationBusElValue implements ElValue<CompilationBus>, ElValue_SettableA<CompilationBus> {
	private boolean                        isSet;
	private       CompilationBus                 value;
	private final List<Consumer<CompilationBus>> consumers = new ArrayList<>();

	@Override
	public boolean isSet() {
		return this.isSet;
	}

	@Override
	public boolean isClosed() {
		return false;
	}

	@Override
	public CompilationBus value() {
		//noinspection RedundantIfStatement
		if (false) {
			assert isSet();
		}
		return null;
	}

	@Override
	public void whenSet(Consumer<CompilationBus> consumer) {
		this.consumers.add(consumer);
	}

	@Override
	public void set(CompilationBus compilationBus) {
		this.isSet = true;
		this.value = compilationBus;

		for (final Consumer<CompilationBus> consumer : this.consumers) {
			consumer.accept(this.value);
		}
	}
}
