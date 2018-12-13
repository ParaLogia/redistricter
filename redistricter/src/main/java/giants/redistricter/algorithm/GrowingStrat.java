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

    public GrowingStrat(State state, ObjectiveFunction objFct,
                        Variation variation, RandomService random){
        this.state = state;
        this.objFct = objFct;
        this.variation = variation;
        this.random = random;
        Set<Precinct> precincts = state.getPrecincts();
        precinctPool = new District(-1);
        temperature = 1.0;

        precinctPool.addPrecincts(precincts);
        initSeeds();

        currentObjValue = objFct.calculateObjectiveValue(districts);
    }

    private void initSeeds(){
        districts = new LinkedHashSet<>();
        for (District origDist : state.getDistricts()) {
            District district = new District(origDist.getDistrictId());
            Precinct seed = random.select(origDist.getPrecincts());
            district.addPrecinct(seed);
            precinctPool.removePrecinct(seed);
            districts.add(district);
        }
    }

    @Override
    public Set<District> getDistricts() {
        // Consider returning the precinct pool as well?
        return districts;
    }

    @Override
    public Move generateMove() {
        List<Precinct> addablePrecincts;
        Precinct precinctToAdd;
        Move move = new Move();;

        List<District> sortedPopDists = districts.stream()
                .sorted(Comparator.comparing(District::getPopulation))
                .collect(Collectors.toList());

        for (District districtToGrow : sortedPopDists) {
            Set<Precinct> borders = districtToGrow.getBorderPrecincts();
            addablePrecincts = borders.stream()
                    .flatMap(p -> p.getNeighbors().keySet().stream())
                    .filter(p -> precinctPool.getPrecincts().contains(p))
                    .collect(Collectors.toCollection(ArrayList::new));
            if (addablePrecincts.size() > 0) {
                precinctToAdd = random.select(addablePrecincts);
                move.setSourceDistrict(precinctPool);
                move.setDestinationDistrict(districtToGrow);
                move.setPrecinct(precinctToAdd);
                return move;
            }
        }
        return move;
    }

    @Override
    public boolean isAcceptable() {
        currentObjValue = objFct.calculateObjectiveValue(getDistricts());
        switch (this.variation) {
            case ANY_ACCEPT:
                return true;

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
        move.setObjectiveDelta(currentObjValue - previousObjValue);
        if (previousObjValue > currentObjValue) {
            temperature -= COOLING_RATE;
        }
        previousObjValue = currentObjValue;
        temperature -= COOLING_RATE;
    }

    @Override
    public boolean isComplete() {
        return precinctPool.getPrecincts().isEmpty();
    }
}
