package giants.redistricter.algorithm;

import java.util.LinkedHashMap;
import java.util.Map;

public class ObjectiveFunctionBuilder {
    Map<ObjectiveCriteria,Double> weights = new LinkedHashMap<>();
    ObjectiveFunction objFct;
    Integer year;

    public ObjectiveFunctionBuilder addWeight(ObjectiveCriteria obj, Double weight){
        weights.put(obj,weight);
        return this;
    }
    public ObjectiveFunctionBuilder setYear(Integer year){
        this.year = year;
        return this;
    }

    public ObjectiveFunction build(){
        if (weights.size() > 0){
            objFct = new ObjectiveFunction(weights,year);
            return objFct;
        } else {
            return null;
        }
    }
}
