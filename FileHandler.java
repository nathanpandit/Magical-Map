import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {
    static int tableSize;
    static int objective = 0;
    public FileHandler(String landFilePath, String timeNodePath, String missionPath, String outputFilePath) {
        File landFile = new File(landFilePath);
        File timeFile = new File(timeNodePath);
        File missionFile = new File(missionPath);
        File outFile = new File(outputFilePath);
        Node current;


        try (PrintStream outstream = new PrintStream(outFile); Scanner reader1 = new Scanner(landFile); Scanner reader2 = new Scanner(timeFile); Scanner reader3 = new Scanner(missionFile)) {
            while (reader1.hasNextLine()) {
                String line = reader1.nextLine();
                String[] inputs = line.split(" ");
                if(inputs.length == 2) {
                    int input0 = Integer.parseInt(inputs[0]);
                    int input1 = Integer.parseInt(inputs[1]);
                    tableSize = (((input0 + input1)*(input0 + input1 + 1))/2) + input1; //maximum possible index for the table.
                    Actions.size = Integer.parseInt(inputs[0]);
                    Actions.tableOfNodes = new HashTable<>(tableSize);
                }
                else {
                    Tuple<Integer, Integer> coordinate = new Tuple<>(Integer.parseInt(inputs[0]), Integer.parseInt(inputs[1]));
                    Node newNode = new Node(coordinate.first, coordinate.second, Integer.parseInt(inputs[2]));
                    Actions.tableOfNodes.put(coordinate, newNode); //adds new node to the table.
                }
            }
            current = Actions.tableOfNodes.getNodeAt(0,0); //sets current as 0-0.
            while (reader2.hasNextLine()) { //sets edges.
                String line = reader2.nextLine();
                String[] inputs = line.split(" ");
                String[] inputs2 = inputs[0].split(",");
                String[] inputs3 = inputs2[0].split("-");
                String[] inputs4 = inputs2[1].split("-");
                Tuple<Integer, Integer> coordinate1 = new Tuple<>(Integer.parseInt(inputs3[0]), Integer.parseInt(inputs3[1]));
                Tuple<Integer, Integer> coordinate2 = new Tuple<>(Integer.parseInt(inputs4[0]), Integer.parseInt(inputs4[1]));
                Actions.tableOfNodes.addEdge(coordinate1,coordinate2,(Float)Float.parseFloat(inputs[1]));
            }
            ArrayList<Integer> choices = new ArrayList<>();

             while(reader3.hasNextLine()) {
                String line = reader3.nextLine();
                String[] inputs = line.split(" ");
                if(inputs.length == 1) {
                    Actions.radius = Integer.parseInt(inputs[0]); //sets radius.
                }
                else {
                    Node goalNode = Actions.tableOfNodes.getNodeAt(Integer.parseInt(inputs[0]) , Integer.parseInt(inputs[1]));
                    if(objective == 0) {
                        current = goalNode;
                        objective = 1;
                        continue;
                    }
                    if(!choices.isEmpty()) {
                        outstream.println("Number " + Actions.choice(current,goalNode,choices) + " is chosen!");
                        choices.clear();
                    }
                    outstream.print(Actions.takePath(current, goalNode));
                    outstream.println("Objective " + objective + " reached!");
                    objective+=1;
                    current = goalNode;
                    for(int i = 2; i < inputs.length; i++) {
                        int color = Integer.parseInt(inputs[i]);
                        if(!Actions.notWall.contains(color)) choices.add(color);
                    }
                }
            }



        }  catch (FileNotFoundException e) {
            System.out.println("Error: Cannot find input or output file.");
            e.printStackTrace();
        }
    }
}