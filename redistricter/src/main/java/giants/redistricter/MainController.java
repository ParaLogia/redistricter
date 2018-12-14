package giants.redistricter;

import giants.redistricter.algorithm.*;
import giants.redistricter.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;


@RestController
@Scope(value="session")
public class MainController {
    // TODO Remove once we get the database running
    final boolean JSON_LOAD = true;

    @Autowired
    StateLoaderService stateLoader;

    @Autowired
    MockStateLoader mockStateLoader;

    @Autowired
    AlgorithmService algorithm;

    @Autowired
    RandomService randomService;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(path = "/select")
    public State select(@RequestParam String state) {
        if (JSON_LOAD) {
            return new JsonStateLoader().getStateByShortName(state);
        }
        else {
            return stateLoader.getStateByShortName(state);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(path = "/start", method = RequestMethod.POST, consumes = "application/json")
    public Set<District> start(@RequestBody String data) {
        JacksonJsonParser parser = new JacksonJsonParser();
        Map<String, Object> map = parser.parseMap(data);

        String stateName = (String) map.get("abbreviation");
        State state;
        if (stateName.startsWith("MOCK")) {
            String[] args = stateName.split(" ");
            state = mockStateLoader.loadMockState(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        }
        else if (JSON_LOAD) {
            state = new JsonStateLoader().getStateByShortName(stateName);
        }
        else {
            state = stateLoader.getStateByShortName(stateName);
        }
        Map<String, Double> weights = (Map) map.get("weights");
        ObjectiveFunctionBuilder objectiveBuilder = new ObjectiveFunctionBuilder();
        //change the below line if needed.
        //objectiveBuilder.setYear(Integer.parseInt(String.valueOf(map.get("year"))));
        objectiveBuilder.setYear(2016);
        weights.forEach((criteria, weight) -> {
            objectiveBuilder.addWeight(ObjectiveCriteria.valueOf(criteria), weight);
        });
        ObjectiveFunction objFct = objectiveBuilder.build();
        AlgorithmType alg = AlgorithmType.valueOf((String) map.get("algorithm"));
        Variation variation = Variation.valueOf((String) map.get("variation"));
        long seed = Long.parseLong(String.valueOf(map.get("seed")));
        randomService.setSeed(seed);

        int numDistricts = state.getDistricts().size();
        if (alg == AlgorithmType.REGION_GROWING) {
            Object districts = map.get("districts");
            if (districts != null) {
                numDistricts = Integer.parseInt(String.valueOf(districts));
            }
        }

        return algorithm.start(state, objFct, alg, variation, numDistricts);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(path = "/next")
    public Move next() {
        return algorithm.next();
    }
}