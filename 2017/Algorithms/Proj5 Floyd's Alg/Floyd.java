import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Chris Brodski
 *  Project 5 Floyd's Algorith.
 *  Use a file for input or manually input.
 */

public class Floyd {

    public static void main(String[] args) {
        floydUnit[][] pathArr;
        if (args.length > 0) {
            pathArr = parseFile(args);
        } else {
            System.out.println("No file was given");
            pathArr = manualInput();
        }
        convertToInf(pathArr);
        doFloydsAlg(pathArr);
    }

    public static class floydUnit {
        List<Integer> path = new ArrayList<Integer>();
        double distance;
    }

    public static void doFloydsAlg(floydUnit[][] pathArr) {
        int exclude = 0;
        for (int x = 0; x < pathArr.length; x++) {
            System.out.println("----------------------------------");
            System.out.println("D" + x);
            printDistanceArray(pathArr);
            printPathArray(pathArr);

            exclude = x;
            for (int k = 0; k < pathArr.length; k++) {
                if (k == exclude) {
                    continue;
                }
                for (int j = 0; j < pathArr.length; j++) {
                    if (j == exclude || j == k) {
                        continue;
                    }
                    //If (a > b) ....then Double.compare(a,b) == 1
                    if (Double.compare(pathArr[j][k].distance, (pathArr[exclude][k].distance + pathArr[j][exclude].distance)) > 0) {
                        pathArr[j][k].distance = pathArr[exclude][k].distance + pathArr[j][exclude].distance;
                        pathArr[j][k].path.clear();
                        pathArr[j][k].path.addAll(pathArr[j][x].path);
                        pathArr[j][k].path.add(exclude + 1);
                        pathArr[j][k].path.addAll(pathArr[x][k].path);
                    }
                }
            }
        }
        System.out.println("----------------------------------");
        System.out.println("D" + exclude);
        printDistanceArray(pathArr);
        printPathArray(pathArr);
    }

    public static void printDistanceArray(floydUnit[][] pathArr) {
        System.out.println("Distance array");
        for (int j = 0; j < pathArr.length; j++) {
            for (int k = 0; k < pathArr.length; k++) {
                if (pathArr[j][k].distance == Double.POSITIVE_INFINITY) {
                    System.out.print(String.format("%-10s", "-"));  //https://dzone.com/articles/java-string-format-examples + https://dzone.com/articles/java-string-format-examples
                } else {
                    System.out.print(String.format("%-10s", pathArr[j][k].distance));
                }
            }
            System.out.println("");
        }
        System.out.println("");
    }

    public static void printPathArray(floydUnit[][] pathArr) {
        System.out.println("Intermediate vertices");
        for (int j = 0; j < pathArr.length; j++) {
            for (int k = 0; k < pathArr.length; k++) {
                System.out.print(String.format("%-10s", pathArr[j][k].path));  //https://dzone.com/articles/java-string-format-examples + https://dzone.com/articles/java-string-format-examples
            }
            System.out.println("");
        }
        System.out.println("");
    }

    public static void convertToInf(floydUnit[][] pathArr) {
        for (int j = 0; j < pathArr.length; j++) {
            for (int k = 0; k < pathArr.length; k++) {
                if (pathArr[j][k].distance == -1) {
                    pathArr[j][k].distance = Double.POSITIVE_INFINITY;
                }
            }
        }
    }

    public static floydUnit[][] parseFile(String[] args) {
        System.out.println("Using file: " + args[0]);
        File inFile = null;
        int numVertices;
        int uInput;
        floydUnit[][] pathArr;
        if (0 < args.length) {
            inFile = new File(args[0]);
            try {
                Scanner sc = new Scanner(inFile);
                numVertices = sc.nextInt();
                pathArr = new floydUnit[numVertices][numVertices];
                while (sc.hasNext()) {
                    for (int j = 0; j < numVertices; j++) {
                        for (int k = 0; k < numVertices; k++) {
                            uInput = sc.nextInt();
                            pathArr[j][k] = new floydUnit();
                            pathArr[j][k].distance = uInput;
                        }
                    }
                }
                return pathArr;

            } catch (Exception e) {
                System.out.println("File with that name doesn't exist.");
            }
        }
        return pathArr = new floydUnit[1][1]; //just to make the compiler happy

    }

    public static floydUnit[][] manualInput() {
        System.out.println("How many vertices?:");
        Scanner sc = new Scanner(System.in);
        int uInput;
        int numVertices;
        numVertices = sc.nextInt();
        floydUnit[][] pathArr = new floydUnit[numVertices][numVertices];


        for (int j = 0; j < numVertices; j++) {
            for (int k = 0; k < numVertices; k++) {
                System.out.println("Enter for row " + j + " column " + k);
                uInput = sc.nextInt();
                pathArr[j][k] = new floydUnit();
                pathArr[j][k].distance = uInput;
            }
        }
        return pathArr;
    }
}
