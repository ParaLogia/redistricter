package giants.redistricter.data;

import java.util.Map;

public class ElectionResult {
    Integer year;
    Integer precinctId;
    Map<Party,Integer> results;

    public Integer getYear(){
        return year;
    }
    public Integer getPrecinctId(){
        return precinctId;
    }

    public Map<Party, Integer> getResults() {
        return results;
    }
}
