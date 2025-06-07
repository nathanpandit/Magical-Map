public class Node {

    int x, y, p;
    Tuple<Integer, Integer> coordinate;
    Tuple<Node, Float>[] adjacencyList;
    Node parent = null;
    public double f = Double.MAX_VALUE;
    public double g = Double.MAX_VALUE;
    public double mostRecentG;
    int id;
    public double h = 0;
    public boolean isRevealed;

    public Node(int x, int y, int p) {
        this.x = x;
        this.y = y;
        this.coordinate = new Tuple(x, y);
        this.p = p;
        this.adjacencyList = new Tuple[4];
        this.id = ((x + y) * (x + y + 1) / 2) + y;
        this.isRevealed = p == 0 || p == 1;
        this.mostRecentG = this.g;
    }

    public int compareTo(Node other) {
        if (this.f > other.f) {
            return 1;
        } else if (this.f < other.f) {
            return -1;
        } else if (this.g < other.g) {
            return -1;
        } else if (this.g > other.g) {
            return 1;
        }
        return 0;
    }
    //compares two nodes according to their f & g values.
    // (f and h became useless after i bailed A* but i still wanted to keep them because i spent a lot of time
    // with them :/ ).
}