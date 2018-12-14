package giants.redistricter.data;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "PRECINCT")
public class Precinct {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator = "PRECINCT_ID")
    @SequenceGenerator(name = "PRECINCT_ID", sequenceName = "PRECINCT_ID")

    @Column(name = "PRECINCT_ID")
    private Integer id;

    @Column(name = "DISTRICT_ID")
    private Integer districtId;
    
    @Column(name = "POPULATION")
    private int population;
    @Column(name = "PERIMETER")
    private double perimeter;
    @Column(name = "AREA")
    private double area;

    @Transient
    private District district;
    @Transient
    private  Map<Precinct,Border> neighbors;
    @Transient
    private Map<Party, Integer> votes;
    @Transient
    private Map<Demographic,Integer> demographics;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getDistrictId() {
        return districtId;
    }
    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }
    public int getPopulation() {
        return population;
    }
    public void setPopulation(int population) {
        this.population = population;
    }
    public double getPerimeter() {
        return perimeter;
    }
    public void setPerimeter(double perimeter) {
        this.perimeter = perimeter;
    }
    public double getArea() {
        return area;
    }
    public void setArea(double area) {
        this.area = area;
    }
    public District getDistrict() {
        return district;
    }
    public void setDistrict(District district) {
        this.district = district;
    }
    public Map<Precinct, Border> getNeighbors() {
        return neighbors;
    }
    public void setNeighbors(Map<Precinct, Border> neighbors) {
        this.neighbors = neighbors;
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
