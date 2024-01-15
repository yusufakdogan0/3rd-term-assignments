public class Employee {
    public String job;
    public int promotionPoints;
    Employee(String job){
        this.job = job;
    }
    public int performanceUpdate(int a){
        if (a > 0){
            this.promotionPoints += a/200;
            return a % 200;
        }
        else {
            a = Math.abs(a);
            this.promotionPoints -= a/200;
            return 0;
        }
    }
    public boolean checkForDismissal(){ return promotionPoints <= -5; }
    public void promote(){
        if (this.job.equals("CASHIER")){
            this.job = "COOK";
            this.promotionPoints -= 3;
        }
        else if (this.job.equals("COOK")){
            this.job = "MANAGER";
            this.promotionPoints -= 10;
        }
    }
    public boolean checkForPromotion(){
        if (this.job.equals("CASHIER")){ return this.promotionPoints >= 3; }
        else if (this.job.equals("COOK")){ return this.promotionPoints >= 10; }
        else { return false; }
    }

}
