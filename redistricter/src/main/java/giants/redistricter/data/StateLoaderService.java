package giants.redistricter.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import giants.redistricter.database.DatabaseService;
import org.springframework.stereotype.Repository;


@Service
public class StateLoaderService {
	public static void main(String[] args) {
		StateLoaderService serv = new StateLoaderService();
		State state = serv.getStateByShortName("NY");
		System.out.println(state.getName());
		System.out.println("Population: " + state.getPopulation());
	} 

    private Map<Integer,State> states;
    DatabaseService dbService = new DatabaseService();
    
    public State getStateByShortName(String shortName) {
        State state = checkForStateFile(shortName);
        if(state == null) {
            state = dbService.getStateByShortName(shortName);
            state.setPopulation(0);
            attachDistrictsToState(state);
        }
        return state;
    }
    
    public State getStateByFullName(String fullName) {
            State state = dbService.getStateByFullName(fullName);
            state.setPopulation(0);
            attachDistrictsToState(state);
            return state;
    }
    
    private void attachDistrictsToState(State state) {
        List<District> districts = dbService.getDistrictsByStateId(state.getStateId());
        state.setPrecincts(new HashSet<Precinct>());
        for(District d : districts) {
            attachPrecinctsToDistrict(d);
            state.getPrecincts().addAll(d.getPrecincts());
            state.setPopulation(d.getPopulation() + state.getPopulation());
        }
        
        state.setDistricts(new HashSet<District>(districts));
    }
    
    private void attachPrecinctsToDistrict(District d){
        List<Precinct> precincts = dbService.getPrecinctsByDistrictsId(d.getDistrictId());
        d.setPopulation(0);
        d.setArea(0.0);
        d.setPerimeter(0.0);
        for(Precinct p : precincts) {
            d.setPopulation(p.getPopulation() + d.getPopulation());
            d.setArea(d.getArea()+p.getArea());
            d.setPerimeter(d.getPerimeter()+p.getPerimeter());
        }
        d.setPrecincts(new HashSet<Precinct>(precincts));
    }

//    public State getState(String name) {
//        State state = checkForStateFile(name);
//        if(state == null) {
//        	gerrymandering.model.State dbBean = dbService.getState(name);
//        	if(dbBean == null) {
//        	    return new State();
//        	}
//        	state = sp.processState(dbBean);
//        	attachDistrictsToState(state);
//        	ObjectMapper mapper = new ObjectMapper();
//        	try {
//				mapper.writeValue(new File("src\\main\\resources\\"+dbBean.getShortName()+".json"), state);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}	
//        }
//
//        return state;
//    }

    public State checkForStateFile(String name) {
        StringBuilder content = new StringBuilder();
        ObjectMapper om = new ObjectMapper();

        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/"+name+".json"))){
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                content.append(sCurrentLine);
            }
        } catch (IOException e) {
            return null;
        } 
        try {
            State s = om.readValue(content.toString(), State.class);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;		
    }
}
