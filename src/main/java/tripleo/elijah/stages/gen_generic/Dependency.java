/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_generic;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.stages.deduce.FunctionInvocation;
import tripleo.elijah.stages.gen_fn.AbstractDependencyTracker;
import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;
import tripleo.elijah.stages.gen_fn.GenType;
import tripleo.elijah.stages.gen_fn.GeneratedContainerNC;
import tripleo.elijah_fluffy.util.SimplePrintLoggerToRemoveSoon;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created 9/13/21 4:00 AM
 */
public class Dependency {
    public final IDependencyReferent referent;
    public final Set<Dependency> deps = new HashSet<>();

    public DependencyRef dref;
    public OS_Element resolved;

    public Dependency(final IDependencyReferent aReferent) {
        referent = aReferent;
    }

    public DependencyRef getRef() {
        return dref;
    }

    public void setRef(final DependencyRef aDref) {
        dref = aDref;
    }

    public void noteDependencies(
            final AbstractDependencyTracker aDependencyTracker,
            final List<FunctionInvocation> aDependentFunctions,
            final List<GenType> aDependentTypes) {
        for (final FunctionInvocation dependentFunction : aDependentFunctions) {
            final BaseGeneratedFunction generatedFunction = dependentFunction.getGenerated();
            if (generatedFunction != null) deps.add(generatedFunction.getDependency());
            else SimplePrintLoggerToRemoveSoon.println_err2("52 false FunctionInvocation " + dependentFunction);
        }
        for (final GenType dependentType : aDependentTypes) {
            final GeneratedContainerNC node = (GeneratedContainerNC) dependentType.getNode();
            if (node != null) deps.add(node.getDependency());
            else {
                SimplePrintLoggerToRemoveSoon.println_err2("46 node is null "
                        + (dependentType.getResolved() != null
                                ? dependentType.getResolved()
                                : dependentType.getResolvedn()));
                final Dependency d = new Dependency(null);
                d.resolved = dependentType.getResolved() != null
                        ? dependentType.getResolved().getClassOf()
                        : dependentType.getResolvedn();
                deps.add(d);
            }
        }
    }

    public @NotNull Set<Dependency> getNotedDeps() {
        return deps;
    }
}

//
//
//
