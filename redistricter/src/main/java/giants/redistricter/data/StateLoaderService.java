package giants.redistricter.data;

import giants.redistricter.data.District;
import giants.redistricter.data.State;
import giants.redistricter.database.DatabaseService;
import giants.redistricter.processor.DistrictProcessor;
import giants.redistricter.processor.StateProcessor;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


@Repository
public class StateLoaderService {
	public static void main(String[] args) {
		StateLoaderService serv = new StateLoaderService();
		serv.getState("NY");
	}
	
	
	
    private Map<Integer,State> states;
    DatabaseService dbService = new DatabaseService();
    StateProcessor sp = new StateProcessor();
    DistrictProcessor dp = new DistrictProcessor();

    int i;
    // TODO decide whether to use integer IDs or Strings
    public State getState(String name) {
        State state = new State();
        state = checkForStateFile(name);
        if(state == null) {
        	gerrymandering.model.State dbBean = new gerrymandering.model.State();
        	dbBean = dbService.getState(name);
        	state = sp.processState(dbBean);    
        	ObjectMapper mapper = new ObjectMapper();
        	try {
				mapper.writeValue(new File("src\\main\\resources\\"+dbBean.getShortName()+".json"), state);
			} catch (Exception e) {
				e.printStackTrace();
			}	
        }

        Collection<District> dists = new TreeSet<>(Comparator.comparing(District::getId));
        state.setDistricts(dists);
        //FIXME temporary
        dists.add(new District(++i));
        dists.add(new District(++i));
        dists.add(new District(++i));
        dists.add(new District(++i));
        dists.add(new District(++i));

        return state;
    }
	public State checkForStateFile(String name) {
		BufferedReader br = null;
        FileReader fr = null;
        StringBuilder content = new StringBuilder();
        ObjectMapper om = new ObjectMapper();

        try {
            fr = new FileReader("src/main/resources/"+name+".json");
            br = new BufferedReader(fr);

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                content.append(sCurrentLine);
            }
        } catch (IOException e) {
        	return null;
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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
