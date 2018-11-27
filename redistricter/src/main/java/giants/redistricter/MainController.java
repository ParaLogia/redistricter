package giants.redistricter;

import giants.redistricter.algorithm.*;
import giants.redistricter.data.District;
import giants.redistricter.data.State;
import giants.redistricter.data.StateLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.Random;


@RestController
@Scope(value="session")
public class MainController {

    @Autowired
    StateLoaderService stateLoader;

    @Autowired
    AlgorithmService algorithm;

    @RequestMapping(path = "/select")
    public State select(@RequestParam String state) {
        return stateLoader.getState(state);
    }

    @RequestMapping(path = "/start", method = RequestMethod.POST, consumes = "application/json")
    public Collection<District> start(@RequestBody String data) {
        JacksonJsonParser parser = new JacksonJsonParser();
        Map<String, Object> map = parser.parseMap(data);

        State state = stateLoader.getState((String) map.get("state"));
        // TODO read weights from JSON
        ObjectiveFunction objFct = new ObjectiveFunctionBuilder()
                .addWeight(ObjectiveCriteria.valueOf("POLSBY_POPPER"), 1.0)
                .build();
        AlgorithmType alg = AlgorithmType.valueOf((String) map.get("algorithm"));
        Variation variation = Variation.valueOf((String) map.get("variation"));
        Random rand = new Random(Long.parseLong(String.valueOf(map.get("seed"))));

        return algorithm.start(state, objFct, alg, variation, rand);
    }

    @RequestMapping(path = "/next")
    public Move next() {
        return algorithm.next();
    }
}