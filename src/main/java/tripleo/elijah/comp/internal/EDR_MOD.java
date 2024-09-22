package tripleo.elijah.comp.internal;

import tripleo.elijah.lang.OS_Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EDR_MOD {
    public final List<OS_Module> modules = new ArrayList<OS_Module>();
    private final Map<String, OS_Module> fn2m = new HashMap<String, OS_Module>();

    public void addModule(final OS_Module module, final String fn) {
        modules.add(module);
        fn2m.put(fn, module);
    }

    public int size() {
        return modules.size();
    }

    public List<OS_Module> modules() {
        return modules;
    }

    public boolean containsKey(final String aFileName) {
        return fn2m.containsKey(aFileName);
    }

    public OS_Module get(final String aFileName) {
        return fn2m.get(aFileName);
    }
}
