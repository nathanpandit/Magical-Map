import java.util.Iterator;

public class HashSet<Type> implements Iterable<Type> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private LinkedList<Type>[] buckets;
    private int size;

    public HashSet() {
        buckets = new LinkedList[DEFAULT_CAPACITY];
        size = 0;
    } //constructor

    private int hash(Type element) {
        return (element == null) ? 0 : Math.abs(element.hashCode()) % buckets.length;
    }
    //simple hash function

    public boolean add(Type element) {
        if (contains(element)) {
            return false; // Avoid duplicates
        }

        int bucketIndex = hash(element);
        if (buckets[bucketIndex] == null) {
            buckets[bucketIndex] = new LinkedList<>();
        }

        buckets[bucketIndex].add(element);
        size++;

        if ((double) size / buckets.length > LOAD_FACTOR) {
            resize();
        }
        return true;
    }
    //adds the item to appropriate bucket.

    public boolean contains(Type element) {
        int bucketIndex = hash(element);
        if (buckets[bucketIndex] == null) {
            return false;
        }
        return buckets[bucketIndex].contains(element);
    }
    //checks if the bucket that element is supposed to be in contains the element.

    private void resize() {
        LinkedList<Type>[] oldBuckets = buckets;
        buckets = new LinkedList[oldBuckets.length * 2];
        size = 0;

        for (LinkedList<Type> bucket : oldBuckets) {
            if (bucket != null) {
                for (Type element : bucket) {
                    add(element);
                }
            }
        }
    }
    //classic resizing once load factor is exceeded.

    public int size() {
        return size;
    } //gets size

    @Override
    public Iterator<Type> iterator() {
        return new Iterator<Type>() {
            private int bucketIndex = 0;
            private Iterator<Type> bucketIterator = (buckets[0] == null) ? null : buckets[0].iterator();

            private void moveToNextBucket() {
                while ((bucketIterator == null || !bucketIterator.hasNext()) && bucketIndex < buckets.length - 1) {
                    bucketIndex++;
                    bucketIterator = (buckets[bucketIndex] == null) ? null : buckets[bucketIndex].iterator();
                }
            }

            @Override
            public boolean hasNext() {
                moveToNextBucket();
                return bucketIterator != null && bucketIterator.hasNext();
            }

            @Override
            public Type next() {
                moveToNextBucket();
                return bucketIterator.next();
            }
        };
    }
    //so that foreach is applicable.
}