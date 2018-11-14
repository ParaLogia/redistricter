package giants.redistricter.algorithm;

import giants.redistricter.data.District;
import giants.redistricter.data.State;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Random;

@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AlgorithmService {
    State state;
    AlgorithmStrategy strategy;
    ObjectiveFunction objFct;

    public Collection<District> start(State state, ObjectiveFunction objFct,
                                      AlgorithmType alg, Variation vari, Random rand){
        this.state = state;
        this.objFct = objFct;

        if (alg == AlgorithmType.REGION_GROWING) {
            strategy = new GrowingStrat(state, vari, rand);
        }
        else {
            strategy = new AnnealingStrat(state, vari, rand);
        }
        return strategy.getStatus();
    }

    public Move next(){
        return null;
    }
}
