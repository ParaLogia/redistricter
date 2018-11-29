package giants.redistricter.algorithm;

import giants.redistricter.data.District;
import giants.redistricter.data.Precinct;
import giants.redistricter.data.State;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class GrowingStrat extends AlgorithmStrategy {
    @Autowired
    RandomService random;

    Set<District> districts;
    District precinctPool;
    double temperature;
    Variation variation;
    double previousObjValue;
    double currentObjValue;

    public GrowingStrat(State state, ObjectiveFunction objFct, Variation variation){
        this.state = state;
        this.objFct = objFct;
        this.variation = variation;
        Set<Precinct> precincts = state.getPrecincts();
        precinctPool = new District(-1);
        temperature = 1.0;
        currentObjValue = 0.0;

        precinctPool.addPrecincts(precincts);
        initSeeds();
    }

    private void initSeeds(){
        districts = new LinkedHashSet<>();
        for (District origDist : state.getDistricts()) {
            District district = new District(origDist.getId());
            Precinct seed = random.select(origDist.getPrecincts());
            district.addPrecinct(seed);
            precinctPool.removePrecinct(seed);
        }
    }

    @Override
    public Set<District> getDistricts() {
        // Consider returning the precinct pool as well?
        return districts;
    }

    private District getDistrictToGrow(){
        // TODO get queue of possible moves (in case lowest pop. can't grow)
        return districts.stream()
                .min(Comparator.comparing(District::getPopulation))
                .get();
    }

    @Override
    public Move generateMove() {
        District smallDistrict = getDistrictToGrow();
        Precinct borderPrecinct;
        List<Precinct> addablePrecincts;
        Precinct precinctToAdd;
        Move move;

        borderPrecinct = random.select(smallDistrict.getBorderPrecincts());
        addablePrecincts = borderPrecinct.getNeighbors().keySet()
                .stream()
                .filter(precinctPool.getPrecincts()::contains)
                .collect(Collectors.toCollection(ArrayList::new));
        precinctToAdd = random.select(addablePrecincts);
        move = new Move();
        move.setSourceDistrict(precinctPool);
        move.setDestinationDistrict(smallDistrict);
        move.setPrecinct(precinctToAdd);
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
        return precinctPool.getPrecincts().isEmpty();
    }
}
