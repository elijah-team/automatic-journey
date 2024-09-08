package tripleo.elijah.comp.i;

import tripleo.elijah.comp.RuntimeProcess;
import tripleo.elijah.comp.internal.EDR_ProcessRecord;
import tripleo.elijah_remnant.startup.ProlificStartup2;

public interface CR_State {
	ICompilationAccess ca();

	ProlificStartup2 get_startup();

	ICompilationBus.CB_Action getCur();

	void setCur(ICompilationBus.CB_Action cur);

	ICompilationAccess getCa();

	void setCa(ICompilationAccess ca);

	EDR_ProcessRecord pr();

	void setPr(EDR_ProcessRecord pr);

	RuntimeProcess.RuntimeProcesses rt();

	void set_rt(RuntimeProcess.RuntimeProcesses rt);
}
