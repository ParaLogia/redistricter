package giants.redistricter;

import giants.redistricter.algorithm.*;
import giants.redistricter.data.District;
import giants.redistricter.data.MockStateLoader;
import giants.redistricter.data.State;
import giants.redistricter.data.StateLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;


@RestController
@Scope(value="session")
public class MainController {

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
        return stateLoader.getStateByShortName(state);
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
            state = mockStateLoader.loadState(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        }
        else {
            state = stateLoader.getStateByShortName(stateName);
        }
        Map<String, Double> weights = (Map) map.get("weights");
        ObjectiveFunctionBuilder objectiveBuilder = new ObjectiveFunctionBuilder();
        weights.forEach((criteria, weight) -> {
            objectiveBuilder.addWeight(ObjectiveCriteria.valueOf(criteria), weight);
        });
        ObjectiveFunction objFct = objectiveBuilder.build();
        AlgorithmType alg = AlgorithmType.valueOf((String) map.get("algorithm"));
        Variation variation = Variation.valueOf((String) map.get("variation"));
        long seed = Long.parseLong(String.valueOf(map.get("seed")));
        randomService.setSeed(seed);

        return algorithm.start(state, objFct, alg, variation);
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(path = "/next")
    public Move next() {
        return algorithm.next();
    }
}