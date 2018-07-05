/*
    This class provides a recursive descent parser of Blunt,
    creating a parse tree (which can later be translated to
    VPL code)
*/

import java.util.*;
import java.io.*;

public class Parser {

    private Lexer lex;

    public Parser( Lexer lexer ) {
        lex = lexer;
    }

    public static void main(String[] args) throws Exception
    {
        System.out.print("Enter file name: ");
        Scanner keys = new Scanner( System.in );
        String name = keys.nextLine();

        Lexer lex = new Lexer( name );
        Parser parser = new Parser( lex );

        Node root = parser.parseStatements();
        root.evaluateStatements();

   //    TreeViewer viewer = new TreeViewer("Parse Tree", 0, 0, 800, 500, root );
    }


    private Node parseStatements() {
        Node first = parseStatement();
        Token token = lex.getToken();

        if( token.isKind("id") ) {
            lex.putBack( token );
            Node second = parseStatements();
            return new Node( "parseStatements", "id", first, second, null, null );
        }
        else if (token.isKind("msg")){
            lex.putBack( token );
            Node second = parseStatements();
            return new Node("parseStatements", "message", first, second, null, null);        }
        else if (token.isKind("print")){
            lex.putBack(token);
            Node second = parseStatements();
            return new Node("parseStatements", "print", first, second, null, null);
        }
        else if (token.isKind("input")){
            lex.putBack(token);
            Node second = parseStatements();
            return new Node("parseStatements", "input", first, second, null, null);
        }
        else if (token.isKind("newline")) {
            lex.putBack(token);
            Node second = parseStatements();
            return new Node("parseStatements", "newline", first, second, null, null);
        }
       // else done with parseStatemetns
            lex.putBack( token );     // putting back EOF token?
            return new Node( "parseStatements", "", first, null, null, null );
    }

    private Node parseStatement() {
        Token token = lex.getToken();
        Node first = new Node (token);

        if( token.isKind("id") ) {
            token = lex.getToken();
            errorCheck(token, "single", "=");
            Node second = parseExpression();
            return new Node("parseStatement", "=", first, second,null,null);
        }
        else if( token.isKind("msg") ) {
            token = lex.getToken();
            errorCheck(token, "string");
            Node second = new Node(token);
            return new Node("parseStatement", "message", first, second, null, null);
        }
        else if( token.isKind("print") ) {
            Node second = parseExpression();
            return new Node("parseStatement", "print", first, second, null, null);
        }
        else if(token.isKind("newline")){
            return new Node ("parseStatement", "newline", first, null, null, null);
        }
         else  // else if (token.isKind("input")) {
        {
            errorCheck(token, "input");
            token = lex.getToken();
            errorCheck(token, "string");
            Node second = new Node(token);
            token = lex.getToken();
            errorCheck(token, "id");
            Node third = new Node(token);
            return new Node("parseStatement", "input", first, second, third, null);
        }
    }


    private Node parseExpression() {
        Node first = parseTerm();
        Token token = lex.getNext();
        if ( token.matches("single", "+")) {
            Node second = parseExpression();
            return new Node("parseExpression", "+", first, second, null, null);
        }
        else if ( token.matches("single", "-")) {
            Node second = parseExpression();
            return new Node("parseExpression", "-", first, second, null, null);
        }
        else {
            lex.putBack(token);
            return new Node("parseExpression", "", first, null, null, null);
        }
    }

    private Node parseTerm() {
        Node first = parseFactor();
        Token token = lex.getNext();
        if ( token.matches("single", "*")) {
            Node second = parseTerm();
            return new Node("parseTerm", "*", first, second, null, null);
        }
        else if ( token.matches("single", "/")) {
            Node second = parseTerm();
            return new Node("parseTerm", "/", first, second, null, null);
        }
        lex.putBack(token);
        return new Node("parseTerm", "", first, null, null, null);
    }

    private Node parseFactor(){
        Token token = lex.getNext();

        if (token.isKind("num")){
            Node first = new Node(token);
            return new Node ("parseFactor", "num", first, null, null, null);
        }
        else if (token.matches("single","(")){
            Node first = parseExpression();
            token = lex.getNext();
            errorCheck(token,"single", ")" );
            return new Node("parseFactor", "parens", first, null, null, null);
        }
        else if (token.matches("single","-")){
            Node first = parseFactor();
            return new Node ("parseFactor", "-", first, null, null, null);
        }
        //else if (token.isKind("id"))
         else { //must be 'id' or 'bif'
            Node first = new Node(token);
            token = lex.getNext();
            if (token.matches("single", "(")) {
                Node second = parseExpression();
                token = lex.getNext();
                errorCheck(token,"single", ")" );
                return new Node("parseFactor", "function", first, second, null, null);
            } else {
                lex.putBack(token);
                return new Node("parseFactor", "id", first, null, null, null);
            }
        }
    }

    private void errorCheck( Token token, String kind ) {
        if( ! token.isKind( kind ) ) {
            System.out.println("Error:  expected " + token + " to be of kind " + kind );
            System.exit(1);
        }
    }

    // check whether token is correct kind and details
    private void errorCheck( Token token, String kind, String details ) {
        if( ! token.isKind( kind ) || ! token.getDetails().equals( details ) ) {
            System.out.println("Error:  expected " + token + " to be kind=" + kind + " and details=" + details );
            System.exit(1);
        }
    }



}
