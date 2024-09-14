package tripleo.elijah_fluffy.util;

public class Ok {
    private static final Ok INSTANCE = new Ok();

    private Ok() {
    }

    public static Ok instance() {
        return INSTANCE;
    }
}
