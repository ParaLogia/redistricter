package giants.redistricter.data;

import java.io.Serializable;

public class DemographicsPk implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected String demographic;
    protected Integer value;
    
    public DemographicsPk() {
        
    }
    
    public DemographicsPk(String demographic, Integer value) {
        this.demographic = demographic;
        this.value = value;
    }

}
