public class HashMap<Key, Value> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private LinkedList<Entry<Key, Value>>[] buckets;
    private int size;

    // entry class to store key-value pairs
    private static class Entry<Key, Value> {
        Key key;
        Value value;

        public Entry(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap() {
        buckets = new LinkedList[DEFAULT_CAPACITY];
        size = 0;
    }

    private int hash(Key key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % buckets.length;
    }

    public void put(Key key, Value value) {
        int bucketIndex = hash(key);

        if (buckets[bucketIndex] == null) {
            buckets[bucketIndex] = new LinkedList<>();
        }

        for (Entry<Key, Value> entry : buckets[bucketIndex]) {
            if ((key == null && entry.key == null) || (key != null && key.equals(entry.key))) {
                // updates value if key already exists
                entry.value = value;
                return;
            }
        }

        // adds new entry if key does not exist
        buckets[bucketIndex].add(new Entry<>(key, value));
        size++;

        // resizes if load factor is exceeded
        if ((double) size / buckets.length > LOAD_FACTOR) {
            resize();
        }
    }

    public Value get(Key key) {
        int bucketIndex = hash(key);

        if (buckets[bucketIndex] != null) {
            for (Entry<Key, Value> entry : buckets[bucketIndex]) {
                if ((key == null && entry.key == null) || (key != null && key.equals(entry.key))) {
                    return entry.value;
                }
            }
        }

        return null; // key doesnt exist
    }

    private void resize() {
        LinkedList<Entry<Key, Value>>[] oldBuckets = buckets;
        buckets = new LinkedList[oldBuckets.length * 2];
        size = 0;

        for (LinkedList<Entry<Key, Value>> bucket : oldBuckets) {
            if (bucket != null) {
                for (Entry<Key, Value> entry : bucket) {
                    put(entry.key, entry.value);
                }
            }
        }
    }
    //creates a map of size twice of the previous map and re-inserts every item.
}