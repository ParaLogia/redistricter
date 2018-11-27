package giants.redistricter.algorithm;

import giants.redistricter.data.District;
import giants.redistricter.data.Precinct;
import giants.redistricter.data.State;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.Set;

@Component
@Scope(value="session")
public class AlgorithmService {
    State state;
    AlgorithmStrategy strategy;
    ObjectiveFunction objFct;

    public Set<District> start(State state, ObjectiveFunction objFct,
                               AlgorithmType alg, Variation vari, Random rand){
        this.state = state;
        this.objFct = objFct;

        switch (alg) {
            case REGION_GROWING:
                strategy = new GrowingStrat(state, objFct, vari, rand);
                break;
            case SIMULATED_ANNEALING:
                strategy = new AnnealingStrat(state, objFct, vari, rand);
                break;
        }
        return strategy.getStatus();
    }

    public Move next(){
        return strategy.nextMove();
    }
}
