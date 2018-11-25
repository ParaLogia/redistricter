package giants.redistricter.algorithm;

import giants.redistricter.data.District;
import giants.redistricter.data.Precinct;
import giants.redistricter.data.State;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Random;

@Component
@Scope(value="session")
public class AlgorithmService {
    State state;
    AlgorithmStrategy strategy;
    ObjectiveFunction objFct;

    public Collection<District> start(State state, ObjectiveFunction objFct,
                                      AlgorithmType alg, Variation vari, Random rand){
        this.state = state;
        this.objFct = objFct;

        switch (alg) {
            case REGION_GROWING:
                strategy = new GrowingStrat(state, vari, rand);
                break;
            case SIMULATED_ANNEALING:
                strategy = new AnnealingStrat(state, vari, rand);
                break;
        }
        return strategy.getStatus();
    }

    int i;
    public Move next(){
        // FIXME temporary code
        Move move = new Move();
        Precinct precinct = new Precinct();
        precinct.setId(++i);
        move.setPrecinct(precinct);
        return move;
    }
}
