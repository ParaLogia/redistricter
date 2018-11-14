package giants.redistricter.algorithm;

import giants.redistricter.data.District;
import giants.redistricter.data.State;

import java.util.Collection;
import java.util.Random;

public class GrowingStrat implements AlgorithmStrategy {
    Collection<District> districts;
    Random random;
    District precinctPool;
    Double temperature;
    Variation variation;
    Double DELTA_OBJ_VALUE;
    Double currentObjValue;
    Double currObjValDelta;

    public GrowingStrat(State state, Variation vari, Random rand){

    }

    private void initSeeds(){
        return;
    }
    private District getLowestPopDistrict(){
        return null;
    }

    @Override
    public Collection<District> getStatus() {
        return null;
    }

    @Override
    public Move generateMove() {
        return null;
    }

    @Override
    public void executeMove(Move move) {

    }

    @Override
    public Boolean isAcceptable(Double objVal) {
        return null;
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
