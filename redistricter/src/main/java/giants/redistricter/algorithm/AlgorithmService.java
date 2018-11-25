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

    public Move next(){
        if (strategy == null) {
            return null;        // Consider throwing an exception?
        }
        Move move = null;
        boolean accepted = false;
        while (!accepted) {
            move = strategy.generateMove();
            strategy.executeMove(move);
            if (strategy.isAcceptable()) {
                strategy.acceptMove(move);
                accepted = true;
            } else {
                strategy.revertMove(move);
            }
        }

        return move;
    }
}
