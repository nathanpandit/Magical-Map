import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        String landFile = args[0];
        String timeFile = args[1];
        String missionFile = args[2];
        String outputFile = args[3];

        FileHandler fileHandler = new FileHandler(landFile,timeFile, missionFile, outputFile);
    }

}