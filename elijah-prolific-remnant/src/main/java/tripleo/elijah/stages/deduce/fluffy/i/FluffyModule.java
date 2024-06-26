package tripleo.elijah.stages.deduce.fluffy.i;

import java.util.*;

public interface FluffyModule {
	FluffyLsp lsp();

	String name();

	List<FluffyMember> members();

	void find_multiple_items(FluffyComp aFc);

	void find_all_entry_points();
}
