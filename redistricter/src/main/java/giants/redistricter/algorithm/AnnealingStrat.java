package giants.redistricter.algorithm;

import giants.redistricter.data.District;
import giants.redistricter.data.Precinct;
import giants.redistricter.data.State;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class AnnealingStrat extends AlgorithmStrategy {
    int iterations = 0;
    final int MAX_ITERATIONS = 5000;
    int losingStreak = 0;
    final int MAX_LOSING_STREAK = 50;

    public AnnealingStrat(State state, ObjectiveFunction objFct,
                          Variation variation, RandomService random){
        this.state = state;
        this.random = random;
        this.objFct = objFct;
        this.variation = variation;
        this.districts = new LinkedHashSet<>();

        for (District origDistrict : state.getDistricts()) {
            District newDistrict = new District(origDistrict);
            for (Precinct precinct : newDistrict.getPrecincts()) {
                precinct.setDistrictId(newDistrict.getDistrictId());
                precinct.setDistrict(newDistrict);
            }
            districts.add(newDistrict);
        }

        currentObjValue = objFct.calculateObjectiveValue(districts);
    }

    @Override
    public Set<District> getDistricts() {
        return this.districts;
    }

    @Override
    public Deque<Move> generateMoves() {
        Deque<Move> moves = new LinkedList<>();

        boolean badDist = true;
        District dist = null;
        while (badDist) {
            dist = random.select(districts);
            badDist = dist.getPrecincts().size() <= 1;
        }
        District srcDistrict = dist;
        srcDistrict.getBorderPrecincts().stream()
                .forEach(precinct -> {
                    precinct.getNeighbors().keySet().stream()
                            .filter(n -> n.getDistrict() != srcDistrict)
                            .map(Precinct::getDistrict)
                            .distinct()
                            .forEach(destDistrict -> {
                                Move move = new Move();
                                move.setSourceDistrict(srcDistrict);
                                move.setDestinationDistrict(destDistrict);
                                move.setPrecinct(precinct);
                                moves.add(move);
                            });
                });

        return moves;
    }

    @Override
    public boolean isAcceptable(Move move) {
        if (!super.isAcceptable(move)) {
            return false;
        }
//        if (!move.getSourceDistrict().isContiguousWithChange(move.getPrecinct())) {
//            return false;
//        }
        return true;
    }

    @Override
    public void acceptMove(Move move) {
        super.acceptMove(move);
        if (currObjValDelta < 0) {
            losingStreak++;
        }
        else {
            losingStreak = 0;
        }
        System.err.println(currentObjValue);
        iterations++;
    }

    @Override
    public boolean isComplete() {
        if (losingStreak > MAX_LOSING_STREAK) {
            return true;
        }
        return iterations > MAX_ITERATIONS;
    }
}
