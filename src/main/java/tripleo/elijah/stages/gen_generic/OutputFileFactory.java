package tripleo.elijah.stages.gen_generic;

import org.jetbrains.annotations.*;
import tripleo.elijah.stages.gen_c.*;
import tripleo.elijah.util.*;

import java.util.*;

public final class OutputFileFactory {
	private OutputFileFactory() {
	}

	@Contract("_, _ -> new")
	public static @NotNull GenerateFiles create(final @NotNull String lang,
	                                            final @NotNull OutputFileFactoryParams params) {
		if (Objects.equals(lang, "c")) {
			return new GenerateC(params);
		} else
			throw new NotImplementedException();
	}
}
