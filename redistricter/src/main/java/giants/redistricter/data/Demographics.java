package giants.redistricter.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "DEMOGRAPHIC")
@IdClass(DemographicsPk.class)
public class Demographics implements Serializable{
    @Id
    @Column(name= "PRECINCT_ID")
    private int precinctId;
    @Id
    @Column(name = "DEMOGRAPHIC")
    private String demographic;
    @Column(name = "VALUE")
    private int value;
    
    public Demographics() {
        
    }
    
    public int getPrecinctId() {
        return precinctId;
    }
    public void setPrecinctId(int precinctId) {
        this.precinctId = precinctId;
    }
    public String getDemographic() {
        return demographic;
    }
    public void setDemographic(String demogrphic) {
        this.demographic = demogrphic;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    
    

}
