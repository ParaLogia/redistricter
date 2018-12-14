package giants.redistricter.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_PREFERENCES")
public class UserPreferences {
    
    @Id
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "MAXIMUM_DIFFERENCE")
    private Double maximumDifference;
    @Column(name = "MINIMUM_DIFFERENCE")
    private Double minimumDifference;
    @Column(name = "EFFICIENCY_GAP")
    private Double efficiencyGap;
    @Column(name = "PROPORTIONALITY")
    private Double proportionality;
    @Column(name = "MEAN_MEDIAN_DIFFERENCE")
    private Double meanMedianDifference;
    @Column(name = "POLSBY_POPPER")
    private Double polsbyPopper;
    @Column(name = "SHWARTZBERG")
    private Double shwartzberg;
    
    public UserPreferences(){    
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getMaximumDifference() {
        return maximumDifference;
    }

    public void setMaximumDifference(Double maximumDifference) {
        this.maximumDifference = maximumDifference;
    }

    public Double getMinimumDifference() {
        return minimumDifference;
    }

    public void setMinimumDifference(Double minimumDifference) {
        this.minimumDifference = minimumDifference;
    }

    public Double getEfficiencyGap() {
        return efficiencyGap;
    }

    public void setEfficiencyGap(Double efficiencyGap) {
        this.efficiencyGap = efficiencyGap;
    }

    public Double getProportionality() {
        return proportionality;
    }

    public void setProportionality(Double proportionality) {
        this.proportionality = proportionality;
    }

    public Double getMeanMedianDifference() {
        return meanMedianDifference;
    }

    public void setMeanMedianDifference(Double meanMedianDifference) {
        this.meanMedianDifference = meanMedianDifference;
    }

    public Double getPolsbyPopper() {
        return polsbyPopper;
    }

    public void setPolsbyPopper(Double polsbyPopper) {
        this.polsbyPopper = polsbyPopper;
    }

    public Double getShwartzberg() {
        return shwartzberg;
    }

    public void setShwartzberg(Double shwartzberg) {
        this.shwartzberg = shwartzberg;
    }
    
}