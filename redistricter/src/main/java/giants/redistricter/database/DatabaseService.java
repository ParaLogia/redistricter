package giants.redistricter.database;

import java.util.*;

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
		List<Object> stateRecords;
		State toReturn = new State();
		try {
			hb = HibernateManager.getInstance();
			Map<String,Object> criteria = new HashMap<>();
			criteria.put("stateId", stateId);
			stateRecords = hb.getRecordsBasedOnCriteria(State.class, criteria);
			toReturn = (State)stateRecords.get(0);
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
			List<Object> stateRecords;
			State toReturn = new State();
			try {
				hb = HibernateManager.getInstance();
				Map<String,Object> criteria = new HashMap<>();
				criteria.put("shortName", shortName);
				stateRecords = hb.getRecordsBasedOnCriteria(State.class, criteria);
				toReturn = (State)stateRecords.get(0);
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
		List<Object> stateRecords;
		Collection<State> toReturn = new LinkedHashSet<>();
		try {
			hb = HibernateManager.getInstance();
			stateRecords = hb.getAllRecords(State.class);
			for(Object state : stateRecords) {
				toReturn.add((State) state);
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
		List<Object> districtRecords;
		Collection<District> toReturn = new LinkedHashSet<>();
		try {
			hb = HibernateManager.getInstance();
			Map<String,Object> criteria = new HashMap<>();
			criteria.put("stateId", stateId);
			districtRecords = hb.getRecordsBasedOnCriteria(District.class, criteria);
			for(Object district : districtRecords) {
				toReturn.add((District) district);
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
		District d;
		Set<Precinct> toReturn = new LinkedHashSet<>();
		try {
			hb = HibernateManager.getInstance();
			Map<String,Object> criteria = new HashMap<>();
			criteria.put("stateId", stateId);
			districtList = hb.getRecordsBasedOnCriteria(District.class, criteria);
			for(Object district : districtList) {
				d = (District)district;
				criteria.clear();
				criteria.put("districtId", d.getDistrictId());
				precinctList = hb.getRecordsBasedOnCriteria(Precinct.class, criteria);
				for(Object precinct : precinctList) {
					toReturn.add((Precinct)precinct);
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
