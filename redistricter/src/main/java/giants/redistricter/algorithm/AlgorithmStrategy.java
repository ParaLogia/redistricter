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
    abstract boolean isComplete();
    abstract boolean isAcceptable(Move move);

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
            if (isAcceptable(move)) {
                    acceptMove(move);
                    accepted = true;
            } else{
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
        return movePool.removeFirst();
    }
    
    void executeMove(Move move) {
        District srcDistrict = move.getSourceDistrict();
        District destDistrict = move.getDestinationDistrict();
        Precinct precinct = move.getPrecinct();

        srcDistrict.removePrecinct(precinct);
        destDistrict.addPrecinct(precinct);
        moveHistory.addLast(move);
    }

    void revertMove(Move move) {
        District srcDistrict = move.getSourceDistrict();
        District destDistrict = move.getDestinationDistrict();
        Precinct precinct = move.getPrecinct();

        srcDistrict.addPrecinct(precinct);
        destDistrict.removePrecinct(precinct);
        moveHistory.removeLast();
    }

    public void acceptMove(Move move) {
        move.setObjectiveDelta(currObjValDelta);
        if (currObjValDelta > 0) {
            temperature -= COOLING_RATE;
        }
        previousObjValue = currentObjValue;

        movePool = null;
        bestMove = null;
        bestDelta = -Double.MAX_VALUE;
    }
}
