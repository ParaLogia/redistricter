package giants.redistricter.algorithm;

import java.util.HashMap;
import java.util.Map;

public class ObjectiveFunctionBuilder {
    Map<ObjectiveCriteria,Double> weights = new HashMap<>();
    ObjectiveFunction objFct;

    public ObjectiveFunctionBuilder addWeight(ObjectiveCriteria obj, Double weight){
        weights.put(obj,weight);
        return this;
    }

    public ObjectiveFunction build(){
        if (weights.size() > 0){
            objFct = new ObjectiveFunction(weights);
            return objFct;
        } else {
            return null;
        }
    }
}
