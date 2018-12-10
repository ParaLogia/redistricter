package giants.redistricter.algorithm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import giants.redistricter.data.Border;
import giants.redistricter.data.District;
import giants.redistricter.data.Precinct;
import giants.redistricter.serialize.MoveSerializer;

@JsonSerialize(using = MoveSerializer.class)
public class Move {
    Precinct precinct;
    District sourceDistrict;
    District destinationDistrict;

    public void log(){
        String out = "{Precinct:" + precinct.getId() + ",from:" + sourceDistrict.getId() + ",to:" + destinationDistrict.getId() + "}";
        //no access to information regarding objective function.
        return;
    }

    public Precinct getPrecinct() {
        return precinct;
    }

    public void setPrecinct(Precinct precinct) {
        this.precinct = precinct;
    }

    public District getSourceDistrict() {
        return sourceDistrict;
    }

    public void setSourceDistrict(District sourceDistrict) {
        this.sourceDistrict = sourceDistrict;
    }

    public District getDestinationDistrict() {
        return destinationDistrict;
    }

    public void setDestinationDistrict(District destinationDistrict) {
        this.destinationDistrict = destinationDistrict;
    }
}
