package giants.redistricter.database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import gerrymandering.HibernateManager;
import gerrymandering.model.State;


@Service
public class DatabaseService {
	HibernateManager hb;
	
	//Get State by ID
	public State getState(int stateId) {
		List<Object> l;
		Iterator<Object> itr;
		State toReturn = new State();
		try {
			hb = HibernateManager.getInstance();
			Map<String,Object> criteria = new HashMap<>();
			criteria.put("stateId", stateId);
			l = hb.getRecordsBasedOnCriteria(State.class, criteria);
			itr = l.iterator();
			toReturn = (State) itr.next();
			return toReturn;
		}
		catch(Throwable e) {
			System.out.println("Exception: ");
			e.printStackTrace();
			return toReturn;
		}		
	}	
	
	//Gets all states in DB
	public Set<State> getAllStates(){
		List<Object> l;
		Iterator<Object> itr;
		Set<State> toReturn = new HashSet<State>();
		try {
			hb = HibernateManager.getInstance();
			l = hb.getAllRecords(State.class);
			itr = l.iterator();
			while(itr.hasNext()) {
				toReturn.add((State) itr.next());
			}
			return toReturn;
		}
		catch(Throwable e) {
			System.out.println("Exception: ");
			e.printStackTrace();
			return toReturn;
		}
	}
	
	
	
	
	
}
