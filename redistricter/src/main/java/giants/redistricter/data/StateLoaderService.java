package giants.redistricter.data;

import giants.redistricter.data.District;
import giants.redistricter.data.State;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public class StateLoaderService {
    private Map<Integer,State> states;

    int i;
    // TODO decide whether to use integer IDs or Strings
    public State getState(String name) {
        State state = new State();
        state.setName(name);

        Collection<District> dists = new TreeSet<>(Comparator.comparing(District::getId));
        state.setDistricts(dists);
        //FIXME temporary
        dists.add(new District(++i));
        dists.add(new District(++i));
        dists.add(new District(++i));
        dists.add(new District(++i));
        dists.add(new District(++i));

        return state;
    }
}
