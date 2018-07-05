/**
 * Enter a number.
 *  The output is all possible summations to that number.
 *
 *  Chris Brodski
 */

import java.util.*;

public class Proj1 {
    static List<List<Integer>> allAnswers; //List of list
    static int counter;
    public static void main(String []args) {
        allAnswers = new ArrayList<List<Integer>>();

        System.out.println("Enter a number:");
        Scanner sc = new Scanner(System.in);
        int uInput = sc.nextInt();
        List<Integer>  initList = new ArrayList<Integer>();

        doCombos(uInput, initList);
        System.out.println("Number of answers without repeats: "+ allAnswers.size());

    }

        public static void doCombos(int num, List<Integer> subList){
            for (int i = 1; i <= Math.floor(num/2); i++){
                List<Integer>  valueList = new ArrayList<Integer>();
                List<Integer> nList = new ArrayList<Integer>();
                List<Integer>  someList = new ArrayList<Integer>();

                valueList.addAll(subList);
                valueList.add(i);

                nList.addAll(valueList);
                nList.add(num-i);
                someList.addAll(valueList);

                checkIfAnswerExists(nList);
                doCombos(num-i,someList);
        }
        return;
    }

        public static void checkIfAnswerExists(List<Integer> anAnswer){
            Collections.sort(anAnswer);
            if (allAnswers.size() ==0 ){
                allAnswers.add(anAnswer);
                System.out.println(anAnswer );
            }
            for (List<Integer> thislist : allAnswers) {
                if (anAnswer.equals(thislist)) {    //Prevent duplicates
                    return;
                }
            }
            allAnswers.add(anAnswer);
            System.out.println(anAnswer );
        }

}
