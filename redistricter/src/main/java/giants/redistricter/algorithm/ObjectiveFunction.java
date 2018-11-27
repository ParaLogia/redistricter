package giants.redistricter.algorithm;

import giants.redistricter.data.District;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map;
import java.util.function.Function;
import static giants.redistricter.algorithm.ObjectiveCriteria.*;

public class ObjectiveFunction {
    private Map<ObjectiveCriteria, Double> weights;
    private Map<ObjectiveCriteria,Function<Set<District>,Double>> functions;

    public ObjectiveFunction(Map<ObjectiveCriteria, Double> weights) {
        this.weights = weights;
        functions = new LinkedHashMap<>();
        weights.forEach((obj,weight)->{
            switch(obj){
                case POLSBY_POPPER:
                    functions.put(POLSBY_POPPER,this::calculatePolsbyPopper); break;
                case REOCK:
                    functions.put(REOCK,this::calculateReock); break;
                case X_SYMMETRY:
                    functions.put(X_SYMMETRY,this::calculateXSymmetry); break;
                case SCHWARTZBERG:
                    functions.put(SCHWARTZBERG,this::calculateSchwartzberg); break;
                case SIGNIFICANT_CORNERS:
                    functions.put(SIGNIFICANT_CORNERS,this::calculateSignificantCorners); break;
                case BOYCE_CLARK:
                    functions.put(BOYCE_CLARK,this::calculateBoyceClark); break;
                case LENGTH_WIDTHS:
                    functions.put(LENGTH_WIDTHS,this::calculateLengthWidth); break;
                case POPULATION_FAIRNESS:
                    functions.put(POPULATION_FAIRNESS,this::calculatePopulationFairness); break;
                case EFFICIENCY_GAP:
                    functions.put(EFFICIENCY_GAP,this::calculateEfficiencyGap); break;
                case MEAN_MEDIAN:
                    functions.put(MEAN_MEDIAN,this::calculateMeanMedian); break;
                case PROPORTIONALITY:
                    functions.put(PROPORTIONALITY,this::calculateProportionality); break;
                default: //some error here
                    break;
            }
        });
    }

    public double calculateObjectiveValue(Set<District> districts){
        double overallVal = 0.0;

        for (Map.Entry<ObjectiveCriteria, Function<Set<District>, Double>> entry
                : functions.entrySet()) {
            ObjectiveCriteria criteria = entry.getKey();
            Function<Set<District>, Double> function = entry.getValue();
            double val = function.apply(districts);
            overallVal += weights.get(criteria) * val;
        }
        return overallVal;
    }

    private double calculatePolsbyPopper(Set<District> districts){
        double total = 0.;
        for (District dist:districts){
            double area = dist.getArea();
            double perimeter = dist.getPerimeter();
            double polsyPopper = (4*Math.PI*area)/(perimeter*perimeter);
            total = total + polsyPopper;
        }
        total = (total / districts.size());
        return total;
    }

    private double calculateSchwartzberg(Set<District> districts){
        return 0.0;
    }
    private double calculateReock(Set<District> districts){
        return 0.0;
    }
    private double calculateXSymmetry(Set<District> districts){
        return 0.0;
    }
    private double calculateSignificantCorners(Set<District> districts){
        return 0.0;
    }
    private double calculateBoyceClark(Set<District> districts){
        return 0.0;
    }
    private double calculateLengthWidth(Set<District> districts){
        return 0.0;
    }
    private double calculatePopulationFairness(Set<District> districts){
        return 0.0;
    }
    private double calculateEfficiencyGap(Set<District> districts){
        return 0.0;
    }
    private double calculateMeanMedian(Set<District> districts){
        return 0.0;
    }
    private double calculateProportionality(Set<District> districts){
        return 0.0;
    }
}
