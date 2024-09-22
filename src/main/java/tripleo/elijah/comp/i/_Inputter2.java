package tripleo.elijah.comp.i;

import java.io.File;
import java.util.function.Consumer;

public interface _Inputter2<T> {
    void acceptFile(File aF);

    <Z, U> void apply(Consumer<Z> tt, Consumer<U> uu);
}
