import java.util.ArrayList;

public class Actions {

    static HashTable<Tuple<Integer, Integer>, Node> tableOfNodes;
    static int size;
    static int radius;
    static ArrayList<Integer> notWall = new ArrayList<>();


    public static ArrayList<Node> dijkstra(Node start, Node goal) {
        PriorityQueue<Node> pq = new PriorityQueue<>(size*size); //stores the nodes that will be discovered.
        HashMap<Node, Double> dist = new HashMap<>(); //stores store node-distance pairs.
        HashSet<Node> visited = new HashSet<>(); //keeps track of the nodes that were already visited.
        ArrayList<Node> path = new ArrayList<>(); //initialize path that will be returned at the end.

        // set all initial distances to infinity to be discovered later.
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                Node node = Actions.tableOfNodes.getNodeAt(i,j);
                dist.put(node, Double.POSITIVE_INFINITY);
            }
        }
        dist.put(start, 0.0); //sets distance of source to zero
        pq.add(start);

            Node currentNode = pq.poll();

            // exits when goal node is reached
            if (currentNode == goal) {
                Node node = goal;
                while(true) {
                    path.add(node);
                    if(node==start) break;
                    node = node.parent;
                }
                return path;
            }

            // skips nodes that are already visited
            if (visited.contains(currentNode)) continue;
            visited.add(currentNode);

            // checks for all four edges left,right,up,down of currentnode.
            //skips if edge doesnt exist, node is of color 1 or node that is revealed and still considered a wall, or node is already visited.
            for (Tuple<Node, Float> edge : currentNode.adjacencyList) {
                if(edge == null) continue;
                Node neighbor = edge.first;
                if(neighbor.p == 1) continue;
                if(neighbor.p > 0 && neighbor.isRevealed && !notWall.contains(neighbor.p)) continue;
                if (visited.contains(neighbor)) continue;

                Double newDist = dist.get(currentNode) + edge.second; //gets the new distance. then checks if it is shorter.
                if (newDist < dist.get(neighbor)) {
                    dist.put(neighbor, newDist);
                    neighbor.g = newDist;
                    neighbor.parent = currentNode;
                    pq.add(neighbor);
                }
            }
        }

        // if, somehow, no path is found, returns an empty arraylist so that we know there is a mistake
        return new ArrayList<>();
    }

    public static double dijkstraTest(Node start, Node goal, int choice) {
        PriorityQueue<Node> pq = new PriorityQueue<>(size*size);
        HashMap<Node, Double> dist = new HashMap<>();
        HashSet<Node> visited = new HashSet<>();


        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                Node node = Actions.tableOfNodes.getNodeAt(i,j);
                dist.put(node, Double.POSITIVE_INFINITY);
            }
        }
        dist.put(start, 0.0);
        pq.add(start);

        while (!pq.isEmpty()) {
            Node currentNode = pq.poll();

            if (currentNode == goal) {
                return dist.get(currentNode);
            }

            if (visited.contains(currentNode)) continue;
            visited.add(currentNode);

            // Relaxation
            for (Tuple<Node, Float> edge : currentNode.adjacencyList) {
                if(edge == null) continue;
                Node neighbor = edge.first;
                if(neighbor.p == 1 || (neighbor.p > 0 && (neighbor.isRevealed && neighbor.p != choice && !notWall.contains(neighbor.p)))) continue;
                if (visited.contains(neighbor)) continue;

                Double newDist = dist.get(currentNode) + edge.second;
                if (newDist < dist.get(neighbor)) {
                    dist.put(neighbor, newDist);
                    neighbor.g = newDist;
                    neighbor.parent = currentNode;
                    pq.add(neighbor);
                }
            }
        }
        return 0.0;
    }
    //almost same algorithm. this time, instead of returning the path, it returns the path length so that we can compare later.
    //this time, it does not skip the nodes that have been revealed and have color "choice".

        static public String takePath(Node start, Node goal) {
            ArrayList<Node> currentPath = dijkstra(start,goal); //gets path by dijkstra
            Node current = start;
            String result = "";
            while(current!=goal) { //starts loop to move along path
                for(int i = currentPath.size()-2; i > -1; i--) { //this is because the path is reversed
                    ArrayList<Node> sight = sight(current,radius); //gets sight
                    if(!sight.isEmpty()) {
                        for (Node node : sight) {
                            if (currentPath.contains(node)) { //if sight and path intersect, stops and recurses.
                                if(FileHandler.objective != 0 && result != "") result += "Path is impassable!\n";
                                String result2 = takePath(current, goal);
                                return result + result2;
                            }
                        }
                    }
                    current = currentPath.get(i); //moves to the next node.
                    result += "Moving to " + current.x + "-" + current.y + "\n";
                }
            }
            return result;
        }
        //returns the string output.

        static public ArrayList<Node> sight(Node node, int r) {
            ArrayList<Node> seenNodes = new ArrayList<>();
            for(int i = -r; i < r+1; i++) {
                for(int j = -r; j < r+1; j++) {
                    if(node.x+i < 0 || node.x+i > size-1 || node.y + j < 0 || node.y + j > size-1) continue;
                    Node checking = tableOfNodes.getNodeAt(node.x+i,node.y+j);
                    if(checking == null) continue;
                    if(!notWall.contains(checking.p) && !checking.isRevealed && Math.sqrt((node.x-checking.x)*(node.x-checking.x) + (node.y-checking.y)*(node.y-checking.y)) <= r) {
                        seenNodes.add(checking);
                        checking.isRevealed = true;
                    }
                }
            }
            return seenNodes;
        }
        //checks the square centered at node and with edge length 2r+1.
        //adds a node to sight if it is a non-passable node of type >1 and is contained in the circle centered at node with radius r.
    //marks the added nodes as revealed since now we know they are obstacles.

        static public int choice(Node start, Node goal, ArrayList<Integer> choices) {
            double minPathLength = -1;
            int minPathChoice = -1;
            for(int choice : choices) {
                double pathLength = dijkstraTest(start,goal,choice);
                if(minPathLength == -1 || pathLength < minPathLength) {
                    minPathLength = pathLength;
                    minPathChoice = choice;
                }
            }
            setAllpToZero(minPathChoice);
            return minPathChoice;
        }
        //for each choice given, calculates the shortest path length in case that choice is chosen. then returns
    //the choice that would yield the least path length.

    static public void setAllpToZero(int i) {
        notWall.add(i);
    }
    //adds the color to "notWall" list so that they can be treated as passable in the next operations.
}