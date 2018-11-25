package giants.redistricter.algorithm;

import giants.redistricter.data.District;
import giants.redistricter.data.State;

import java.util.*;
import java.util.stream.Collectors;

public class AnnealingStrat implements AlgorithmStrategy {

    Collection<District> districts;
    Random random;
    Integer iterations;
    Integer MAX_ITERATIONS;
    Double DELTA_OBJ_VALUE;
    Double temperature;
    Variation variation;
    Double currentObjValue;
    Double currObjValDelta;
    List<Move> moves;

    public AnnealingStrat(State state, Variation variation, Random random){
        this.districts = state.getDistricts().stream()
                .map(District::new)
                .collect(Collectors.toCollection(TreeSet::new));
        this.variation = variation;
        this.random = random;
    }

    private Boolean checkIterations(){
        return false;
    }

    private Boolean checkDeltaObj(Double objVal){
        return false;
    }

    @Override
    public Collection<District> getStatus() {
        return this.districts;
    }

    @Override
    public Move generateMove() {
        return null;
    }

    @Override
    public void executeMove(Move move) {

    }

    @Override
    public boolean isAcceptable() {
        return true;
    }

    @Override
    public void acceptMove(Move move) {

    }

    @Override
    public void revertMove(Move move) {

    }

    @Override
    public Boolean isComplete(Double objValue) {
        return null;
    }

    @Override
    public District selectRandomDistrict() {
        return null;
    }
}
