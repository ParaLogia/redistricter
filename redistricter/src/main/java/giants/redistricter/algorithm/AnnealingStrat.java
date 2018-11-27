package giants.redistricter.algorithm;

import giants.redistricter.data.District;
import giants.redistricter.data.Precinct;
import giants.redistricter.data.State;

import java.util.*;
import java.util.stream.Collectors;

public class AnnealingStrat extends AlgorithmStrategy {

    Variation variation;
    Random random;
    Set<District> districts;
    Integer iterations;
    final double DELTA_OBJ_VALUE = 0.01;
    double temperature;
    double currentObjValue;
    double currObjValDelta;
    List<Move> moves;

    public AnnealingStrat(State state, ObjectiveFunction objFct, Variation variation, Random random){
        this.state = state;
        this.objFct = objFct;
        this.variation = variation;
        this.random = random;
        this.districts = state.getDistricts().stream()
                .map(District::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private boolean checkIterations(){
        return false;
    }

    private boolean checkDeltaObj(double objVal){
        return false;
    }

    @Override
    public Set<District> getStatus() {
        return this.districts;
    }

    @Override
    public Move generateMove() {
        return null;
    }

    @Override
    public boolean isAcceptable() {
        return true;
    }

    @Override
    public void acceptMove(Move move) {

    }

    @Override
    public boolean isComplete() {
        return true;
    }

}
