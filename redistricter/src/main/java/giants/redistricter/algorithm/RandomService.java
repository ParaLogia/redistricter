package giants.redistricter.algorithm;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Random;

@Component
@Scope(value="session")
public class RandomService extends Random {

    /* Note that the time complexity may be up to O(n). */
    public <E> E select(Collection<E> collection) {
        int index = nextInt(collection.size());

        if (collection instanceof List) {
            return ((List<E>) collection).get(index);
        }
        else {
            return collection.stream()
                    .skip(index)
                    .findFirst()
                    .get();
        }
    }
}
