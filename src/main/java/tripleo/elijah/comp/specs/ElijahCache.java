package tripleo.elijah.comp.specs;

import tripleo.elijah.lang.OS_Module;

import java.util.Optional;

public interface ElijahCache {
    void put(ElijahSpec aSpec, String aAbsolutePath, OS_Module aModule);

    Optional<OS_Module> get(String aAbsolutePath);
}
