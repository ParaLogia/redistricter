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
    Integer id;
    
    @Column(name = "DISTRICT_ID")
    Integer districtId;
    
    @Column(name = "GEO_DATA")
    String border;
    
    @Column(name = "CENTER_POINT")
    String centerPoint;
    
    @Transient
    District district;
    @Transient
    Integer population;
    @Transient
    Map<Precinct,Border> neighbors;
    @Transient
    Double area;
    @Transient
    Map<Party, Integer> votes;
    @Transient
    Map<Demographic,Integer> demographics;

    
    
    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(String centerPoint) {
        this.centerPoint = centerPoint;
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
