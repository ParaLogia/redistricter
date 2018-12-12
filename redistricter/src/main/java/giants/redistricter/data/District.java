package giants.redistricter.data;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "DISTRICT")
public class District {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator = "DISTRICT_ID")
    @SequenceGenerator(name = "DISTIRCT_ID", sequenceName = "DISTRICT_ID")
    
    @Column(name = "DISTRICT_ID")
    Integer districtId;
    
    @Column(name = "STATE_ID")
    Integer stateId;

    @Column(name = "GEO_DATA")
    String border;
    
    @Transient
    Integer population;
    @Transient
    Set<Precinct> precincts;
    @Transient
    Set<Precinct> borderPrecincts;
    @Transient
    Double area;
    @Transient
    Double perimeter;
    @Transient
    Map<Demographic,Integer> demographics;
    @Transient
    Map<Party,Integer> votes;

    public District() {
        this.precincts = new LinkedHashSet<>();
        this.borderPrecincts = new LinkedHashSet<>();
    }

    public District(Integer id){
        this();
        this.districtId = id;
    }

    /**
     * Constructs a District by copying attributes from another District.
     * A new set of precincts is created, with Precinct references retained.
     *
     * @param other the District whose attributes are to be copied into this one.
     */
    public District(District other){
        this.districtId = other.districtId;
        this.precincts = new LinkedHashSet<>(other.getPrecincts());
        this.borderPrecincts = new LinkedHashSet<>(other.getBorderPrecincts());
        this.border = other.border;
        this.population = other.population;
        this.area =  other.area;
        this.perimeter = other.perimeter;
        this.demographics = new HashMap<Demographic,Integer>(other.getDemographics());
        this.votes = new HashMap<Party,Integer>(other.getVotes());
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public void setPrecincts(Set<Precinct> precincts) {
        this.precincts = precincts;
    }

    public void setBorderPrecincts(Set<Precinct> borderPrecincts) {
        this.borderPrecincts = borderPrecincts;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public void setPerimeter(Double perimeter) {
        this.perimeter = perimeter;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
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
        perimeter -= precinct.getPerimeter();
        for (Map.Entry<Precinct, Border> entry : neighbors.entrySet()) {
            Precinct neighbor = entry.getKey();
            Border border = entry.getValue();
            if (precincts.contains(neighbor)) {
                perimeter += border.getLength();
                borderPrecincts.add(neighbor);
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
        perimeter += precinct.getPerimeter();
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

    /**
     * Checks if the current district is contiguous and not split up.
     * This method takes a precinct that was updated to reduce computation cost.
     * You can use this method before the move is conducted as a test if you want.
     * @param precinct the precinct that was most recently updated.
     * @return true if is contiguous, false if it is split into one or more regions
     */
    public Boolean isContiguousWithChange(Precinct precinct){
        if (!immediateAdjacency(precinct)){
            return contiguousGraph(precinct);
        } else {
            return false;
        }
    }
    private Boolean immediateAdjacency(Precinct precinct){
        //gets a set of adjacent precincts
        ArrayList<Precinct> adjacents = new ArrayList<>();
        precinct.getNeighbors().forEach((p,b) -> {
            if(p.getDistrict().getDistrictId().equals(this.districtId)){
                adjacents.add(p);
            }
        });

        boolean visited[] = new boolean[adjacents.size()];
        visited = miniPrecinctDFS(adjacents,adjacents.get(0),visited);
        boolean check = true;
        for (int i=0; i<visited.length;i++){
            if(!visited[i]){
                check = false;
            }
        }
        return check;
    }
    private boolean[] miniPrecinctDFS(ArrayList<Precinct> adjacents,Precinct precinct,boolean[] visited){
        //i hate this
        if (adjacents.contains(precinct)) {
            visited[adjacents.indexOf(precinct)] = true;
            precinct.getNeighbors().forEach((p, b) -> {
                if (p.getDistrict().getDistrictId().equals(this.districtId)) {
                    miniPrecinctDFS(adjacents, p, visited);
                }
            });
        }
        return visited;
    }
    private Boolean contiguousGraph(Precinct precinct){
        //gets a set of adjacent precincts
        //rip duplicate code
        //this check is going to be very slow
        ArrayList<Precinct> borderP = new ArrayList<>();
        borderP.addAll(borderPrecincts);

        boolean visited[] = new boolean[borderP.size()];
        visited = bigPrecinctDFS(borderP,borderP.get(0),visited);

        boolean check = true;
        for (int i=0; i<visited.length;i++){
            if(!visited[i]){
                check = false;
            }
        }
        return check;
    }
    private boolean[] bigPrecinctDFS(ArrayList<Precinct> borderP,Precinct precinct,boolean[] visited){
        //i think this works
        if (borderP.contains(precinct)) {
            visited[borderP.indexOf(precinct)] = true;
        }
        precinct.getNeighbors().forEach((p, b) -> {
            if (p.getDistrict().getDistrictId().equals(this.districtId)) {
                miniPrecinctDFS(borderP, p, visited);
            }
        });
        return visited;
    }
}
