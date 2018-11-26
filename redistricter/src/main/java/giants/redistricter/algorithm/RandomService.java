package giants.redistricter.algorithm;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RandomService {

    /* Note that the time complexity may be up to O(n). */
    public static <E> E select(Collection<E> collection, Random rand) {
        int index = rand.nextInt(collection.size());

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
