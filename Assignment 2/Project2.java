import java.io.*;

public class Project2 {
    public static int initalBranchNum = 29; // represents initial size of the branch table
    public static Hashtable<Branch> branches = new Hashtable<>(initalBranchNum);
    public static void main(String[] args) {
        String inputFileName1 = args[0];
        String inputFileName2 = args[1];
        String outputFileName = args[2];
        try {
            BufferedReader initial = new BufferedReader(new FileReader(inputFileName1));
            BufferedReader input = new BufferedReader(new FileReader(inputFileName2));
            BufferedWriter output = new BufferedWriter(new FileWriter(outputFileName));
            String line;
            // Handling initial file

            while ((line = initial.readLine()) != null){
                if ( line.trim().equals("") ){continue;} // Pass the empty lines
                String[] lineArray = line.split(",");

                // The string branch refers to both cityName and branchName.
                // Since there was no function that concerns a whole city it is not necessary to distinguish them.

                String branch = (lineArray[0] + lineArray[1]).trim();
                String name = lineArray[2].trim();
                String job = lineArray[3].trim();

                // addbranch() method takes an employee and a branch.
                // If branch does not exist it adds a new branch and adds the employee in it.

                addBranch(branch, name, job);
            }
            while ((line = input.readLine()) != null){ // taking inputs
                line = line.trim();
                if (line.equals("")){continue;}
                String[] lineArray = line.split(":");
                if (lineArray.length == 1) { // if it is a new month.

                    // finishMonth is a method that is called at the end of every month.
                    // It assigns all the monthlyBonus values to 0.

                    finishMonth();
                    continue;
                }
                if (lineArray[0].equals("PRINT_MONTHLY_BONUSES")){
                    String[] branchArray = lineArray[1].split(",");
                    String branchName = (branchArray[0]+branchArray[1]).trim();

                    // findBranch is a static method that returns a branch for a given branchName in the format of "cityName branchName"
                    Branch branch = findBranch(branchName);
                    output.write("Total bonuses for the " + branchArray[1].trim() + " branch this month are: " + branch.monthlyBonus + "\n");
                }
                if (lineArray[0].equals("PRINT_OVERALL_BONUSES")){
                    String[] branchArray = lineArray[1].split(",");
                    String branchName = (branchArray[0]+branchArray[1]).trim();
                    Branch branch = findBranch(branchName);
                    output.write("Total bonuses for the " + branchArray[1].trim() + " branch are: " + branch.totalBonus + "\n");
                }
                if (lineArray[0].equals("PRINT_MANAGER")){
                    String[] branchArray = lineArray[1].split(",");
                    String branchName = (branchArray[0]+branchArray[1]).trim();
                    Branch branch = findBranch(branchName);
                    Node manager =  branch.manager;
                    output.write("Manager of the " + branchArray[1].trim() + " branch is " + manager.key + "." + "\n");
                }
                if (lineArray[0].equals("ADD")){
                    String[] branchArray = lineArray[1].split(",");
                    String branchName = (branchArray[0]+branchArray[1]).trim();
                    String employeeName = branchArray[2].trim();
                    String job = branchArray[3].trim();
                    Branch branch = findBranch(branchName);
                    if (branch.employees.find(employeeName) != null){ //if employee already exist
                        output.write("Existing employee cannot be added again.\n");
                        continue;
                    }
                    branch.addEmployee(employeeName,job);

                    // promotions() is a method that handles all the promotions in a branch and returns output String.
                    // dismissals() is a method that handles all the dismissals in a branch and returns output String.
                    // Promoting someone to manager is not considered as a promotion but as a dismissal or resign.
                    // Promotions should be handled before dismissals since it is possible to reach from cashier to manager in same line.
                    // They will be explained in there

                    output.write(branch.promotions());
                    output.write(branch.dismissals(branchName));
                }

                if (lineArray[0].equals("PERFORMANCE_UPDATE")){
                    String[] branchArray = lineArray[1].split(",");
                    String branchName = (branchArray[0]+branchArray[1]).trim();
                    String employeeName = branchArray[2].trim();
                    int points = Integer.parseInt(branchArray[3].trim());//Points are handled as integers
                    Branch branch = findBranch(branchName);
                    Node employeeNode = branch.employees.find(employeeName);
                    if (employeeNode == null){// If employee does not exist.
                        output.write("There is no such employee.");
                        output.newLine();
                        continue;
                    }
                    Employee employee = (Employee) employeeNode.value;
                    int bonus = employee.performanceUpdate(points);
                    branch.monthlyBonus += bonus;
                    branch.totalBonus += bonus;
                    if (employee.checkForDismissal()){ // returns true if employee has -5 promotion points.
                        if (employee.job.equals("MANAGER")){ branch.managerDismiss = true; }
                        if (employee.job.equals("COURIER")){ branch.courierDismiss = employeeNode; }
                        if (employee.job.equals("CASHIER")){ branch.cashierDismiss = employeeNode; }
                        if (employee.job.equals("COOK")){ branch.cookDismiss = employeeNode; }
                    }
                    if (employee.checkForPromotion()){ // returns true if employee has enough point to promote.
                        if (!branch.promotionQueue.contains(employeeNode)){
                            branch.promotionQueue.add(employeeNode);
                        }
                    }
                    else { // remove employee from promotion queue if he has no longer enough points to proomote
                        branch.promotionQueue.remove(employeeNode);
                    }
                    output.write(branch.promotions());
                    output.write(branch.dismissals(branchName));
                }
                if (lineArray[0].equals("LEAVE")){
                    String[] branchArray = lineArray[1].split(",");
                    String branchName = (branchArray[0]+branchArray[1]).trim();
                    String employeeName = branchArray[2].trim();
                    Branch branch = findBranch(branchName);
                    Node employeeNode = branch.employees.find(employeeName);
                    if (employeeNode == null){
                        output.write("There is no such employee.\n");
                        continue;
                    }

                    // resign() is a method that handles leave command and returns the output string

                    String message = branch.resign(employeeNode,branchName);
                    output.write(message);
                }
            }
        output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void finishMonth(){ // Sets every monthlyBonus value to 0
        for (Node curNode : branches.hashTable){
            while (curNode != null){
                ((Branch) curNode.value).monthlyBonus = 0;
                curNode = curNode.next;
            }
        }
    }
    public static Branch findBranch(String branchName){ // returns a branch from branches hashtable
        Node branchNode = branches.find(branchName);
        return ((Branch) branchNode.value);
    }

    // addBranch() first checks that whether a given branch exist or not.
    // If it does, adds an employee to it
    // If it does not, creates it and adds the employee anyway
    public static void addBranch(String branch, String name, String job){
        int hashCode = branch.hashCode(); // Used a default hash function
        int position = Math.abs(hashCode % branches.tableSize); // Used absolute value since hashcode() can return negative numbers
        Node branchNode;  // The node that employee will be put in
        if (branches.hashTable[position] == null){ // If branch does not exist create it and add
            branchNode = new Node(branch, new Branch());
            branches.hashTable[position] = branchNode;
            branches.size += 1;
            branches.checkLoadFactor();

        } else if (branches.hashTable[position].key.equals(branch)) { // If branch exists and is the first one in its chain, just assign it
            branchNode = branches.hashTable[position];

        } else { // If the first one on the chain is not the branch we are looking for continue to search

            // returnNode is a method that returns a given node in a linked list

            if (branches.hashTable[position].returnNode(branch)==null){ // If it does not exist we gotta add it so increase the size
                branches.size += 1;
            }

            // addNext() is the special add method for branchs.
            // If it can find the given branch in a linked list he will return it.
            // If it cant it will add it and return it
            // checkLoadFactor controls whether load factor which is "size/tablesize" has exceeded the certain threshold or not.
            // If it does, it initializes rehashing process


            branchNode = branches.hashTable[position].addNext(branch, new Branch());
            branches.checkLoadFactor();
        }
        if (branchNode.value instanceof Branch){ // Finally add the employee to the branch
            ((Branch) branchNode.value).addEmployee(name,job);
        }




 }











 }