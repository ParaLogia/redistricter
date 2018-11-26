package giants.redistricter.algorithm;

import giants.redistricter.data.District;

import java.util.Collection;

public interface AlgorithmStrategy {
    Collection<District> getStatus();
    Move generateMove();
    void executeMove(Move move);
    public boolean isAcceptable(double objectiveValue);
    void acceptMove(Move move);
    void revertMove(Move move);
    Boolean isComplete(Double objValue);
}
