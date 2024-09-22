package tripleo.elijah_remnant.value;

import java.util.function.Consumer;

// TODO m/ ...
public interface ElValue<T> {
    boolean isSet();

    boolean isClosed();

    T value();

    void whenSet(Consumer<T> consumer);

    void set(T t);
}
