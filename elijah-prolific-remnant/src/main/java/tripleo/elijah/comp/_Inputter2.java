package tripleo.elijah.comp;

import java.io.*;
import java.util.function.*;

public interface _Inputter2<T> {
	void acceptFile(File aF);

	<Z, U> void apply(Consumer<Z> tt, Consumer<U> uu);
}
