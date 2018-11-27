package giants.redistricter.algorithm;

import giants.redistricter.data.District;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map;
import java.util.function.Function;

public class ObjectiveFunction {
    private Map<ObjectiveCriteria, Double> weights;
    private Map<ObjectiveCriteria,Function<Set<District>,Double>> functions;
    
    public ObjectiveFunction(Map<ObjectiveCriteria, Double> weights) {
        this.weights = weights;
        functions = new LinkedHashMap<>();
        weights.forEach((obj,weight)->{
            switch(obj){
                case POLSBY_POPPER: functions.put(ObjectiveCriteria.POLSBY_POPPER,this::calculatePolsbyPopper); break;
                case REOCK: functions.put(ObjectiveCriteria.REOCK,this::calculateReock); break;
                case X_SYMMETRY: functions.put(ObjectiveCriteria.X_SYMMETRY,this::calculateXSymmetry); break;
                case SCHWARTZBERG: functions.put(ObectiveCriteria.SCHWARTZBERG,this::calculateSchwartzberg); break;
                case SIGNIFICANT_CORNERS: functions.put(ObjectiveCriteria.SIGNIFICANT_CORNERS,this::calculateSignificantCorners); break;
                case BOYCE_CLARK: functions.put(ObjectiveCriteria.BOYCE_CLARK,this::calculateBoyceClark); break;
                case LENGTH_WIDTHS: functions.put(ObjectiveCriteria.LENGTH_WIDTHS,this::calculateLengthWidth); break;
                case POPULATION_FAIRNESS: functions.put(ObjectiveCriteria.POPULATION_FAIRNESS,this::calculatePopulationFairness); break;
                case EFFICIENCY_GAP: functions.put(ObjectiveCriteria.EFFICIENCY_GAP,this::calculateEfficiencyGap); break;
                case MEAN_MEDIAN: functions.put(ObjectiveCriteria.MEAN_MEDIAN,this::calculateMeanMedian); break;
                case PROPORTIONALITY: functions.put(ObjectiveCriteria.PROPORTIONALITY,this::calculateProportionality); break;
                default: //some error here
                    break;
            }
        });
    }

    public Double calculateObjectiveValue(Set<District> districts){
        Double overallVal = 0.0;

        for (Map.Entry<ObjectiveCriteria, Function<Set<District>, Double>> entry
                : functions.entrySet()) {
            ObjectiveCriteria criteria = entry.getKey();
            Function<Set<District>, Double> function = entry.getValue();
            Double val = function.apply(districts);
            overallVal += weights.get(criteria) * val;
        }
        return overallVal;
    }

    private Double calculatePolsbyPopper(Set<District> districts){
        Double total = 0.;
        for (District dist:districts){
            Double area = dist.getArea();
            Double perimeter = dist.getPerimeter();
            Double polsyPopper = (4*Math.PI*area)/(perimeter*perimeter);
            total = total + polsyPopper;
        }
        total = (total / districts.size());
        return total;
    }

    private Double calculateSchwartzberg(Set<District> districts){
        return null;
    }
    private Double calculateReock(Set<District> districts){
        return null;
    }
    private Double calculateXSymmetry(Set<District> districts){
        return null;
    }
    private Double calculateSignificantCorners(Set<District> districts){
        return null;
    }
    private Double calculateBoyceClark(Set<District> districts){
        return null;
    }
    private Double calculateLengthWidth(Set<District> districts){
        return null;
    }
    private Double calculatePopulationFairness(Set<District> districts){
        return null;
    }
    private Double calculateEfficiencyGap(Set<District> districts){
        return null;
    }
    private Double calculateMeanMedian(Set<District> districts){
        return null;
    }
    private Double calculateProportionality(Set<District> districts){
        return null;
    }
}
