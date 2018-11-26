package giants.redistricter.database;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import gerrymandering.HibernateManager;
import gerrymandering.model.District;
import gerrymandering.model.Precinct;
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
	
	//Get State by Short Name
		public State getState(String shortName) {
			List<Object> l;
			Iterator<Object> itr;
			State toReturn = new State();
			try {
				hb = HibernateManager.getInstance();
				Map<String,Object> criteria = new HashMap<>();
				criteria.put("shortName", shortName);
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
	public Collection<State> getAllStates(){
		List<Object> l;
		Iterator<Object> itr;
		Collection<State> toReturn = new HashSet<State>();
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
	
	public Collection<District> getAllDistrictsForState(int stateId){
		List<Object> l;
		Iterator<Object> itr;
		Collection<District> toReturn = new HashSet<District>();
		try {
			hb = HibernateManager.getInstance();
			Map<String,Object> criteria = new HashMap<>();
			criteria.put("stateId", stateId);
			l = hb.getRecordsBasedOnCriteria(District.class, criteria);
			itr = l.iterator();
			while(itr.hasNext()) {
				toReturn.add((District) itr.next());
			}
			return toReturn;
		}
		catch(Throwable e) {
			System.out.println("Exception: ");
			e.printStackTrace();
			return toReturn;
		}
	}
	
	public Set<Precinct> getAllPrecinctsForState(int stateId){
		List<Object> districtList;
		List<Object> precinctList;
		Iterator<Object> precinctItr;
		Iterator<Object> districtItr;
		District d;
		Set<Precinct> toReturn = new HashSet<Precinct>();
		try {
			hb = HibernateManager.getInstance();
			Map<String,Object> criteria = new HashMap<>();
			criteria.put("stateId", stateId);
			districtList = hb.getRecordsBasedOnCriteria(District.class, criteria);
			districtItr = districtList.iterator();
			while(districtItr.hasNext()) {
				d = (District) districtItr.next();
				criteria = new HashMap<>();
				criteria.put("districtId", d.getDistrictId());
				precinctList = hb.getRecordsBasedOnCriteria(Precinct.class, criteria);
				precinctItr = precinctList.iterator();
				while(precinctItr.hasNext()) {
					toReturn.add((Precinct) precinctItr.next());
				}			
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
