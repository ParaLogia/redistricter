package giants.redistricter.algorithm;

import giants.redistricter.data.District;
import giants.redistricter.data.Party;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map;
import java.util.function.BiConsumer;
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
                default: //default to polsby_popper in case something is wrong.
                    functions.put(POLSBY_POPPER,this::calculatePolsbyPopper);
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
        double total = 0.0;
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
        //modified polsbypopper?
        return 0.0;
    }
    private double calculateReock(Set<District> districts){
        //the ratio of the area of the district to the area of a minimum bounding circle
        return 0.0;
    }
    private double calculateXSymmetry(Set<District> districts){
        //dividing the overlapping area, between a district and its reflection across the horizontal axis, by the area of the original district
        return 0.0;
    }
    private double calculateSignificantCorners(Set<District> districts){
        //count the number of “significant” corners. The more significant corners, the less compact the district by this metric.
        //we have to define significant corners and normalize it.
        return 0.0;
    }
    private double calculateBoyceClark(Set<District> districts){
        // the(normalized) mean absolute deviation in the radial lines from the centroid of the district to its vertices
        return 0.0;
    }
    private double calculateLengthWidth(Set<District> districts){
        //Minimum bounding rectangle
        return 0.0;
    }
    private double calculatePopulationFairness(Set<District> districts){
        //no access to state so extra code.
        int populationPerDistrict;
        int totalPopulation = 0;
        double total = 0.0;
        for (District district:districts) {
            totalPopulation += district.getPopulation();
        }
        populationPerDistrict = totalPopulation/districts.size();
        for (District district:districts){
            total += Math.abs((populationPerDistrict - district.getPopulation()));
        }
        total = 1.0 - total/totalPopulation;
        return total;
    }
    private double calculateEfficiencyGap(Set<District> districts){
        //based on wasted votes.
        //need access to the votes from each district.
        double total = 0.0;
        for (District d:districts) {
            int winningPartyVotes = 0;
            for (Map.Entry<Party,Integer> entry: d.getVotes().entrySet()){
                if (winningPartyVotes < entry.getValue()){
                    winningPartyVotes = entry.getValue();
                }
            }
            int totalPopulation = d.getPopulation();
            double tempTotal;
            tempTotal = Math.abs(winningPartyVotes - (totalPopulation/2));
            tempTotal += totalPopulation - winningPartyVotes;
            tempTotal = tempTotal/totalPopulation;
            total += 1.0 - tempTotal;
        }
        total = total/districts.size();
        //higher is better score.
        return total;
    }
    private double partisanFairness(Set<District> districts){
        //what the hell does the teacher want.
        return 0.0;
    }
    private double calculateMeanMedian(Set<District> districts){
        return 0.0;
    }
    private double calculateProportionality(Set<District> districts){
        //this test should be done on the entire state for accuracy. This should not be run on a single state.
        if (districts.size() == 1){
            return 0.0;
        }
        double repVotes = 0;
        double demoVotes = 0;
        double repWins = 0;
        double demoWins = 0;
        for (District d: districts){
            Map.Entry<Party,Integer> highestEntry = d.getVotes().entrySet().iterator().next();
            for(Map.Entry<Party,Integer> entry: d.getVotes().entrySet()){
                if (entry.getValue() > highestEntry.getValue()){
                    highestEntry = entry;
                }
                if (entry.getKey().equals(Party.REPUBLICAN)){
                    repVotes += entry.getValue();
                } else if (entry.getKey().equals(Party.DEMOCRAT)){
                    demoVotes += entry.getValue();
                }
            }
            if (highestEntry.getKey().equals(Party.DEMOCRAT)){
                demoWins++;
            } else if (highestEntry.getKey().equals(Party.REPUBLICAN)){
                repWins++;
            }
        }
        double totalVotes = repVotes + demoVotes;
        double demoWinStat = demoWins/districts.size();
        double demoVotesStat = demoVotes/totalVotes;
        double repWinStat = repWins/districts.size();
        double repVotesStat = repVotes/totalVotes;

        double out = Math.abs(demoWinStat-demoVotesStat);
        out += Math.abs(repWinStat-repVotesStat);
        out = 1-out;
        return out;
    }
}
