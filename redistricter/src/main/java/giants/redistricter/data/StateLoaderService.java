package giants.redistricter.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import giants.redistricter.database.DatabaseService;
import giants.redistricter.processor.DistrictProcessor;
import giants.redistricter.processor.StateProcessor;


@Repository
public class StateLoaderService {
	public static void main(String[] args) {
		StateLoaderService serv = new StateLoaderService();
		serv.getState("AZ");
	}

    private Map<Integer,State> states;
    DatabaseService dbService = new DatabaseService();
    StateProcessor sp = new StateProcessor();
    DistrictProcessor dp = new DistrictProcessor();

    public State getState(String name) {
        State state = checkForStateFile(name);
        if(state == null) {
        	gerrymandering.model.State dbBean = dbService.getState(name);
        	if(dbBean == null) {
        	    return new State();
        	}
        	state = sp.processState(dbBean);
        	attachDistrictsToState(state);
        	ObjectMapper mapper = new ObjectMapper();
        	try {
				mapper.writeValue(new File("src\\main\\resources\\"+dbBean.getShortName()+".json"), state);
			} catch (Exception e) {
				e.printStackTrace();
			}	
        }

        return state;
    }

	private void attachDistrictsToState(State state) {
        Set<gerrymandering.model.District> districtDbBeans = dbService.getAllDistrictsForState(state.getId());
        Set<District> districts = dp.processDistricts(districtDbBeans);
        state.setDistricts(districts);
    }

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
