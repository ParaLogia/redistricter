package giants.redistricter.data;

import giants.redistricter.data.District;
import giants.redistricter.data.State;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;


@Repository
public class StateLoaderService {
    private Map<Integer,State> states;

    // TODO decide whether to use integer IDs or Strings
    public State getState(String name) {
        State state = new State();
        state.setName(name);

        //FIXME temp
        Collection<District> dists = new HashSet<>();
        dists.add(new District(1));
        dists.add(new District(2));
        state.setDistricts(dists);

        return state;
    }
}
