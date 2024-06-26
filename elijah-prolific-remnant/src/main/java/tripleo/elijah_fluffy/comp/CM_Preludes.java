package tripleo.elijah_fluffy.comp;

import tripleo.elijah.stages.gen_c.*;
import tripleo.elijah.stages.gen_generic.*;

import java.util.*;

public enum CM_Preludes {
	C {
		@Override
		public GenerateFiles create(final OutputFileFactoryParams aParams) {
			return new GenerateC(aParams);
		}

		@Override
		public String getName() {
			return "c";
		}
	},
	JAVA {
		@Override
		public GenerateFiles create(final OutputFileFactoryParams aParams) {
			return null;
		}

		@Override
		public String getName() {
			return "java";
		}
	};

	public abstract GenerateFiles create(final OutputFileFactoryParams aP);

	public interface _Creator {
		GenerateFiles create(OutputFileFactoryParams aParams);
	}

	public static _Creator dispatch(final String lang) {
		for (CM_Preludes cmPreludes : CM_Preludes.values()) {
			if (Objects.equals(lang, cmPreludes.getName()))
				return new _Creator(){
					@Override
					public GenerateFiles create(final OutputFileFactoryParams aParams) {
						return cmPreludes.create(aParams);
					}
				};
		}
		//throw new NotImplementedException();
		return null;
	}

	public abstract String getName();
}
