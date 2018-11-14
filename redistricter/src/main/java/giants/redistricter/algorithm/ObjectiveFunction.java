package giants.redistricter.algorithm;

import giants.redistricter.data.District;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class ObjectiveFunction {
    Map<ObjectiveCriteria, Double> weights;
    Map<ObjectiveCriteria,Function<Collection<District>,Double>> functions;

    public Double calculateObjectiveValue(Collection<District> districts){
        return null;
    }
    private Double calculatePolsbyPopper(Collection<District> districts){
        return null;
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
