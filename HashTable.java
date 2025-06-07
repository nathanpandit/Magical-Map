public class HashTable<Key extends Tuple<Integer, Integer>, Value extends Node> {
    private static class Entry<Key, Value> {
        final Key key;
        final Value value;

        Entry(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
    }

    private final Entry<Key, Value>[] table;
    private final int size;

    @SuppressWarnings("unchecked")
    public HashTable(int size) {
        this.size = size;
        this.table = (Entry<Key, Value>[]) new Entry[size];
    }

    private int getNode(int x, int y) {
        return ((x+y)*(x+y+1)/2)+y; // i used cantor's pairing function to ensure bijection between N^2 and N.
    }
    //gets the index of the node by applying cantor's pairing function

    public void put(Key key, Value value) {
        int index = getNode(key.first, key.second);

        if (table[index] != null && !table[index].key.equals(key)) {
            throw new IllegalStateException("Make sure there's a bijection.");
        }
        table[index] = new Entry<>(key, value);
    }
    //puts the given node into the table according to its pairing function.

    public Value getNodeAt(int x, int y) {
        int index = getNode(x,y);
        Entry<Key, Value> entry = table[index];
        if (entry == null) {
            return null; // Key not found
        }
        return entry.value;
    }
    //gets the node from the table according to its position

    public void addEdge(Key key1, Key key2, Float weight) {
        Node node1 = getNodeAt(key1.first,key1.second);
        Node node2 = getNodeAt(key2.first,key2.second);
        if(key1.first < key2.first) { //key1 is to the left of key2
            node2.adjacencyList[0] = new Tuple<Node,Float>(node1,weight);
            node1.adjacencyList[1] = new Tuple<Node,Float>(node2,weight);
        }
        else if(key2.first < key1.first) { //key1 is to the right of key2
            node2.adjacencyList[1] = new Tuple<Node,Float>(node1,weight);
            node1.adjacencyList[0] = new Tuple<Node,Float>(node2,weight);
        }
        else if(key1.second < key2.second) { //key1 is below key2
            node2.adjacencyList[3] = new Tuple<Node,Float>(node1,weight);
            node1.adjacencyList[2] = new Tuple<Node,Float>(node2,weight);
        }
        else if(key2.second < key1.second) { //key1 is above key2
            node2.adjacencyList[2] = new Tuple<Node,Float>(node1,weight);
            node1.adjacencyList[3] = new Tuple<Node,Float>(node2,weight);
        }
    }
    //Adds an edge between the two keys with the given weight.
    //An edge is a tuple of the form (Node, weight).
    //adjacencyList indices indicate the following:
    // 0 = left, 1 = right, 2 = up, 3 = down
}