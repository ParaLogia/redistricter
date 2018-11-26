package giants.redistricter.processor;

import giants.redistricter.data.State;

public class StateProcessor {
	
	public State processState(gerrymandering.model.State state) {
		State toReturn = new State();
		toReturn.setName(state.getName());
		toReturn.setId(state.getStateId());
		return toReturn;
	}

}
