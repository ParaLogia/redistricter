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
    @Column(name = "EQUAL_POP")
    private Double equalPop;
    @Column(name = "COMPACTNESS")
    private Double compactness;
    @Column(name = "EFFICIENCY_GAP")
    private Double efficiencyGap;
    @Column(name = "PARTISON_FAIRNESS")
    private Double partisonFairness;
    
    public UserPreferences(){    
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getEqualPop() {
        return equalPop;
    }

    public void setEqualPop(Double equalPop) {
        this.equalPop = equalPop;
    }

    public Double getCompactness() {
        return compactness;
    }

    public void setCompactness(Double compactness) {
        this.compactness = compactness;
    }

    public Double getEfficiencyGap() {
        return efficiencyGap;
    }

    public void setEfficiencyGap(Double efficiencyGap) {
        this.efficiencyGap = efficiencyGap;
    }

    public Double getPartisonFairness() {
        return partisonFairness;
    }

    public void setPartisonFairness(Double partisonFairness) {
        this.partisonFairness = partisonFairness;
    }
    
    
    
    
    
}
