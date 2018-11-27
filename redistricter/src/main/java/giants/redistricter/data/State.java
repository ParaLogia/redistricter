package giants.redistricter.data;

import java.util.Set;

public class State {
    String name;
    Integer id;
    Set<District> districts;
    Set<Precinct> precincts;
    Integer population;
    Set<ElectionResult> votes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<District> getDistricts() {
        return districts;
    }

    public void setDistricts(Set<District> districts) {
        this.districts = districts;
    }

    public Set<Precinct> getPrecincts() {
        return precincts;
    }

    public void setPrecincts(Set<Precinct> precincts) {
        this.precincts = precincts;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Set<ElectionResult> getVotes() {
        return votes;
    }

    public void setVotes(Set<ElectionResult> votes) {
        this.votes = votes;
    }
}
