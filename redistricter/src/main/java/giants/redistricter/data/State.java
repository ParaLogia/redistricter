package giants.redistricter.data;

import java.util.Collection;

public class State {
    String name;
    Integer id;
    Collection<District> districts;
    Collection<Precinct> precincts;
    Integer population;
    Collection<ElectionResult> votes;

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

    public Collection<District> getDistricts() {
        return districts;
    }

    public void setDistricts(Collection<District> districts) {
        this.districts = districts;
    }

    public Collection<Precinct> getPrecincts() {
        return precincts;
    }

    public void setPrecincts(Collection<Precinct> precincts) {
        this.precincts = precincts;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Collection<ElectionResult> getVotes() {
        return votes;
    }

    public void setVotes(Collection<ElectionResult> votes) {
        this.votes = votes;
    }
}
