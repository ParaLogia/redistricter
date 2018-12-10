package giants.redistricter.algorithm;

import giants.redistricter.data.District;
import giants.redistricter.data.Precinct;
import giants.redistricter.data.State;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class AnnealingStrat extends AlgorithmStrategy {

    @Autowired
    RandomService random;

    Variation variation;
    Set<District> districts;
    int iterations = 0;
    final int MAX_ITERATIONS = 5000;
    final double CONVERGED_DELTA_VAL = 0.01;
    double temperature = 1.0;
    double currentObjValue = 0.0;
    double previousObjValue = 0.0;
    double currObjValDelta = Double.MAX_VALUE;
    List<Move> moves;

    public AnnealingStrat(State state, ObjectiveFunction objFct, Variation variation){
        this.state = state;
        this.objFct = objFct;
        this.variation = variation;
        this.districts = state.getDistricts().stream()
                .map(District::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<District> getDistricts() {
        return this.districts;
    }

    @Override
    public Move generateMove() {
        District srcDistrict;
        Precinct precinct;
        District destDistrict = null;
        Move move;

        srcDistrict = random.select(districts);
        precinct = random.select(srcDistrict.getBorderPrecincts());
        // Consider storing a lookup table to map precincts to their districts
        for (District district : districts) {
            if (district.getPrecincts().contains(precinct)) {
                destDistrict = district;
                break;
            }
        }
        if (destDistrict == null) {
            assert false : "Precinct without district: " + precinct;
        }

        move = new Move();
        move.setSourceDistrict(srcDistrict);
        move.setPrecinct(precinct);
        move.setDestinationDistrict(destDistrict);
        return move;
    }

    @Override
    public boolean isAcceptable() {
        currentObjValue = objFct.calculateObjectiveValue(getDistricts());
        switch (this.variation) {
            case GREEDY_ACCEPT:
                return currentObjValue > previousObjValue;

            case PROBABILISTIC_ACCEPT:
                // TODO actual probability
                return previousObjValue == 0
                        || currentObjValue / previousObjValue > temperature;

            default:
                assert false : "Invalid Variation";
                return false;
        }
    }

    @Override
    public void acceptMove(Move move) {
        previousObjValue = currentObjValue;
        temperature -= COOLING_RATE;
    }

    @Override
    public boolean isComplete() {
        return iterations > MAX_ITERATIONS
                || currObjValDelta < CONVERGED_DELTA_VAL;
    }
}
