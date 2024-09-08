package tripleo.elijah.comp;

import java.util.function.Consumer;

// TODO m/ ...
public interface ElValue<T> {
	boolean isSet();
	boolean isClosed();

	T value();

	void whenSet(Consumer<T> consumer);
}
