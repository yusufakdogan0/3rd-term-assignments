import java.util.ArrayList;

public class Branch {
    public static int initialTableSize = 91;
    public Hashtable<Employee> employees;
    public Node manager;
    public int monthlyBonus;
    public int totalBonus;
    public int courierNum;
    public int cashierNum;
    public int cookNum;
    public boolean managerDismiss; // It is true if manager should be dismissed.
    public Node cookDismiss; // Holds the cook waiting for the dismissal in the next opportunity
    public Node cashierDismiss;
    public Node courierDismiss;
    // promotionQueue is the queue that takes people waiting for promotions.
    // Actually it is better to make it cookProQueue since there can be at most one cashier waiting for promotion.
    // However it does not matter since promoting to manager is handled as dismissal in my code
    public ArrayList<Node> promotionQueue;
    Branch(){
        this.employees = new Hashtable<>(initialTableSize);
        this.promotionQueue = new ArrayList<>();
    }

    // Adds a new employee.

    public void addEmployee(String name, String job){
        Node newNode = new Node(name, new Employee(job));
        if (job.equals("MANAGER")){ // Assign the manager
            this.manager = newNode;
        }
        int hashCode = name.hashCode();
        int position = Math.abs(hashCode % employees.tableSize);
        if (job.equals("COURIER")){this.courierNum += 1;}  // Update numbers
        if (job.equals("CASHIER")){this.cashierNum += 1;}
        if (job.equals("COOK")){this.cookNum += 1;}
        if (this.employees.hashTable[position] == null){
            this.employees.hashTable[position] = newNode;
        }
        else {
            this.employees.hashTable[position].addNode(newNode);
        }
        employees.size += 1; // Update the size
        employees.checkLoadFactor(); // Check the load factor and rehash if it is necessary
    }

    // Check the dismissals
    public String dismissals(String branchName){
        branchName = branchName.split(" ")[1];
        String out = "";
        if (this.managerDismiss && ((Employee) this.manager.value).checkForDismissal()){ // If manager should be dismissed
            Node newManager = findToPromote("COOK"); // Find a new cook to replace him
            if (newManager != null){
                ((Employee) newManager.value).promote(); // promote the cook
                this.employees.delete(manager); // Let manager see his family.
                this.cookNum -= 1; // Update cook number
                // Write the output
                out += manager.key + " is dismissed from branch: " + branchName + ".\n" + newManager.key + " is promoted from Cook to Manager.\n";
                this.manager = newManager; // Update the manager
                this.managerDismiss = false;
            }
        }
        else { managerDismiss = false; } // If manager should not be dismissed

        if (this.courierDismiss != null && ((Employee) this.courierDismiss.value).checkForDismissal()){ // If courier should be dsmissed
            if (this.courierNum > 1){ // If he is not alone
                this.courierNum -= 1; // Update the number
                this.employees.delete(this.courierDismiss); // Give him an unpaid vacation
                out += this.courierDismiss.key + " is dismissed from branch: " + branchName + ".\n"; // Write the output
                this.courierDismiss = null;
            }
        }
        else {this.courierDismiss = null;} // If courier should not be dismissed

        // Cashiers and cooks are handled same with couriers

        if (this.cashierDismiss != null && ((Employee) this.cashierDismiss.value).checkForDismissal()){
            if (this.cashierNum > 1){
                this.cashierNum -= 1;
                this.employees.delete(this.cashierDismiss);
                out += this.cashierDismiss.key + " is dismissed from branch: " + branchName + ".\n";
                this.cashierDismiss = null;
            }
        }
        else {this.cashierDismiss = null;}

        if (this.cookDismiss != null && ((Employee) this.cookDismiss.value).checkForDismissal()){
            if (this.cookNum > 1){
                this.cookNum -= 1;
                this.employees.delete(this.cookDismiss);
                out += this.cookDismiss.key + " is dismissed from branch: " + branchName + ".\n";
                this.cookDismiss = null;
            }
        }
        else {this.cookDismiss = null;}

        return out; // Return the output text
    }
    // If an employee wanted to resign
    public String resign(Node employeeNode, String branchname){
        branchname = branchname.split(" ")[1];
        Employee employee = (Employee) employeeNode.value;

        // If he is already waiting for dismissal we cant let him go (and we won't pay for it)

        if (employeeNode == this.manager && this.managerDismiss){
            return "";
        }
        if (employeeNode == this.cashierDismiss || employeeNode == this.courierDismiss || employeeNode == this.cookDismiss){
            return "";
        }
        if (this.canResign(employee)){ // canResign() is a method returns true if someone can resign
            promotionQueue.remove(employeeNode);
            if (employee.job.equals("MANAGER")){ // manager is handled in the same way with dissmissals
                Node newManager = findToPromote("COOK");
                this.manager = newManager;
                ((Employee) newManager.value).promote();
                this.employees.delete(employeeNode);
                this.cookNum -= 1;
                return employeeNode.key + " is leaving from branch: " + branchname + ".\n" + newManager.key + " is promoted from Cook to Manager.\n";
            }
            else { // If he is not manager
                this.employees.delete(employeeNode);
                return employeeNode.key + " is leaving from branch: " + branchname + ".\n";
            }

        }
        else { // If he or she can't resign bc of us just slap some money on his face and give him some motivational speech
            this.monthlyBonus += 200;
            this.totalBonus += 200;
        }
        return "";
    }
    // Returns whether an employee can resign or not
    public boolean canResign(Employee employee){
        if (employee.job.equals("MANAGER")){
            Node newManager = findToPromote("COOK");
            return newManager != null;
        }
        if (employee.job.equals("COURIER")){
            if (this.courierNum <= 1){
                return false;
            }
            else { courierNum -= 1; }
        }
        if (employee.job.equals("CASHIER")){
            if (this.cashierNum <= 1) {
                return false;
            }
            else { cashierNum -= 1; }
        }
        if (employee.job.equals("COOK")){
            if (this.cookNum <= 1) {
                return false;
            }
            else {cookNum -= 1;}
        }
        return true;
    }
    // All the promotions from cashier to cook is handled here
    public String promotions(){
        String out = "";
        Node curNode = checkForPromotions(); // returns the cashier that can be promoted
        while (curNode != null){
            cashierNum -= 1;
            cookNum += 1;
            ((Employee)curNode.value).promote();
            promotionQueue.remove(curNode);
            if (((Employee)curNode.value).checkForPromotion()){
                promotionQueue.add(curNode);
            }
            out += curNode.key + " is promoted from Cashier to Cook.\n";
            curNode = checkForPromotions();
        }
        return out;
    }
    public Node checkForPromotions(){
        if (this.cashierNum > 1){
            Node cashier = findToPromote("CASHIER");
            return cashier;
        }
        return null;
    }
    // search promotion queue for a given job
    public Node findToPromote(String job){
        for (Node employeeNode : promotionQueue) {
            if (((Employee) employeeNode.value).job.equals(job)) {
                return employeeNode;
            }
        }
        return null;
    }
}
