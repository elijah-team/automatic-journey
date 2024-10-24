/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.contexts.ClassContext;
import tripleo.elijah.lang.types.OS_UserClassType;
import tripleo.elijah.lang2.ElElementVisitor;
import tripleo.elijah_fluffy.util.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a "class"
 * <p>
 * items -> ClassItems docstrings variables
 */
public class ClassStatement extends _CommonNC /* ProgramClosure */
        implements ClassItem,
                ModuleItem,
                StatementItem,
                FunctionItem,
                OS_Element,
                OS_Element2,
                Documentable,
                OS_Container {

    static final List<TypeName> emptyTypeNameList = ImmutableList.of();
    private final OS_Element parent;
    ClassInheritance _inh = new ClassInheritance(); // remove final for ClassBuilder
    private ClassTypes _type;
    private TypeNameList genericPart;
    private OS_UserClassType osType;

    public ClassStatement(final OS_Element parentElement, final Context parentContext) {
        parent = parentElement; // setParent

        @NotNull final ElObjectType x = DecideElObjectType.getElObjectType(parentElement);
        switch (x) {
            case MODULE:
                final OS_Module module = (OS_Module) parentElement;
                //
                this.setPackageName(module.pullPackageName());
                _packageName.addElement(this);
                module.add(this);
                break;
            case FUNCTION:
                // do nothing
                break;
            default:
                // we kind of fail the switch test here because OS_Container is not an
                // OS_Element,
                // so we have to test explicitly, messing up the pretty flow we had.
                // hey sh*t happens.
                if (parentElement instanceof OS_Container) {
                    ((OS_Container) parentElement).add(this);
                } else {
                    throw new IllegalStateException(String.format("Cant add ClassStatement to %s", parentElement));
                }
        }

        setContext(new ClassContext(parentContext, this));
    }

    @Override
    public void visitGen(final ElElementVisitor visit) {
        visit.addClass(this); // TODO visitClass
    }

    @Override // OS_Element
    public ClassContext getContext() {
        return (ClassContext) _a._context;
    }

    public void setContext(final ClassContext ctx) {
        _a.setContext(ctx);
    }

    @Override
    public OS_Element getParent() {
        return parent;
    }

    @Override
    public String toString() {
        final String package_name;
        if (getPackageName() != null && getPackageName()._name != null) {
            final Qualident package_name_q = getPackageName()._name;
            package_name = package_name_q.toString();
        } else package_name = "`'";
        return String.format("<Class %d %s %s>", _a.getCode(), package_name, getName());
    }

    public ConstructorDef addCtor(final IdentExpression aConstructorName) {
        return new ConstructorDef(aConstructorName, this, getContext());
    }

    public DestructorDef addDtor() {
        return new DestructorDef(this, getContext());
    }

    public Collection<ClassItem> findFunction(final String name) {
        return Collections2.filter(items, new Predicate<ClassItem>() {
            @Override
            public boolean apply(@Nullable final ClassItem item) {
                if (item instanceof FunctionDef && !(item instanceof ConstructorDef))
                    return ((FunctionDef) item).name().equals(name);
                return false;
            }
        });
    }

    public ClassTypes getType() {
        return _type;
    }

    public void setType(final ClassTypes aType) {
        _type = aType;
    }

    public void postConstruct() {
        assert nameToken != null;
        int destructor_count = 0;
        for (final ClassItem item : items) {
            if (item instanceof DestructorDef) destructor_count++;
        }
        assert destructor_count == 0 || destructor_count == 1;
    }

    public IdentExpression getNameNode() {
        return nameToken;
    }

    // region inheritance

    public void setInheritance(final ClassInheritance inh) {
        _inh = inh;
    }

    public ClassInheritance classInheritance() {
        return _inh;
    }

    public @NotNull Iterable<AnnotationPart> annotationIterable() {
        final List<AnnotationPart> aps = new ArrayList<AnnotationPart>();
        if (annotations == null) return aps;
        for (final AnnotationClause annotationClause : annotations) {
            aps.addAll(annotationClause.aps);
        }
        return aps;
    }

    // endregion

    // region annotations

    public FunctionDef funcDef() {
        return new FunctionDef(this, getContext());
    }

    // endregion

    // region called from parser

    public DefFunctionDef defFuncDef() {
        return new DefFunctionDef(this, getContext());
    }

    public PropertyStatement prop() {
        final PropertyStatement propertyStatement = new PropertyStatement(this, getContext());
        add(propertyStatement);
        return propertyStatement;
    }

    @Override // OS_Container
    public void add(final OS_Element anElement) {
        if (!(anElement instanceof ClassItem))
            throw new IllegalStateException(String.format("Cant add %s to ClassStatement", anElement));
        items.add((ClassItem) anElement);
    }

    public @org.jetbrains.annotations.Nullable TypeAliasStatement typeAlias() {
        NotImplementedException.raise();
        return null;
    }

    public InvariantStatement invariantStatement() {
        NotImplementedException.raise();
        return null;
    }

    public ProgramClosure XXX() {
        return new ProgramClosure() {};
    }

    public StatementClosure statementClosure() {
        return new AbstractStatementClosure(this);
    }

    // endregion

    public @NotNull List<TypeName> getGenericPart() {
        if (genericPart == null) return emptyTypeNameList;
        else return genericPart.p;
    }

    public void setGenericPart(final TypeNameList genericPart) {
        this.genericPart = genericPart;
    }

    public Collection<ConstructorDef> getConstructors() {
        final Collection<ClassItem> x = Collections2.filter(items, new Predicate<ClassItem>() {
            @Override
            public boolean apply(@Nullable final ClassItem input) {
                return input instanceof ConstructorDef;
            }
        });
        return Collections2.transform(x, new Function<ClassItem, ConstructorDef>() {
            @Nullable
            @Override
            public ConstructorDef apply(@Nullable final ClassItem input) {
                return (ConstructorDef) input;
            }
        });
    }

    public OS_Type getOS_Type() {
        if (osType == null) osType = new OS_UserClassType(this);
        return osType;
    }
}

//
//
//
