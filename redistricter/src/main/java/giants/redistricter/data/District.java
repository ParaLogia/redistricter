package giants.redistricter.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class District {
    Integer id;
    Collection<Precinct> precincts;
    Collection<Precinct> borderPrecincts;
    Integer population;
    Double area;
    Double perimeter;
    Map<Demographic,Integer> demographics;
    Map<Party,Integer> votes;


    public District(Integer id){
        this.id = id;
        this.precincts = new HashSet<>();
        // TODO?
    }

    public District(District dist){
        this.id = dist.id;
        this.precincts = new HashSet<>(dist.precincts);
        // TODO
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Collection<Precinct> getPrecincts() {
        return precincts;
    }

    public void setPrecincts(Collection<Precinct> precincts) {
        this.precincts = precincts;
    }

    public Collection<Precinct> getBorderPrecincts() {
        return borderPrecincts;
    }

    public void setBorderPrecincts(Collection<Precinct> borderPrecincts) {
        this.borderPrecincts = borderPrecincts;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Double getPerimeter() {
        return perimeter;
    }

    public void setPerimeter(Double perimeter) {
        this.perimeter = perimeter;
    }

    public Map<Demographic, Integer> getDemographics() {
        return demographics;
    }

    public void setDemographics(Map<Demographic, Integer> demographics) {
        this.demographics = demographics;
    }

    public Map<Party, Integer> getVotes() {
        return votes;
    }

    public void setVotes(Map<Party, Integer> votes) {
        this.votes = votes;
    }

    public void removePrecinct(Precinct p) {
        return;
    }
    public void addPrecinct(Precinct p){
        return;
    }
    public void addPrecincts(Collection<Precinct> ps){
        return;
    }
}
