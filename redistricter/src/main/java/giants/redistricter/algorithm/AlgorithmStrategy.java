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
        while (!accepted) {
            move = generateMove();
            executeMove(move);
            if (isAcceptable()) {
                acceptMove(move);
                accepted = true;
            } else {
                revertMove(move);
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
        //update other things later too like demograhpics
        destDistrict.setArea(destDistrict.getArea() + precinct.getArea());
        srcDistrict.setArea(srcDistrict.getArea() - precinct.getArea());
    }

    void revertMove(Move move) {
        District srcDistrict = move.getSourceDistrict();
        District destDistrict = move.getDestinationDistrict();
        Precinct precinct = move.getPrecinct();

        srcDistrict.addPrecinct(precinct);
        destDistrict.removePrecinct(precinct);
        destDistrict.setArea(destDistrict.getArea() - precinct.getArea());
        srcDistrict.setArea(srcDistrict.getArea() + precinct.getArea());
    }
}
