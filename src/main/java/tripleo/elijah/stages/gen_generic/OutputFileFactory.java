package tripleo.elijah.stages.gen_generic;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah_fluffy.comp.CM_Preludes;

public final class OutputFileFactory {
    private OutputFileFactory() {}

    @Contract("_, _ -> new")
    public static @Nullable GenerateFiles create(
            final @NotNull String lang, final @NotNull OutputFileFactoryParams params) {
        final CM_Preludes._Creator dispatch = CM_Preludes.dispatch(lang);
        if (dispatch == null) return null;
        return dispatch.create(params);
    }
}
