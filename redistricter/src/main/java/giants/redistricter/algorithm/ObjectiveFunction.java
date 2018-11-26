package giants.redistricter.algorithm;

import giants.redistricter.data.District;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class ObjectiveFunction {
    Map<ObjectiveCriteria, Double> weights;
    Map<ObjectiveCriteria,Function<Collection<District>,Double>> functions;
    
    public ObjectiveFunction(Map<ObjectiveCriteria, Double> weights) {
        this.weights = weights;
        weights.forEach((obj,weight)->{
            switch(obj){
                case POLSBY_POPPER: functions.put(ObjectiveCriteria.POLSBY_POPPER,this::calculatePolsbyPopper); break;
                case REOCK: functions.put(ObjectiveCriteria.REOCK,this::calculateReock); break;
                case X_SYMMETRY: functions.put(ObjectiveCriteria.X_SYMMETRY,this::calculateXSymmetry); break;
                //etc.
                default: break;
            }
        });
    }

    public Double calculateObjectiveValue(Collection<District> districts){
        // Problems here to fix later.
//        Map<ObjectiveCriteria,Double> objVals;
//        functions.forEach((obj, fct) -> {
//            objVals.put(obj, fct.apply(obj);
//        });
//        Double overallVal = 0.;
//        objVals.forEach((obj,val) -> {overallVal += weights.get(obj)*val;});
//        return overallVal;
        return calculatePolsbyPopper(districts);
    }
    private Double calculatePolsbyPopper(Collection<District> districts){
        Double total = 0.;
        for (District dist:districts){
            Double area = dist.getArea();
            Double peri = dist.getPerimeter();
            Double polsyPopper = (4*Math.PI*area)/(peri*peri);
            total = total + polsyPopper;
        }
        total = (total / districts.size());
        return total;
    }
    private Double calculateSchwartzberg(Collection<District> districts){
        return null;
    }
    private Double calculateReock(Collection<District> districts){
        return null;
    }
    private Double calculateXSymmetry(Collection<District> districts){
        return null;
    }
    private Double calculateSignificantCorners(Collection<District> districts){
        return null;
    }
    private Double calculateBoyceClark(Collection<District> districts){
        return null;
    }
    private Double calculateLengthWidth(Collection<District> districts){
        return null;
    }
    private Double calculatePopulationFairness(Collection<District> districts){
        return null;
    }
    private Double calculateEfficiencyGap(Collection<District> districts){
        return null;
    }
    private Double calculateMeanMedian(Collection<District> districts){
        return null;
    }
    private Double calculateProportionality(Collection<District> districts){
        return null;
    }
}
