package tripleo.elijah.nextgen.expansion;

import junit.framework.*;
import tripleo.elijah.comp.*;
import tripleo.elijah.comp.Compilation.*;
import tripleo.elijah.comp.internal.*;
import tripleo.elijah.lang.*;
import tripleo.elijah.nextgen.model.*;
import tripleo.elijah.stages.gen_generic.*;
import tripleo.elijah.stages.logging.*;

import java.util.*;

import static tripleo.elijah.util.Helpers.*;

public class SX_NodeTest extends TestCase {

	public void testFullText() {
		final StdErrSink      errSink       = new StdErrSink();
		final IO              io            = new IO();
		final CompilationImpl comp          = new CompilationImpl(errSink, io);
		final AccessBus       ab            = new AccessBus(comp);
		final PipelineLogic   pipelineLogic = new PipelineLogic(ab);
		final OS_Module mod = comp.moduleBuilder()
		                          .withFileName("filename.elijah")
		                          .addToCompilation()
		                          .build();
		final OutputFileFactoryParams p    = new OutputFileFactoryParams(mod, errSink, ElLog.Verbosity.SILENT, pipelineLogic);
		final GenerateFiles           fgen = OutputFileFactory.create(CompilationAlways.defaultPrelude(), p);

		final SM_ClassDeclaration node = new SM_ClassDeclaration() {
			@Override
			public SM_Name name() {
				return new SM_Name() {
					@Override
					public String getText() {
						return "Main";
					}
				};
			}

			@Override
			public SM_ClassSubtype subType() {
				return SM_ClassSubtype.NORMAL;
			}

			@Override
			public SM_ClassInheritance inheritance() {
				return new SM_ClassInheritance() {
					@Override
					public List<SM_Name> names() {
						return List_of(new SM_Name() {
							@Override
							public String getText() {
								return "Arguments";
							}
						});
					}
				};
			}

			@Override
			public SM_ClassBody classBody() {
				return null;
			}
		};

		fgen.forNode(node);
	}
}