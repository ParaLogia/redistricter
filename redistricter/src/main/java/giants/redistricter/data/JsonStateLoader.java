package giants.redistricter.data;

import giants.redistricter.algorithm.ObjectiveCriteria;
import giants.redistricter.algorithm.ObjectiveFunction;
import giants.redistricter.algorithm.ObjectiveFunctionBuilder;
import org.springframework.boot.json.JacksonJsonParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class JsonStateLoader {
    private final String PRECINCTS_FILE = "src/main/resources/data/%s_precincts_final.json";
    private final String NEIGHBORS_FILE = "src/main/resources/data/%s_neighbors.json";

    public static void main(String... args) {
        State state;
        state = new JsonStateLoader().getStateByShortName("ny", 1998);

        System.out.println("Name: " + state.getName());
        System.out.println("Votes: " + state.getVotes());

        ObjectiveFunction objective = new ObjectiveFunctionBuilder()
                .addWeight(ObjectiveCriteria.POLSBY_POPPER, 0.7)
                .addWeight(ObjectiveCriteria.POPULATION_FAIRNESS, 0.3)
                .build();
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

        Integer statePopulation = 0;
        Map<Party, Integer> stateVotes = new LinkedHashMap<>();
        Map<Demographic, Integer> stateDemographics = new LinkedHashMap<>();
        for (District d : districts) {
            Integer population = d.getPopulation();
            Map<Party, Integer> votes = d.getVotes();
            Map<Demographic, Integer> demographics = d.getDemographics();

            statePopulation += population;
            votes.forEach((party, count) -> {
                stateVotes.merge(party, count, (total, partial) -> total + partial);
            });
            demographics.forEach((demographic, count) -> {
                stateDemographics.merge(demographic, count, (total, partial) -> total + partial);
            });
        }

        final Map<String, String> STATE_NAMES = new HashMap<>();
        STATE_NAMES.put("NY", "New York");
        STATE_NAMES.put("NH", "New Hampshire");
        STATE_NAMES.put("CO", "Colorado");

        State state = new State();
        state.setName(shortName.toUpperCase());
        state.setShortName(STATE_NAMES.get(shortName.toUpperCase()));
        state.setPopulation(statePopulation);
        state.setVotes(stateVotes);
        state.setPrecincts(precincts);
        state.setDistricts(districts);
        return state;
    }
}
