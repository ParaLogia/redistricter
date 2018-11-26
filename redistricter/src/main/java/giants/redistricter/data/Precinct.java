package giants.redistricter.data;

import java.util.Map;

public class Precinct {
    Integer id;
    District district;
    Integer population;
    Map<Precinct,Border> neighbors;
    Double area;
    Map<Party, Integer> votes;
    Map<Demographic,Integer> demographics;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Map<Precinct, Border> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(Map<Precinct, Border> neighbors) {
        this.neighbors = neighbors;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Map<Party, Integer> getVotes() {
        return votes;
    }

    public void setVotes(Map<Party, Integer> votes) {
        this.votes = votes;
    }

    public Map<Demographic, Integer> getDemographics() {
        return demographics;
    }

    public void setDemographics(Map<Demographic, Integer> demographics) {
        this.demographics = demographics;
    }
}
