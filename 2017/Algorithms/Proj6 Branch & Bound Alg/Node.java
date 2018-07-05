import java.util.ArrayList;

public class Node {
    int id;
    double possibleBest; //should have been named "bound"
    int currentValue;
    int level;
    ArrayList<Item> itemsInSack = new ArrayList<Item>();
    Node parent;
    Node left;
    Node right;

    Node(){
        currentValue = 0;
        possibleBest = 0;
    }

    public int calcTotalWeight(){
        int wt = 0;
        for (Item item : itemsInSack){
            wt = wt + item.weight;
        }
        return wt;
    }

    @Override
    public String toString(){
        String str;
        String items = "[";
        for (int i = 0; i < this.itemsInSack.size(); i++){
            items = items + (this.itemsInSack.get(i).indx + 1) + ", ";
        }
        items = items + "]";
        str = "<Node " + this.id + ": items: " + items + " level: " + this.level + " profit: " + this.currentValue + " weight: " + this.calcTotalWeight() + "  bound: " + this.possibleBest + " >";
        return str;
    }


}
