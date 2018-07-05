import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/** Chris Brodski
 *  Project 4
 *  Input the values for the game,
 *  then the program will optimally play for both players.
 */

public class Proj4 {

    public static void main(String []args) {

        Scanner sc = new Scanner(System.in);
        int uInput;
        int numValues;
        System.out.println("How many values?:");
        numValues = sc.nextInt();

        int values[] = new int[numValues];
        thePlay[][] thePlayList = new thePlay[numValues][numValues];

        System.out.println("Begin entering each value");
        for (int i = 0; i < numValues; i++) {
            System.out.println("Enter value: ");
            uInput = sc.nextInt();
            values[i] = uInput;
        }

        doBaseCases(thePlayList, values);
        fillNextCells(thePlayList, values);
        printPlays(thePlayList, values);
        playGame(thePlayList,values);
    }

    public static void doBaseCases(thePlay[][] thePlayList, int[] values){
        for (int i = 0; i < thePlayList.length; i ++){
            thePlayList[i][i] = new thePlay();
            thePlayList[i][i].score = values[i];
            thePlayList[i][i].player2Score = 0;
            thePlayList[i][i].action = "L"; //L or R
        }
    }

    public static void printPlays(thePlay[][] thePlayList, int[] values) {
        System.out.println("Array:");
        for (int i = 0; i < values.length; i ++) {
            System.out.print(values[i] + "\t \t\t");
        }
        System.out.println("");
        System.out.println("");
        for (int j = 0; j < thePlayList.length; j++) {
            for (int k = 0; k < thePlayList.length; k++) {
                if (thePlayList[j][k] != null) {
                    System.out.print(thePlayList[j][k].action + ", ");
                    System.out.print(thePlayList[j][k].score + "     \t");
                }
                else{
                    System.out.print("-    \t\t");
                }
            }
            System.out.println("");
        }
    }

    public static void fillNextCells(thePlay[][] thePlayList, int[] values){
        int diagCount = 1;
        int k = diagCount;
        int j = 0;
        while(diagCount < values.length ){
            thePlayList[j][k] = new thePlay();
            int leftOption = values[j] + thePlayList[j+1][k].player2Score;
            int rightOption = values[k] + thePlayList[j][k-1].player2Score;
            if (leftOption >= rightOption) {
                thePlayList[j][k].score = leftOption;
                thePlayList[j][k].player2Score = thePlayList[j+1][k].score;
                thePlayList[j][k].action = "L";
            }
            else {
                thePlayList[j][k].score = rightOption;
                thePlayList[j][k].player2Score = thePlayList[j][k-1].score;
                thePlayList[j][k].action = "R";
            }
            k = k + 1;
            j = j + 1;
            if (k == values.length){
                j = 0;
                diagCount = diagCount + 1;
                k = diagCount;
            }
        }
    }

    public static void playGame(thePlay[][] thePlayList, int[] values){
        List<Integer> vList = new ArrayList<Integer>();
        for (int i = 0; i < values.length; i ++){
            vList.add(values[i]);
        }
        int i = 0;
        int j = 0;
        int k = values.length - 1;
        List<Integer> p1 = new ArrayList<Integer>();
        List<Integer> p2 = new ArrayList<Integer>();
        int endIndex = 0;
        while (i != values.length){

            //Player 1
            endIndex = vList.size() - 1;
            System.out.print("Turn " + i + ": " + vList + " \t");
            if (thePlayList[j][k].action.equals("R")) {
                System.out.println("Player 1 takes: " + vList.get(endIndex));
                p1.add(vList.get(endIndex)); // Add the right
                vList.remove(endIndex); //Remove the right
                k = k - 1;
            } else {
                System.out.println("Player 1 takes: " + vList.get(0));
                p1.add(vList.get(0));
                vList.remove(0);
                j = j + 1;
            }
            i++;
            if (i == values.length){
                break;
            }

            endIndex = vList.size() -1;
            //Player 2
            System.out.print("Turn " + i + ": " + vList + " \t");
            if (thePlayList[j][k].action.equals("R")) {
                System.out.println("Player 2 takes: " + vList.get(endIndex));
                p2.add(vList.get(endIndex));
                vList.remove(endIndex);
                k = k - 1;
            } else {
                System.out.println("Player 2 takes: " + vList.get(0));
                p2.add(vList.get(0));
                vList.remove(0);
                j = j + 1;
            }
            i++;
        }
        System.out.println("Final sets:");
        System.out.println("Player 1 (goes 1st): " + p1);
        System.out.println("Player 2 (goes 2nd): " + p2);


    }

    public static class thePlay{
        int score;
        int player2Score; //This is the score of the other player if he plays optimally
        String action; //tell player to select Left or Right.

    }
}
