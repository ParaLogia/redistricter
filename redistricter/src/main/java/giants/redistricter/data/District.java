package giants.redistricter.data;

import java.util.*;

public class District {
    Integer id;
    Set<Precinct> precincts;
    Set<Precinct> borderPrecincts;
    String border;
    Integer population;
    Double area;
    Double perimeter;
    Map<Demographic,Integer> demographics;
    Map<Party,Integer> votes;

    public District() {
        this.precincts = new LinkedHashSet<>();
        this.borderPrecincts = new LinkedHashSet<>();
    }

    public District(Integer id){
        this();
        this.id = id;
    }

    /**
     * Constructs a District by copying attributes from another District.
     * A new set of precincts is created, with Precinct references retained.
     *
     * @param other the District whose attributes are to be copied into this one.
     */
    public District(District other){
        this.id = other.id;
        this.precincts = new LinkedHashSet<>(other.getPrecincts());
        this.borderPrecincts = new LinkedHashSet<>(other.getBorderPrecincts());
        this.border = other.border;
        this.population = other.population;
        this.area =  other.area;
        this.perimeter = other.perimeter;
        this.demographics = new Map<Demographic,Integer>(other.getDemographics());
        this.votes = new Map<Party,Integer>(other.getVotes());
    }


    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<Precinct> getPrecincts() {
        return precincts;
    }

    public Set<Precinct> getBorderPrecincts() {
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
                borderPrecincts.add(neighbor);
            } else {
                perimeter -= border.getLength();
            }
        }
        borderPrecincts.remove(precinct);
    }

    /* Precondition: precinct must not be in this.precincts */
    public void addPrecinct(Precinct precinct){
        int population = precinct.getPopulation();
        double area = precinct.getArea();
        Map<Party, Integer> votes = precinct.getVotes();
        Map<Precinct, Border> neighbors = precinct.getNeighbors();
        boolean borderPrecinct = false;
        //don't add for remove, or it might break some other code. Just becareful.
        precinct.setDistrict(this);

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
                borderPrecincts.add(precinct);
            }
        }
    }

    public void addPrecincts(Collection<Precinct> precincts){
        // Consider calculating borders lazily if performance issues occur
        for (Precinct precinct : precincts) {
            addPrecinct(precinct);
        }
    }
}
