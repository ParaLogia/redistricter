package giants.redistricter.algorithm;

import giants.redistricter.data.District;
import giants.redistricter.data.Precinct;
import giants.redistricter.data.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;

public class GrowingStrat implements AlgorithmStrategy {
    State state;
    Collection<District> districts;
    Random random;
    District precinctPool;
    Double temperature;
    Variation variation;
    final Double DELTA_OBJ_VALUE;
    final Double COOLING_RATE;
    Double previousObjValue;
    Double currentObjValue;
    Double currObjValDelta;

    public GrowingStrat(State state, Variation variation, Random rand){
        Collection<Precinct> precincts = state.getPrecincts();
        precinctPool = new District(-1);
        temperature = 1.0;
        currentObjValue = 0.0;

        // FIXME load constants from elsewhere?
        DELTA_OBJ_VALUE = 0.01;
        COOLING_RATE = 0.05;

        precinctPool.addPrecincts(precincts);
        initSeeds();
    }

    private void initSeeds(){
        /* Choose one random precinct from each of the original districts.
         * This approach is subject to change. */
        districts = new ArrayList<>();
        for (District origDist : state.getDistricts()) {
            District district = new District(origDist.getId());
            Precinct seed = RandomService.select(origDist.getPrecincts(), random);
            district.addPrecinct(seed);
            precinctPool.removePrecinct(seed);
        }
    }

    @Override
    public Collection<District> getStatus() {
        // Consider returning the precinct pool as well?
        return districts;
    }

    private District getLowestPopDistrict(){
        return districts.stream()
                .min(Comparator.comparing(District::getPopulation))
                .get();
    }

    @Override
    public Move generateMove() {
        District smallDistrict = getLowestPopDistrict();
        Precinct borderPrecinct;
        Collection<Precinct> addablePrecincts;
        Precinct precinctToAdd;
        Move move;

        borderPrecinct = RandomService.select(smallDistrict.getBorderPrecincts(), random);
        addablePrecincts = borderPrecinct.getNeighbors().keySet()
                .stream()
                .filter(precinctPool.getPrecincts()::contains)
                .collect(Collectors.toCollection(ArrayList::new));
        precinctToAdd = RandomService.select(addablePrecincts, random);
        move = new Move();
        move.setSourceDistrict(precinctPool);
        move.setDestinationDistrict(smallDistrict);
        move.setPrecinct(precinctToAdd);
        return move;
    }

    @Override
    public void executeMove(Move move) {
        District srcDistrict = move.getSourceDistrict();
        District destDistrict = move.getDestinationDistrict();
        Precinct precinct = move.getPrecinct();

        srcDistrict.removePrecinct(precinct);
        destDistrict.addPrecinct(precinct);
    }

    @Override
    public boolean isAcceptable(double objectiveValue) {
        // TODO move objective function calculation into this class
        currentObjValue = objectiveValue;
        switch (this.variation) {
            case GREEDY_ACCEPT:
                return objectiveValue > previousObjValue;

            case PROBABILISTIC_ACCEPT:
                return previousObjValue == 0
                    || objectiveValue / previousObjValue > temperature;

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
    public void revertMove(Move move) {
        District srcDistrict = move.getSourceDistrict();
        District destDistrict = move.getDestinationDistrict();
        Precinct precinct = move.getPrecinct();

        destDistrict.addPrecinct(precinct);
        srcDistrict.removePrecinct(precinct);
    }

    @Override
    public Boolean isComplete(Double objValue) {
        return (currObjValDelta < DELTA_OBJ_VALUE)
                || precinctPool.getPrecincts().isEmpty();
    }
}
