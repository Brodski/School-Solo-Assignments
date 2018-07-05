/*  a Node holds one node of a parse tree
    with several pointers to children used
    depending on the kind of node
*/

import java.util.ArrayList;
import java.io.*;
import java.awt.*;
import java.util.Scanner;

public class Node {

    public static ArrayList<KeyValue> memory = new ArrayList<KeyValue>(); // A list of key value pairs. ex) memory["x"] = 2.34
    public static int count = 0;  // maintain unique id for each node

    private int id;

    private String kind;  // non-terminal or terminal category for the node
    private String info;  // extra information about the node such as
    // the actual identifier for an I

    // references to children in the parse tree
    // (uniformly use these from first to fourth)
    private Node first, second, third, fourth;

    public Node(String k, String inform, Node one, Node two, Node three, Node four) {
        kind = k;
        info = inform;
        first = one;
        second = two;
        third = three;
        fourth = four;
        id = count;
        count++;
//        System.out.println(this);
    }

    // construct a node that is essentially a token
    public Node(Token token) {
        kind = token.getKind();
        info = token.getDetails();
        first = null;
        second = null;
        third = null;
        fourth = null;
        id = count;
        count++;
//        System.out.println(this);
    }

    public String toString() {
        return "#" + id + "[" + kind + "," + info + "]";
    }

    // produce array with the non-null children
    // in order
    private Node[] getChildren() {
        int count = 0;
        if (first != null) count++;
        if (second != null) count++;
        if (third != null) count++;
        if (fourth != null) count++;
        Node[] children = new Node[count];
        int k = 0;
        if (first != null) {
            children[k] = first;
            k++;
        }
        if (second != null) {
            children[k] = second;
            k++;
        }
        if (third != null) {
            children[k] = third;
            k++;
        }
        if (fourth != null) {
            children[k] = fourth;
            k++;
        }

        return children;
    }

    //******************************************************
    //              Executing the program
    //******************************************************

    public void evaluateStatements() {
        Node childrenNodes[] = this.getChildren();
        if (this.kind.equals("parseStatements")) {
            for (int i =0; i<childrenNodes.length; i++) {
                childrenNodes[i].evaluateStatements();
            }
        }
        if (this.kind.equals("parseStatement")) {
            if (this.info.equals("=")) {
                 if (memory.size() == 0) {
                    memory.add(new KeyValue(childrenNodes[0].info, childrenNodes[1].evaluate()));
                }
                else {
                    for ( int i = 0; i < memory.size(); i++) {
                        if (memory.get(i).key.equals(childrenNodes[0].info)) {
                            memory.get(i).value = childrenNodes[1].evaluate();
                        } else {
                            memory.add(new KeyValue(childrenNodes[0].info, childrenNodes[1].evaluate()));
                        }
                    }
                }
            }
            if (this.info.equals("message")) {
                System.out.print(childrenNodes[1].info);
            }
            if (this.info.equals("print")) {
                System.out.print(childrenNodes[1].evaluate());;
            }
            if (this.info.equals("input")) {
                System.out.print(childrenNodes[1].info);
                Scanner in = new Scanner(System.in);
                double  userInput = in.nextDouble();
                if (memory.size() == 0) {
                    memory.add(new KeyValue(childrenNodes[2].info, userInput )); //(key, value)
                }
                else {
                    for (int i = 0; i < memory.size(); i++) {
                        System.out.println("Inside of for loop");
                        if (memory.get(i).key.equals(childrenNodes[2].info)) {
                            memory.get(i).value = userInput;
                        } else {
                            memory.add(new KeyValue(childrenNodes[2].info, userInput));
                        }
                    }
                }
            }
            if (this.info.equals("newline")) {
                System.out.println("\n");
            }
        }
    }

    public double evaluate() {
        Node childrenNodes[] = this.getChildren();
        if (this.kind.equals("parseExpression")) {
            if (this.info.equals("+")) {
                double term = childrenNodes[0].evaluate();
                double expression = childrenNodes[1].evaluate();
                return term + expression;
            }
            else if (this.info.equals("-")) {
                double term = childrenNodes[0].evaluate();
                double expression = childrenNodes[1].evaluate();
                return  term - expression;
            }
            else { // else if (this.info.equals(""))
                double term = childrenNodes[0].evaluate();
                return term;
            }
        }
        if (this.kind.equals("parseTerm")) {
            if (this.info.equals("*")) {
                double factor = childrenNodes[0].evaluate();
                double term = childrenNodes[1].evaluate();;
                return factor * term;
            }
            if (this.info.equals("/")) {
                double factor = childrenNodes[0].evaluate();
                double term = childrenNodes[1].evaluate();;
                return factor / term;
            } else { // else if (this.info.equals("")) {
                double factor = childrenNodes[0].evaluate();;
                return factor;
            }
        }
        if (this.kind.equals("parseFactor")) {
            if (this.info.equals("num")) {
                return Double.parseDouble(childrenNodes[0].info);
            }
            if (this.info.equals("parens")) {
                return childrenNodes[0].evaluate();
            }
            if (this.info.equals("-")) {
                return -(childrenNodes[0].evaluate());
            }
            if (this.info.equals("function")) {
                String builtInFunction = childrenNodes[0].info;
                double expression = childrenNodes[1].evaluate();
                if (builtInFunction.equals("sin")) {
                    return Math.sin(expression);
                }
                if (builtInFunction.equals("cos")) {
                    return Math.cos(expression);
                }
                if (builtInFunction.equals("tan")) {
                    return Math.tan(expression);
                }
                if (builtInFunction.equals("exp")) {
                    return Math.exp(expression);
                }
                if (builtInFunction.equals("sqrt")) {
                    return Math.sqrt(expression);
                }
            }
            if (this.info.equals("id")) {
                for (int i = 0; i < memory.size(); i++) {
                    if (memory.get(i).key.equals(childrenNodes[0].info)) {
                        //String val = memory.get(i).value;
                        return memory.get(i).value;
                    }
                }
            }else {
                    System.out.println("Error: expected key value pair, with key: " + childrenNodes[0].info);
                    System.exit(1);
            }
        }
        return  -69;
    }
















    //******************************************************
    // graphical display of this node and its subtree
    // in given camera, with specified location (x,y) of this
    // node, and specified distances horizontally and vertically
    // to children
    public void draw( Camera cam, double x, double y, double h, double v ) {

        System.out.println("draw node " + id );

        // set drawing color
        cam.setColor( Color.black );

        String text = kind;
        if( ! info.equals("") ) text += "(" + info + ")";
        cam.drawHorizCenteredText( text, x, y );

        // positioning of children depends on how many
        // in a nice, uniform manner
        Node[] children = getChildren();
        int number = children.length;
        System.out.println("has " + number + " children");

        double top = y - 0.75*v;

        if( number == 0 ) {
            return;
        }
        else if( number == 1 ) {
            children[0].draw( cam, x, y-v, h/2, v );     cam.drawLine( x, y, x, top );
        }
        else if( number == 2 ) {
            children[0].draw( cam, x-h/2, y-v, h/2, v );     cam.drawLine( x, y, x-h/2, top );
            children[1].draw( cam, x+h/2, y-v, h/2, v );     cam.drawLine( x, y, x+h/2, top );
        }
        else if( number == 3 ) {
            children[0].draw( cam, x-h, y-v, h/2, v );     cam.drawLine( x, y, x-h, top );
            children[1].draw( cam, x, y-v, h/2, v );     cam.drawLine( x, y, x, top );
            children[2].draw( cam, x+h, y-v, h/2, v );     cam.drawLine( x, y, x+h, top );
        }
        else if( number == 4 ) {
            children[0].draw( cam, x-1.5*h, y-v, h/2, v );     cam.drawLine( x, y, x-1.5*h, top );
            children[1].draw( cam, x-h/2, y-v, h/2, v );     cam.drawLine( x, y, x-h/2, top );
            children[2].draw( cam, x+h/2, y-v, h/2, v );     cam.drawLine( x, y, x+h/2, top );
            children[3].draw( cam, x+1.5*h, y-v, h/2, v );     cam.drawLine( x, y, x+1.5*h, top );
        }
        else {
            System.out.println("no Node kind has more than 4 children???");
            System.exit(1);
        }

    }
    // draw

    // a node can translate the tree rooted at it to VPL code


}// Node class
