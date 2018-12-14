package giants.redistricter.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "VOTES")
@IdClass(VotesPk.class)
public class Vote {
    @Id
    @Column(name = "PRECINCT_ID")
    private int precinctId;
    @Id
    @Column(name = "PARTY")
    private String party;
    @Id
    @Column(name = "year")
    private int year;
    @Column(name = "VOTES")
    private int votes;
    
    public Vote() {
        
    }

    public int getPrecinctId() {
        return precinctId;
    }

    public void setPrecinctId(int precinctId) {
        this.precinctId = precinctId;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
    
    

}
