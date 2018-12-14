package giants.redistricter.data;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import giants.redistricter.algorithm.ObjectiveCriteria;
import giants.redistricter.algorithm.ObjectiveFunction;
import giants.redistricter.algorithm.ObjectiveFunctionBuilder;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MockStateLoader {
    private final String PRECINCTS_FILE = "src/main/resources/data/%s_precincts_final.json";
    private final String NEIGHBORS_FILE = "src/main/resources/data/%s_neighbors.json";

    public static void main(String... args) {
        State state;
        state = new MockStateLoader().getStateByShortName("ny", 1998);

        ObjectiveFunction objective = new ObjectiveFunctionBuilder()
                .addWeight(ObjectiveCriteria.POLSBY_POPPER, 0.0)
                .addWeight(ObjectiveCriteria.POPULATION_FAIRNESS, 1.0)
                .build();

        for (int i = 0; i < 100; i++)
        System.out.println("Obj val: "+objective.calculateObjectiveValue(state.getDistricts()));
    }


    private Set<Precinct> loadPrecincts(String fileName, int year) {
        String precinctsJson;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            precinctsJson = br.lines().collect(Collectors.joining());
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        JacksonJsonParser parser = new JacksonJsonParser();
        Set<Precinct> precincts = new LinkedHashSet<>();
        for (Object o : parser.parseList(precinctsJson)) {
            Precinct precinct = new Precinct();
            Map attrs = (Map) o;
            precinct.setId((Integer) attrs.get("id"));
            precinct.setDistrictId((Integer) attrs.get("district"));
            precinct.setPopulation((Integer) attrs.get("population"));
            precinct.setArea((Double) attrs.get("area"));
            precinct.setPerimeter((Double) attrs.get("perimeter"));

            // TODO rethink yearly votes
            Map yearlyVotes = (Map) attrs.get("votes");
            String yearString = String.valueOf(year);
            Map<String, Integer> loadedVotes = (Map<String, Integer>) yearlyVotes.get(yearString);
            Map<Party, Integer> votes = loadedVotes
                    .keySet().stream()
                    .collect(Collectors.toMap(
                            Party::valueOf,
                            loadedVotes::get,
                            (a, b) -> a + b,
                            LinkedHashMap::new
                    ));

            precinct.setVotes(votes);

            Map<String, Integer> loadedDemographics = (Map<String, Integer>) attrs.get("demographics");
            Map<Demographic, Integer> demographics = loadedDemographics
                    .keySet().stream()
                    .collect(Collectors.toMap(
                            Demographic::valueOf,
                            loadedDemographics::get,
                            (a, b) -> a + b,
                            LinkedHashMap::new
                    ));

            precinct.setDemographics(demographics);
            System.out.println(demographics);

            precincts.add(precinct);
        }

        return precincts;
    }

    private void attachNeighbors(String fileName, Set<Precinct> precincts) {
        String neighborsJson;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            neighborsJson = br.lines().collect(Collectors.joining());
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        JacksonJsonParser parser = new JacksonJsonParser();
        Map neighbors = parser.parseMap(neighborsJson);

        Map<Integer, Precinct> precinctsFromId = precincts.stream()
                .collect(Collectors.toMap(Precinct::getId, p -> p));

        for (Precinct precinct : precincts) {
            String idString = String.valueOf(precinct.getId());
            Map<String, Double> loadedNeighbors = (Map<String, Double>) neighbors.get(idString);
            Map<Precinct, Border> precinctNeighbors = loadedNeighbors.keySet()
                    .stream()
                    .collect(Collectors.toMap(
                            id -> precinctsFromId.get(Integer.valueOf(id)),
                            id -> new Border(loadedNeighbors.get(id)),
                            (x, y) -> x,
                            LinkedHashMap::new
                        ));

            precinct.setNeighbors(precinctNeighbors);
        }
    }

    private Set<District> attachDistricts(Set<Precinct> precincts) {
        Map<Integer, District> distsFromId = new LinkedHashMap<>();

        for (Precinct precinct : precincts) {
            Integer distId = precinct.getDistrictId();
            District district = distsFromId.computeIfAbsent(distId, District::new);
            district.addPrecinct(precinct);
        }

        return new LinkedHashSet<>(distsFromId.values());
    }

    public State getStateByShortName(String shortName, int year) {
        String precinctsFile = String.format(PRECINCTS_FILE, shortName.toLowerCase());
        String neighborsFile = String.format(NEIGHBORS_FILE, shortName.toLowerCase());

        Set<Precinct> precincts = loadPrecincts(precinctsFile, year);
        attachNeighbors(neighborsFile, precincts);
        Set<District> districts = attachDistricts(precincts);

        for (District d : districts) {
            System.out.println("id: "+d.getDistrictId());
            System.out.println("demographics: "+d.getDemographics());
            System.out.println("votes: "+d.getVotes());
        }

        State state = new State();
        state.setName(shortName);
        state.setShortName(shortName);
        state.setPrecincts(precincts);
        state.setDistricts(districts);
        return state;
    }

    /* Create a mock state with precincts arranged in a square grid */
    public State loadMockState(String name, int width, int numDists) {

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
                Border border = new Border(1.0);
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
        assert !precinctIterator.hasNext() : "Unassigned Precincts in MockStateLoader.loadMockState";

        State state = new State();
        state.setName(name);
        state.setDistricts(districts);
        state.setPrecincts(precincts);

        return state;
    }


}
