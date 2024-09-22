package tripleo.elijah.comp;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah_fluffy.comp.CM_Preludes;

public class CompilationAlways {
    public static boolean VOODOO = false;
    public static CM_Preludes _defaultPrelude = CM_Preludes.C;

    /**
     * Return the unquoted name
     */
    @NotNull
    public static String defaultPrelude() {
        return _defaultPrelude.getName();
    }
}
