package giants.redistricter.data;

import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "STATE")
public class State {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator = "STATE_ID")
    @SequenceGenerator(name = "STATE_ID", sequenceName = "STATE_ID")
    @Column(name = "STATE_ID")
    Integer stateId;
    
    @Column(name = "NAME")
    String name;
    
    @Column(name = "SHORT_NAME")
    String shortName;
    
    @Column(name = "CONSTITUTION_TEXT")
    String constitutionText;
    
    @Transient
    Set<District> districts;  
    @Transient
    Set<Precinct> precincts; 
    @Transient
    Integer population;
    @Transient
    Map<Party, Integer> votes;

    public String getShortName() {
        return this.shortName;
    }
    
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    

    public String getConstitutionText() {
        return constitutionText;
    }

    public void setConstitutionText(String constitutionText) {
        this.constitutionText = constitutionText;
    }

    public Integer getStateId() {
        return stateId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setStateId(Integer id) {
        this.stateId = stateId;
    }

    public Set<District> getDistricts() {
        return districts;
    }

    public void setDistricts(Set<District> districts) {
        this.districts = districts;
    }

    public Set<Precinct> getPrecincts() {
        return precincts;
    }

    public void setPrecincts(Set<Precinct> precincts) {
        this.precincts = precincts;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Map<Party, Integer> getVotes() {
        return votes;
    }

    public void setVotes(Map<Party, Integer> votes) {
        this.votes = votes;
    }
}
