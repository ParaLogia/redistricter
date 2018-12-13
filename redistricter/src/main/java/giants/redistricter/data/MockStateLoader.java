package giants.redistricter.data;

import giants.redistricter.algorithm.ObjectiveCriteria;
import giants.redistricter.algorithm.ObjectiveFunction;
import giants.redistricter.algorithm.ObjectiveFunctionBuilder;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

@Repository
public class MockStateLoader {
    public static void main(String... args) {
        State state = new MockStateLoader().loadState("Mock", 24, 8);
        ObjectiveFunction objective = new ObjectiveFunctionBuilder()
                .addWeight(ObjectiveCriteria.POLSBY_POPPER, 0.3)
                .addWeight(ObjectiveCriteria.POPULATION_FAIRNESS, 0.7)
                .build();
        Set<District> districts = state.getDistricts();
//        districts.iterator().next().setPopulation(0);
        for (District d : districts) {
//            System.out.println("District " + d.getDistrictId());
//            System.out.println(d.getVotes());
            System.out.println("Population: " + d.getPopulation());
            System.out.println("Area: " + d.getArea());
            System.out.println("Perimeter: " + d.getPerimeter());
//            System.out.println("Interior Border Precincts: " + d.getBorderPrecincts().size());
            System.out.println("Precincts: " + d.getPrecincts().size());
        }
        System.out.println("Objective val: " + objective.calculateObjectiveValue(districts));
    }

    /* Create a mock state with precincts arranged in a square grid */
    public State loadState(String name, int width, int numDists) {

        Set<Precinct> precincts = new LinkedHashSet<>();
        Precinct[][] precArr = new Precinct[width][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                Precinct p = new Precinct();
                p.setId(i*width + j);
                p.setArea(1.0);
                p.setPerimeter(4.0);
                p.setPopulation(2*width+i+j);
                LinkedHashMap<Party, Integer> votes = new LinkedHashMap<>();
                votes.put(Party.DEMOCRAT, 2*width-i-j);
                votes.put(Party.REPUBLICAN, i+j);
                p.setVotes(votes);
                precArr[i][j] = p;
                precincts.add(p);
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                Precinct p = precArr[i][j];
                LinkedHashMap<Precinct, Border> neighbors = new LinkedHashMap<>();

                // Bad practice, but we'll assume Borders are immutable
                Border border = new Border();
                border.setLength(1.0);
                if (i > 0) {
                    neighbors.put(precArr[i-1][j], border);
                }
                if (i < width-1) {
                    neighbors.put(precArr[i+1][j], border);
                }
                border.setLength(1.0);
                if (j > 0) {
                    neighbors.put(precArr[i][j-1], border);
                }
                if (j < width-1) {
                    neighbors.put(precArr[i][j+1], border);
                }
                p.setNeighbors(neighbors);
            }
        }

        Set<District> districts = new LinkedHashSet<>();
        Iterator<Precinct> precinctIterator = precincts.iterator();
        for (int i = 1; i <= numDists; i++) {
            District d = new District(i);
            for (int j = 0; j < precincts.size()/numDists; j++) {
                Precinct p = precinctIterator.next();
                d.addPrecinct(p);
                p.setDistrict(d);
                p.setDistrictId(d.getDistrictId());
            }
            districts.add(d);
        }
        assert !precinctIterator.hasNext() : "Unassigned Precincts in MockStateLoader.loadState";

        State state = new State();
        state.setName(name);
        state.setDistricts(districts);
        state.setPrecincts(precincts);

        return state;
    }
}
