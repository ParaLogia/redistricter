package giants.redistricter.database;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import gerrymandering.HibernateManager;
import gerrymandering.model.State;


@Service
public class DatabaseService {
	HibernateManager hb;
	public State getState(State state) {
		List<Object> l;
		Iterator<Object> itr;
		try {
			hb = HibernateManager.getInstance();
			Map<String,Object> criteria = new HashMap<>();
			criteria.put("stateId", state.getStateId());
			l = hb.getRecordsBasedOnCriteria(State.class, criteria);
			itr = l.iterator();
			State toReturn = (State) itr.next();
			return toReturn;
		}
		catch(Throwable e) {
			System.out.println("Exception: ");
			e.printStackTrace();
			return state;
		}
		
		
		
	}
	
}
