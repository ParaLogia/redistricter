package giants.redistricter.algorithm;

import giants.redistricter.data.Border;
import giants.redistricter.data.District;
import giants.redistricter.data.Precinct;

public class Move {
    Precinct precinct;
    District sourceDistrict;
    District destinationDistrict;
    Border border;

    public void log(){
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

    public Border getBorder() {
        return border;
    }

    public void setBorder(Border border) {
        this.border = border;
    }
}
