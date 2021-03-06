package giants.redistricter.algorithm;

import giants.redistricter.data.District;
import giants.redistricter.data.Precinct;
import giants.redistricter.data.State;

import java.util.*;
import java.util.stream.Collectors;

public class GrowingStrat extends AlgorithmStrategy {
    private Set<District> districts;
    private District precinctPool;

    final int MAX_MOVE_POOL_SIZE = 150;

    public GrowingStrat(State state, ObjectiveFunction objFct,
                        Variation variation, RandomService random, int numSeeds){
        this.state = state;
        this.objFct = objFct;
        this.variation = variation;
        this.random = random;
        Set<Precinct> precincts = state.getPrecincts();
        precinctPool = new District(-1);

        precinctPool.addPrecincts(precincts);
        initSeeds(numSeeds);
        System.err.println("numSeeds: " + numSeeds);
        System.err.println("dists: " + districts.size());

        currentObjValue = objFct.calculateObjectiveValue(districts);
    }

    private void initSeeds(int numSeeds) {
        if (numSeeds < 2) {
            throw new IllegalArgumentException("Too few district seeds: " + numSeeds);
        }
        if (numSeeds > state.getDistricts().size()*2) {
            throw new IllegalArgumentException("Too many district seeds: " + numSeeds);
        }

        districts = new LinkedHashSet<>();

        while (numSeeds > 0) {
            for (District origDist : state.getDistricts()) {
                int id = numSeeds;
                District district = new District(id);

                Precinct seed = null;
                boolean badSeed = true;
                while (badSeed) {
                    seed = random.select(origDist.getPrecincts());
                    badSeed = districts.contains(seed.getDistrict());
                    for (Precinct neighbor : seed.getNeighbors().keySet()) {
                        if (districts.contains(neighbor.getDistrict())) {
                            badSeed = true;
                            break;
                        }
                    }
                }

                seed.setDistrictId(id);
                seed.setDistrict(district);
                district.addPrecinct(seed);
                precinctPool.removePrecinct(seed);

                districts.add(district);
                numSeeds--;
                if (numSeeds <= 0) {
                    break;
                }
            }
        }
    }

    @Override
    public Set<District> getDistricts() {
        // Consider returning the precinct pool as well?
        return districts;
    }

    @Override
    public Deque<Move> generateMoves() {
        Deque<Move> moves = new LinkedList<>();

        List<District> sortedPopDists = districts.stream()
                .sorted(Comparator.comparing(District::getPopulation))
                .collect(Collectors.toList());

        for (District districtToGrow : sortedPopDists) {
            Set<Precinct> borders = districtToGrow.getBorderPrecincts();
            borders.stream()
                    .flatMap(p -> p.getNeighbors().keySet().stream())
                    .filter(p -> precinctPool.getPrecincts().contains(p))
                    .forEach(precinctToAdd -> {
                        Move move = new Move();
                        move.setSourceDistrict(precinctPool);
                        move.setDestinationDistrict(districtToGrow);
                        move.setPrecinct(precinctToAdd);
                        moves.add(move);
                    });
            if (moves.size() > MAX_MOVE_POOL_SIZE) {
                return moves;
            }
        }
        return moves;
    }

    @Override
    public boolean isComplete() {
        return precinctPool.getPrecincts().isEmpty();
    }
}
