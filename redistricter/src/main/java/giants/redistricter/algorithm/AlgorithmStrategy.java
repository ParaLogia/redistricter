package giants.redistricter.algorithm;

import giants.redistricter.data.District;
import giants.redistricter.data.Precinct;
import giants.redistricter.data.State;

import java.util.Set;

public abstract class AlgorithmStrategy {
    final double COOLING_RATE = 0.05;
    State state;
    ObjectiveFunction objFct;

    abstract Set<District> getDistricts();
    abstract Move generateMove();
    abstract boolean isAcceptable();
    abstract void acceptMove(Move move);
    abstract boolean isComplete();

    Move nextMove() {
        Move move = null;

        if (isComplete()) {
            return move;
        }
        boolean accepted = false;
        int tries = 0;
        int max_tries = 500;
        while (!accepted) {
            move = generateMove();
            executeMove(move);
            //contiguity might break it, remove if doesn't work.
            if (isAcceptable()) {
                if (move.getSourceDistrict().isContiguousWithChange(move.getPrecinct())) {
                    acceptMove(move);
                    accepted = true;
                }
            } else {
                revertMove(move);
            }
            tries++;
            if (tries >= max_tries) {
                return null;
            }
        }
        return move;
    }
    
    void executeMove(Move move) {
        District srcDistrict = move.getSourceDistrict();
        District destDistrict = move.getDestinationDistrict();
        Precinct precinct = move.getPrecinct();

        srcDistrict.removePrecinct(precinct);
        destDistrict.addPrecinct(precinct);
    }

    void revertMove(Move move) {
        District srcDistrict = move.getSourceDistrict();
        District destDistrict = move.getDestinationDistrict();
        Precinct precinct = move.getPrecinct();

        srcDistrict.addPrecinct(precinct);
        destDistrict.removePrecinct(precinct);
    }
}
