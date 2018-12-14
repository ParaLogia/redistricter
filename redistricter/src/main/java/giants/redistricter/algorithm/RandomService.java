package giants.redistricter.algorithm;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

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

    /* Note that the time complexity may be up to O(n). */
    public <E> List<E> sample(Collection<E> collection, int sampleSize) {
        int collectionSize = collection.size();
        if (sampleSize > collectionSize) {
            throw new IllegalArgumentException("Sample size greater than collection size: "+ sampleSize);
        }

        boolean complement = sampleSize*2 > collectionSize;
        if (complement) {
            sampleSize = collectionSize-sampleSize;
        }
        Set<Integer> indices = new HashSet<>();
        while (indices.size() < sampleSize) {
            indices.add(nextInt(collectionSize));
        }

        Iterator<E> iterator = collection.iterator();
        List<E> sample = new ArrayList<>();
        for (int i = 0; i < collectionSize; i++) {
            E next = iterator.next();
            if (complement ^ indices.contains(i)) {
                sample.add(next);
                if (sample.size() >= sampleSize) {
                    break;
                }
            }
        }

        return sample;
    }
}
