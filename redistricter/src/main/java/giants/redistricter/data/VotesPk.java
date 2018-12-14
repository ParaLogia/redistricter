package giants.redistricter.data;

import java.io.Serializable;

public class VotesPk implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    protected int precinctId;
    protected String party;
    protected int year;
    
    public VotesPk() {
        
    }
    
    public VotesPk(int precinctId, String party, int year) {
        this.precinctId = precinctId;
        this.party = party;
        this.year = year;
    }
    

}
