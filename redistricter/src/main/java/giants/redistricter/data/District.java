package giants.redistricter.data;

import java.util.*;

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
        this.precincts = new TreeSet<>(Comparator.comparing(Precinct::getId));
        // TODO?
    }

    /**
     * Constructs a District by copying attributes from another District.
     * A new collection of precincts is created, with Precinct references retained.
     *
     * @param other the District whose attributes are to be copied into this one.
     */
    public District(District other){
        this.id = other.id;
        this.precincts = new TreeSet<>(other.getPrecincts());
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

    public Collection<Precinct> getBorderPrecincts() {
        return borderPrecincts;
    }

    public Integer getPopulation() {
        return population;
    }

    public Double getArea() {
        return area;
    }

    public Double getPerimeter() {
        return perimeter;
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

    /* Precondition: precinct must be in this.precincts */
    public void removePrecinct(Precinct precinct) {
        int population = precinct.getPopulation();
        double area = precinct.getArea();
        Map<Party, Integer> votes = precinct.getVotes();
        Map<Precinct, Border> neighbors = precinct.getNeighbors();
        boolean borderPrecinct = false;

        precincts.remove(precinct);
        this.population -= population;
        this.area -= area;
        votes.forEach((party, count) -> {
            this.votes.merge(party, count, (total, partial) -> total - partial);
        });
        for (Map.Entry<Precinct, Border> entry : neighbors.entrySet()) {
            Precinct neighbor = entry.getKey();
            Border border = entry.getValue();
            if (precincts.contains(neighbor)) {
                perimeter += border.getLength();
                if (!borderPrecincts.contains(neighbor)) {
                    borderPrecincts.add(neighbor);
                }
            } else {
                perimeter -= border.getLength();
                borderPrecinct = true;
            }
        }
        if (borderPrecinct) {
            borderPrecincts.remove(precinct);
        }
    }

    /* Precondition: precinct must not be in this.precincts */
    public void addPrecinct(Precinct precinct){
        int population = precinct.getPopulation();
        double area = precinct.getArea();
        Map<Party, Integer> votes = precinct.getVotes();
        Map<Precinct, Border> neighbors = precinct.getNeighbors();
        boolean borderPrecinct = false;

        precincts.add(precinct);
        this.population += population;
        this.area += area;
        votes.forEach((party, count) -> {
            this.votes.merge(party, count, (total, partial) -> total + partial);
        });
        for (Map.Entry<Precinct, Border> entry : neighbors.entrySet()) {
            Precinct neighbor = entry.getKey();
            Border border = entry.getValue();
            if (this.precincts.contains(neighbor)) {
                perimeter -= border.getLength();
                boolean removeBorder = neighbor.getNeighbors()
                        .keySet()
                        .stream()
                        .allMatch(precincts::contains);
                if (removeBorder) {
                    borderPrecincts.remove(neighbor);
                }
            } else {
                perimeter += border.getLength();
                borderPrecinct = true;
            }
        }
        if (borderPrecinct) {
            borderPrecincts.add(precinct);
        }
    }

    public void addPrecincts(Collection<Precinct> precincts){
        // Consider calculating borders lazily if performance issues occur
        for (Precinct precinct : precincts) {
            addPrecinct(precinct);
        }
    }
}
