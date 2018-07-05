import java.io.File;
import java.util.Scanner;

/** Project 6
 * Chris Brodski
 *
 * Run Proj6.java
 * Enter file name.
 * The program will then do the branch & bound algorithm.
 */

public class Proj6 {

    public static int capacity;
    public static int numItems;
    public static Node knownBest;
    public static Item[] allItems;
    public static int count;

    public static void main(String args[]) {

        count = 1;
        Scanner sc = getFileScanner();
        capacity = sc.nextInt();
        numItems = sc.nextInt();
        allItems = new Item[numItems];

        int i = 0;
        while (sc.hasNext()) {
            allItems[i] = new Item();
            allItems[i].profit = sc.nextInt();
            allItems[i].weight = sc.nextInt();
            allItems[i].indx = i;
            i++;
        }
        knownBest = new Node();
        Node topNode = new Node();
        topNode.id = count;
        topNode.level = 0;
        topNode.currentValue = 0;
        topNode.possibleBest = calcBound(0, topNode);

        printStartingInfo();

        doAlgorith(topNode);

        sysp("\nBest node: " + knownBest.toString());
    }



    public static void doAlgorith(Node node){ //Branch and Bound
//        sysp("---------------------\nAT LEVEL " +node.level");
        sysp("");

        if (node.possibleBest <= knownBest.currentValue ){
            sysp(node.toString());
            sysp("    pruned, don't explore children b/c bound "+ node.possibleBest + " <= known achievable profit " + knownBest.currentValue + ", found on Node # " +knownBest.id );
            return;
        }
        if (node.level == numItems){
            sysp("ENDING execution for node " + node.toString());
            sysp("    No items left in sack. numItems: " +numItems);
            checkOptimal(node);
            return;
        }
        node.left = new Node();
        node.right = new Node();
        count++;
        node.left.id = count;
        count++;
        node.right.id = count;
        node.left.parent = node;
        node.right.parent = node;
        node.left.level = node.level + 1;
        node.right.level = node.level + 1;

        node.left.itemsInSack.addAll(node.itemsInSack);
        node.right.itemsInSack.addAll(node.itemsInSack);

        node.left.possibleBest = calcBound(node.level + 1, node );
        node.right.possibleBest = node.possibleBest; // Dont need to do calcBound() again, since same answer

        if (node.parent != null) {
            node.left.currentValue = node.currentValue;
            node.right.currentValue = node.currentValue;
        }

        node.right.itemsInSack.add(allItems[node.level]);
        node.right.currentValue = node.currentValue + allItems[node.level].profit;

        if ( node.right.calcTotalWeight() > capacity) {
            sysp("EXECUTING: " + node.toString());
            sysp("    LEFT: " + node.left.toString());
            sysp("        exploring further");
            sysp("    RIGHT: " + node.right.toString());
            sysp("        pruned because too heavy");
            checkOptimal(node);
            doAlgorith(node.left);
            return;
        }
        else if (node.left.possibleBest > node.right.possibleBest){
            sysp("EXECUTING: " + node.toString());
            sysp("    LEFT: " + node.left.toString());
            sysp("        exploring further");
            sysp("    RIGHT: " + node.right.toString());
            sysp("        exploring further");
            doAlgorith(node.left);
            doAlgorith(node.right);
        }
        else { // left <= right
            sysp("EXECUTING: " + node.toString());
            sysp("    LEFT: " + node.left.toString());
            sysp("        exploring further");
            sysp("    RIGHT: " + node.right.toString());
            sysp("        exploring further");
            doAlgorith(node.right);
            doAlgorith(node.left);
        }
    }

    public static void checkOptimal(Node node){
        if (node.currentValue > knownBest.currentValue){
            knownBest = node;
            sysp("    Checking node " + node.id + ". New best solution found! Profit: " + knownBest.currentValue);
        }
        else {
            sysp("    Checking node " + node.id + ". Not a better solution. Profit: " + knownBest.currentValue + " at node " + knownBest.id);
        }
    }

    //Calculation for branch & bound algorithm
    public static double calcBound(int starting, Node parentNode){
        int i = starting;
        int tempSackWt = parentNode.calcTotalWeight();

        double bound = parentNode.currentValue; //set up

        while (tempSackWt < capacity){
            if (i == numItems){
                return bound;
            }
            if (allItems[i].weight + tempSackWt < capacity) {
                tempSackWt = tempSackWt + allItems[i].weight;
                bound = bound + allItems[i].profit;
            }
            else{
                double remWt = capacity - tempSackWt; // remaining weight
                double fraction = remWt / allItems[i].weight;
                bound = bound + (allItems[i].profit * fraction);
                return bound;
            }
            i++;
        }
        return bound;
    }

    public static void printStartingInfo(){
         sysp("Capacity of knapsack is " + capacity);
         sysp("Items are:");
         for (int i = 0; i < numItems; i++){
             sysp("" + (i+1) + ": " + allItems[i].profit + " " + allItems[i].weight);
         }
    }

    public static Scanner getFileScanner(){
        System.out.println("Enter file name:");
        Scanner sc = new Scanner(System.in);
        File inFile = new File(sc.next());
        try {
            sc = new Scanner(inFile);
            return sc;
        }catch (Exception e){
            sysp("File doesn't exist");
            System.exit(1);
        }
        return sc;
    }

    public static void sysp(String str){
        System.out.println(str);
    }
}