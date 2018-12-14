package giants.redistricter.algorithm;

import giants.redistricter.data.District;
import giants.redistricter.data.Precinct;
import giants.redistricter.data.State;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;

import static giants.redistricter.algorithm.Variation.*;

public abstract class AlgorithmStrategy {
    final double COOLING_RATE = 0.05;
    State state;
    ObjectiveFunction objFct;
    Variation variation;
    double temperature;

    double currentObjValue = 0.0;
    double previousObjValue = 0.0;
    double currObjValDelta = Double.MAX_VALUE;

    Deque<Move> movePool;
    Move bestMove;
    double bestDelta = -Double.MAX_VALUE;
    Deque<Move> moveHistory = new LinkedList<>();

    abstract Set<District> getDistricts();
    abstract Deque<Move> generateMoves();
    abstract void acceptMove(Move move);
    abstract boolean isComplete();

    public Move nextMove() {
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
//                if (move.getSourceDistrict().isContiguousWithChange(move.getPrecinct())) {
                    acceptMove(move);
                    accepted = true;
//                }
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

    public Move generateMove() {
        if (movePool == null) {
            movePool = generateMoves();
        }
        if (movePool.isEmpty()) {
            return bestMove;
        }
        return movePool.remove();
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

    public boolean isAcceptable() {
        currentObjValue = objFct.calculateObjectiveValue(getDistricts());
        switch (this.variation) {
            case ANY_ACCEPT:
                return true;

            case GREEDY_ACCEPT:
                return currentObjValue > previousObjValue
                        || movePool != null && movePool.isEmpty();

            case PROBABILISTIC_ACCEPT:
                // TODO actual probability
                return previousObjValue == 0
                        || currentObjValue / previousObjValue > temperature;

            default:
                assert false : "Invalid Variation";
                return false;
        }
    }
}
