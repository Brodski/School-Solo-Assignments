import java.io.File;
import java.util.*;

/** Chris Brodski
 *
 * This is a NIST RBAC.
 * The program assumes you enter the a correctly formatted file.
 * The file could have logical errors, but the program assumes that the file is not nonsense.
 *
 * The programs will read: roleHierarchy.txt, resourceObjects.txt, permissionsToRoles.txt,
 *      roleSetsSSD.txt, and userRoles.txt
 */

//Some objects will look funny, it's b/c I had something in mind when I initially started writing
    // the program, but then changed my implementation :(

public class RBAC {

    public static void  main(String[] args){
        List userList = new ArrayList<user>();
        List objectList = new ArrayList<commonData>();
        List roleList = new ArrayList<role>();
        List excludeList = new ArrayList<exclude>();


        //Step 2
        readRoleFile(roleList);
        sysp("Printing role hierarchy:");
        printRoleHierarchy(roleList);
        //Step 3
        sysp("");
        addRolesToObjectList(roleList,objectList);
        readObjectFile(objectList, roleList);
        sysp("Printing initial role-object matrix:");
        printObjectList(objectList, roleList);

        //Step 4
        List<List<List<String>>> ROMatrix = new ArrayList<List<List<String>>>(); //3D List
        updateRoleObjMatrix(ROMatrix, roleList, objectList);
        readPermissionsFile(ROMatrix,roleList,objectList); //Assumes you enter a file that makes sense. If you enter a nonsense file, it might compute but it will be absurd.
        printROMatrixTable(ROMatrix, objectList, roleList);
        sysp("The tables above and below are equivalent\n");
        printROMatrixText(ROMatrix,roleList, objectList);
        sysp("");

        //Step 5
        excludeList = readSSD(ROMatrix,objectList,roleList);
        sysp("");

        //Step 6
        userList = readUserRoles(roleList, excludeList);
        printUserRoleMatrix(userList, roleList);

        //Step 7
        doQuery(userList, objectList, ROMatrix, roleList);

    }

    public static void doQuery(List<user> userList, List<commonData> objectList, List<List<List<String>>> ROMatrix, List<role> roleList ){

        sysp("Please enter the user in your query: ");
        Scanner sc = new Scanner(System.in);
        String uInput = sc.next();

        if (checkUserExistence(userList, uInput) == false) {
            sysp("invalid user, try again.");
            doQuery(userList, objectList, ROMatrix, roleList);
            return;
        }

        sysp("Please enter the object in you query (hit enter if it's for any): ");
        String obInput = getUserInputPro();
        sysp("Please enter the access right in your query (hit enter if it's for any): ");
        String accInput = getUserInputPro();


        user u = getUser(userList, uInput);

        if (obInput.equals("") && accInput.equals("")){
            printUserAcessRights(userList, objectList, ROMatrix, roleList, u);
        } else if (checkObjectExistence(objectList, obInput) == false) {
            sysp("invalid object, try again.");
            doQuery(userList, objectList, ROMatrix,roleList);
            return;
        } else if ( accInput.equals("") ){
            printUserAcessRights_2(userList, objectList, ROMatrix, roleList, u, obInput);
        } else{
            boolean isAuthorized = checkAccessRightsToObj(userList, objectList, ROMatrix, roleList, u, obInput, accInput);
            if (isAuthorized){
                sysp("Authorized");
            }else {
                sysp("Rejected");
            }
        }
        sysp("Would you like to continue the next query? Type 'yes' if you want to query more");
        uInput = getUserInputPro();
        if (uInput.equals("yes")){
            doQuery(userList, objectList, ROMatrix,roleList);
            return;
        }
        return;


    }

    public static user getUser(List<user> userList, String username) {
        for (user u : userList) {
            if (u.name.equals(username)) {
                return u; //We know user already exists
            }
        }
        return new user(); //just to make compiler happy
    }

    public static boolean checkAccessRightsToObj(List<user> userList, List<commonData> objList, List<List<List<String>>> ROMatrix, List<role> roleList, user myUser, String userObject, String wantedAccess){
        for (int i = 0; i< roleList.size(); i++){
            if ( myUser.roles.contains( roleList.get(i).name) == false  ){
                continue;
            }
            for (int j = 0; j < objList.size(); j++){
                if (objList.get(j).name.equals(userObject) == false){
                    continue;
                }
                if(ROMatrix.get(i).get(j).isEmpty() == false){
                    for ( String s : ROMatrix.get(i).get(j) ){
                        if (s.equals(wantedAccess)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void printUserAcessRights_2(List<user> userList, List<commonData> objList, List<List<List<String>>> ROMatrix, List<role> roleList, user myUser, String userObject){
        sysp("User " + myUser.name + " has roles: " + myUser.roles);
        for (int i = 0; i< roleList.size(); i++){
            if ( myUser.roles.contains( roleList.get(i).name) == false  ){
                continue;
            }
            System.out.print(roleList.get(i).name);
            System.out.print(" ---->");

            for (int j = 0; j < objList.size(); j++){
                if (objList.get(j).name.equals(userObject) == false){
                    continue;
                }
                if(ROMatrix.get(i).get(j).isEmpty() == false){
                    System.out.print(objList.get(j).name + ": ");
                    for ( String s : ROMatrix.get(i).get(j) ){
                        System.out.print(s + " ");
                    }
                    System.out.print(" ");
                } else {
                    System.out.print("(" + userObject + " not found)");}
            }
            sysp("");
        }
    }

    public static void printUserAcessRights(List<user> userList, List<commonData> objList, List<List<List<String>>> ROMatrix, List<role> roleList, user myUser){
        sysp("User " + myUser.name + " has roles: " + myUser.roles);
        for (int i = 0; i< roleList.size(); i++){
            if ( myUser.roles.contains( roleList.get(i).name) == false  ){
                continue;
            }
            System.out.print(roleList.get(i).name);
            System.out.print(" ---->");

            for (int j = 0; j < objList.size(); j++){
                if(ROMatrix.get(i).get(j).isEmpty() == false){
                    System.out.print(objList.get(j).name + ": ");
                    for ( String s : ROMatrix.get(i).get(j) ){
                        System.out.print(s + " ");
                    }
                    System.out.print(" ");
                }
            }
            sysp("");
        }
    }


    public static boolean checkObjectExistence(List<commonData> objList, String uInput){
        for (commonData cd : objList){
            if (cd.name.equals(uInput)){
                return true;
            }
        }
        return false;
    }


    public static boolean checkUserExistence(List<user> userList, String uInput){
        for (user u : userList){
            if (u.name.equals(uInput)){
                return true;
            }
        }
        return false;
    }

    public static String getUserInputPro(){
        Scanner sc = new Scanner(System.in);
        String obInput = sc.nextLine();
        if (obInput.equals("")){
            return obInput;
        }
        return obInput;
    }

    public static void printUserRoleMatrix(List<user> userList, List<role> roleList){
        System.out.print(String.format("%-8s", " "));
        for ( role r : roleList) {
            System.out.print(String.format("%-5s", r.name));
        }
        sysp("");
        for ( user u : userList){
            System.out.print(String.format("%-8s", u.name));
            for ( role r : roleList){
                if ( u.roles.contains( r.name )){
                    System.out.print(String.format("%-5s", "+"));
                }else {
                    System.out.print(String.format("%-5s", " "));
                }
            }
            sysp("");
        }
    }

    public static void printROMatrixText(List<List<List<String>>> ROMatrix, List<role> roleList, List<commonData> objList){
        for (int i = 0; i< roleList.size(); i++){
            System.out.print(roleList.get(i).name);
            System.out.print(" ---->");

            for (int j = 0; j < objList.size(); j++){
                if(ROMatrix.get(i).get(j).isEmpty() == false){
                    System.out.print(objList.get(j).name + ": ");
                    for ( String s : ROMatrix.get(i).get(j) ){
                        System.out.print(s + " ");
                    }
                    System.out.print(". ");
                }
            }
            sysp("");
        }
    }

    public static List<user> readUserRoles(List<role> roleList, List<exclude> excludeList){
        List<user> userList = new ArrayList<user>();
        List<String> usernames = new ArrayList<String>();
        int lineNum = 1;
        String uInput;
        String username = "";
        String roleFromFile = "";
        //sysp("Enter user roles file:");
        //Scanner sc = new Scanner(System.in);
        //sc = getFileScanner(sc.next());
        Scanner sc = getFileScanner("userRoles.txt");
        while (sc.hasNextLine()) {
            lineNum = lineNum + 1;
            List<String> assignList = new ArrayList<String>();
            username = sc.next();
            roleFromFile = sc.nextLine();

            if (usernames.contains(username)){
                sysp("Duplicate user found at " + lineNum + " Fix the file and try again.");
                userList.clear();
                assignList.clear();
                usernames.clear();
                getUserInputPro();
                userList = readUserRoles(roleList,excludeList); // ???
                break;
            }

            String[] splitRoles = roleFromFile.split("\\s+");
            for ( String s : splitRoles){
                if (!s.equals("")) {
                    assignList.add(s);}
            }
            if (checkRBAC(assignList, excludeList) == false){
                sysp("Error in user-roles file at line " + lineNum + ". Fix the file and try again.");
                userList.clear();
                assignList.clear();
                usernames.clear();
                sc.close();
                getUserInputPro();
                userList = readUserRoles(roleList,excludeList); // ???
                break;
            }
            //else, we are good
            usernames.add(username);
            userList.add(new user(username, assignList ) );
        }
        return userList;
    }

    //Returns true if the assignList doesn't conflict with the RBAC, ie the excludeList
    public static boolean checkRBAC(List<String> assignList, List<exclude> excludeList){
        for ( exclude ex : excludeList){
            int count = 0;
            for ( String as : assignList){
                if (ex.roles.contains(as) ){
                    count = count + 1;
                }
                if (count >= ex.numExclude){
                    sysp( "Error. Constrains are not satisfied, due to contraint # "  + (excludeList.indexOf(ex) + 1));
                    return false;
                }
            }
        }
        return true;
    }

    public static List<exclude> readSSD(List<List<List<String>>> ROMatrix, List<role> roleList, List<commonData> objList){
        String uInput;
        String roleName = "";
        List<exclude>  excludeList = new ArrayList<exclude>();
        int limitedNum =0;
        int lineNum = 0;
        Scanner sc = getFileScanner("roleSetsSSD.txt");
        while (sc.hasNextLine()) {
            lineNum = lineNum + 1;
            List<String> rolesToExcludeList = new ArrayList<String>();
            limitedNum = sc.nextInt();
            roleName = sc.nextLine();
            String[] splitRoles = roleName.split("\\s+");
            for ( String s : splitRoles){
                if (!s.equals("")) {
                    rolesToExcludeList.add(s);
                }
            }
            if (limitedNum >= 2){
                exclude myExclude = new  exclude(limitedNum, rolesToExcludeList);
                excludeList.add(myExclude);
            } else {
                sysp("Error at line " + lineNum + " press enter to read it again");
                sc.close();
                rolesToExcludeList.clear();
                excludeList.clear();
                getUserInputPro();
                excludeList = readSSD(ROMatrix, roleList, objList);
                break;
            }
            int last = excludeList.size()-1;
            sysp("Contraint " + excludeList.size() +", n = " + excludeList.get(last).numExclude + ", set of roles = " + excludeList.get(last).roles );
        }
        return excludeList;
    }


    public static void readPermissionsFile(List<List<List<String>>> ROMatrix, List<role> roleList, List<commonData> objList) {
        String uInput;
        String roleName = "";
        String access = "";
        String objName = "";
        int i=0;
        int j=0;
        Scanner sc = getFileScanner("permissionsToRoles.txt");
        role rdRole = null;
        while (sc.hasNext()) {
            roleName = sc.next();
            access = sc.next();
            objName = sc.next();
            rdRole = getRoleByName(roleName, roleList);
            i = rdRole.index;
            commonData obj = getObjByName(objName, objList);
            j = obj.index;
            ROMatrix.get(i).get(j).add(access);
            if (rdRole.descendant != null ){
                addAttributeToAllDescendents2(ROMatrix, j, rdRole.descendant, access); }
        }
    }

    public static void addAttributeToAllDescendents2(List<List<List<String>>> ROMatrix, int j, role currentRole, String attribute){
        int i = currentRole.index;
        ROMatrix.get(i).get(j).add(attribute);
        if(!(currentRole.descendant == null)) {
            addAttributeToAllDescendents2(ROMatrix, j, currentRole.descendant, attribute);
        }

    }

    public static void addAttributeToAllDescendents(List<List<List<String>>> ROMatrix, role origRole, role currentRole, String attribute){
        int i = origRole.index;
        int j = currentRole.index;
        ROMatrix.get(i).get(j).add(attribute);
        if(!(currentRole.descendant == null)) {
            addAttributeToAllDescendents(ROMatrix, origRole, currentRole.descendant, attribute);
        }

    }

    public static void printROMatrixTable(List<List<List<String>>> ROMatrix, List<commonData> objectList, List<role> roleList){
        int i = 0;
        int jk = 0;
        for ( commonData cd : objectList){
            if (jk % 5 == 0) {
                System.out.print(String.format("%-10s", "| "));
            }
            System.out.print(String.format("%-25s", cd.name));
            jk++;
        }
        sysp("");
        for ( role tRole : roleList){
            for (int  j = 0; j < ROMatrix.get(i).size(); j ++) {
                if (j % 5 == 0){
                    System.out.print(String.format("%-10s", "| " + tRole.name));
                }
                int items =  ROMatrix.get(i).get(j).size();
                if (items>0){
                    System.out.print(String.format("%-25s", ROMatrix.get(i).get(j)));
                }
                else {
                    System.out.print(String.format("%-25s", "-"));
                }
            }
            sysp("");
            i++;
        }
        sysp("");
    }

    public static void updateRoleObjMatrix(List<List<List<String>>> ROMatrix, List<role> roleList, List<commonData> objectlist){
        int i = 0;
        int j = 0;
        for (role tRole : roleList){
            List<List<String>> roleRow = new ArrayList<List<String>>();
            for (commonData cd : objectlist){
                List<String> roleCell = new ArrayList<String>();
                if (i==j){
                    roleCell.add("control");
                }
                roleRow.add(roleCell);
                j = j + 1;
            }
            ROMatrix.add(roleRow);
            j = 0;
            i = i + 1;
        }
        for ( role tRole : roleList) {
            if (!(tRole.descendant == null)) {
                addAttributeToAllDescendents(ROMatrix, tRole, tRole.descendant, "control");
                addAttributeToAllDescendents(ROMatrix, tRole, tRole.descendant, "own");
            }
        }


    }

    public static void printObjectList(List<commonData> objectList, List<role> roleList){
        System.out.print(String.format("%-8s", " "));
        for ( commonData cd : objectList){
            System.out.print(String.format("%-5s", cd.name));
        }
        sysp("");
        for ( role tRole : roleList){
            sysp(tRole.name);
        }
        sysp("");

    }

    public static void addRolesToObjectList(List<role> roleList, List<commonData> objectList){
        int i = 0;
        for (role tRole : roleList){
            commonData cd = new commonData();
            cd.name = tRole.name;
            cd.index = i;
            tRole.index = i;
            objectList.add(cd);
            i++;
        }
    }

    public static void readObjectFile(List<commonData> objectList, List<role> roleList){

        Scanner sc = getFileScanner("resourceObjects.txt");
        List<commonData> auxCd = new ArrayList<commonData>();
        int i = 0;
        while (sc.hasNext()) {
            String name = sc.next();
            commonData cd = new commonData();
            cd.name = name;
            cd.index = i + roleList.size();
//            objectList.add(cd);
            auxCd.add(cd);
            if ( checkForDuplicates(name, auxCd) ){ //If duplicate exists
                sysp("Duplicate object found. Press any non-whitespace key then enter to try again");
                sc.close();
                auxCd.clear();
                getUserInputPro();
                readObjectFile(objectList, roleList);
                return;
            }
            i++;
        }
        objectList.addAll(auxCd);
    }

    public static boolean checkForDuplicates(String newName, List<commonData> objectList){
        for (int i = 0; i < objectList.size() - 1; i ++){
            if (objectList.get(i).name.equals(newName) ){
                return true;
            }
        }
        return false;
    }


    public static Scanner getFileScanner(){
        sysp("Enter name of file: ");
        Scanner sc = new Scanner(System.in);
        String uInput = sc.next();
        File inFile = new File(uInput);

        try {
            sc = new Scanner(inFile);
        }catch (Exception e){
            sysp("File doesn't exist");
        }
        return sc;
    }

    public static Scanner getFileScanner(String file){
        sysp("Reading " + file);
        Scanner sc = new Scanner(System.in);
        String uInput = file;
        File inFile = new File(uInput);
        try {
            sc = new Scanner(inFile);
        }catch (Exception e){
            sysp("File doesn't exist");
        }
        return sc;
    }

    public static void readRoleFile(List<role> roleList ){
        Scanner sc = getFileScanner("roleHierarchy.txt");
        int lineNum = 1;
        while (sc.hasNext()){
            String ascName = sc.next();
            String desName = sc.next();
            if (connect2roles(ascName, desName, roleList) == false){
                sysp("Invalid line found in roleHierarchy.txt: line " + lineNum +", press enter to read it again");
                roleList.clear();
  //              sc = new Scanner(System.in);
//                sc.nextLine();
                getUserInputPro();
                readRoleFile(roleList);
            }
            lineNum = lineNum + 1;
        }
    }

    public static void printFromTop(role thisRole){
        System.out.print(thisRole.name);
        System.out.print(" ------> ");
        for (int i = 0; i < thisRole.ascendantList.size(); i ++){
            role nRole = (role) thisRole.ascendantList.get(i);
            System.out.print(nRole.name + ", ");
        }
        sysp("");
        for (int i = 0; i < thisRole.ascendantList.size(); i ++){
            role nRole = (role) thisRole.ascendantList.get(i);
            printFromTop(nRole);
        }
    }

    public static void printRoleHierarchy(List<role> roleList){
        sysp("Descendant-->Ascendant");
        for (role thisRole : roleList){
            if (thisRole.descendant == null){
                printFromTop(thisRole);
            }

        }
    }

    public static boolean connect2roles(String ascName, String desName, List<role> roleList){
        boolean ascExist = searchForNameExistence(ascName, roleList);
        boolean desExist = searchForNameExistence(desName, roleList);

        if (!ascExist && desExist){
            role nRole = new role(ascName);
            roleList.add(nRole);
        }
        else if (!desExist && ascExist){
            roleList.add(new role(desName) );
        }
        else if (!ascExist && !desExist){
            roleList.add(new role(ascName) );
            roleList.add(new role(desName) );
        }
        if (getRoleByName(ascName,roleList).descendant != null){
            return false;
        }
        getRoleByName(ascName, roleList).descendant = getRoleByName(desName, roleList);
        getRoleByName(desName, roleList).ascendantList.add(getRoleByName(ascName, roleList) );
        return true;
    }

    public static commonData getObjByName(String wantedName, List<commonData> commonDataList ){
        for (commonData cd: commonDataList){
            if (cd.name.equals(wantedName)){
                return cd;
            }
        }
        return new commonData(); //Just to make the compiler happy.
    }

    public static role getRoleByName(String wantedName, List<role> roleList ){
        for (role thisRole: roleList){
            if (thisRole.name.equals(wantedName)){
                return thisRole;
            }
        }
        return new role(); // just to make the compiler happy.
    }

    public static boolean searchForNameExistence(String wantedName, List<role> roleList){
        for ( role thisRole : roleList){
            if (thisRole.name.equals(wantedName)){
                return true;
            }
        }
        return false;
    }

    public static void sysp(String msg) {
        System.out.println(msg);
    }


    public static class role extends commonData {
        //role ascendant;
        List ascendantList = new ArrayList<role>();
        role descendant;

        role(){}
        role(String name){
            this.name = name;
        }
    }

    public static class user {
        String name;
        List<String> roles;
        user() {}
        user(String name, List<String> roles){
            this.name = name;
            this.roles = roles;
        }
    }

    public static class exclude {
        int numExclude;
        List<String> roles;

        exclude(){}
        exclude(int num, List<String> roles){
            this.numExclude = num;
            this.roles = roles;
        }
    }

    public static class commonData {
        String name;
        int index;
    }
}
