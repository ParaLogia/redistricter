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
    double objectiveDelta;

    public void log(){
        String out = "{Precinct:" + precinct.getId() + ",from:" + sourceDistrict.getDistrictId() + ",to:" + destinationDistrict.getDistrictId() + "}";
        //no access to information regarding objective function.
        return;
    }

    public double getObjectiveDelta() {
        return objectiveDelta;
    }

    public void setObjectiveDelta(double objectiveDelta) {
        this.objectiveDelta = objectiveDelta;
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
