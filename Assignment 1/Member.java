import javax.imageio.IIOException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
//update findMembersİnRank
public class Member {
    public static Member boss = new Member();
    public String name;
    public double gms;
    public Member superior;
    public Member rightSubordinate;
    public Member leftSubordinate;
    //height is 1 for leafs
    public int height;
    public static int divisonNumber;

    public Member (){}
    public Member(String name, double gms){
        this.name = name;
        this.gms = gms;
    }
    //reorganize function will reorganize any given member and move to the its initial(before reorganization) superior.
    public void reorganize(){
        //First store initial superior as initSuperior
        Member initSuperior = new Member();
        if (this.superior != null){initSuperior = this.superior;}
        //balance function returns height of the left subordinate- height of the right subordinate
        //in other words it returns how much left branch is bigger than right branch
        if (this.balance() > 1){//left is bigger than right
            if (this.leftSubordinate.balance() >= 0){this.leftRotation();}//left-left is bigger than left-right
            else {//left-right is bigger than left-left
                this.leftSubordinate.rightRotation();
                this.leftRotation();
            }
        }
        if (balance() < -1){//right is bigger than left
            if (this.rightSubordinate.balance() <= 0){this.rightRotation();}//right-right is bigger than right-left
            else{//right-left is bigger than right-right
                this.rightSubordinate.leftRotation();
                this.rightRotation();
            }
        }
        if (initSuperior.name != null){initSuperior.reorganize();}//reorgnaize initial superior if it is not null
    }
    //find function returns a member with a given gms
    public Member find(double gms){
        if (this.gms == gms){return this;}
        if (this.gms < gms){return this.rightSubordinate.find(gms);}
        else {return this.leftSubordinate.find(gms);}
    }
    //leftrotation function rotates a given member with his left subordinate.
    //Actually it is right rotation in book but visualizing it, was much easier for me in this way.
    public void leftRotation(){
        if (this == boss){
            boss = this.leftSubordinate;
            boss.superior = null;
        }
        else {
            this.leftSubordinate.superior = this.superior;
            if (this.superior.gms < this.gms){this.superior.rightSubordinate = this.leftSubordinate;}
            else {this.superior.leftSubordinate = this.leftSubordinate;}
        }
        this.superior = this.leftSubordinate;
        this.leftSubordinate = this.superior.rightSubordinate;
        if(this.leftSubordinate!=null){this.leftSubordinate.superior = this;}
        this.superior.rightSubordinate = this;
        this.updateHeight();

    }
    //rightrotation function rotates a given member with his right subordinate.
    public void rightRotation(){
        if(this == boss){
            boss = this.rightSubordinate;
            boss.superior = null;
        }
        else{
            this.rightSubordinate.superior = this.superior;
            if (this.superior.gms < this.gms){this.superior.rightSubordinate = this.rightSubordinate;}
            else {this.superior.leftSubordinate = this.rightSubordinate;}
        }

        this.superior = this.rightSubordinate;
        this.rightSubordinate = this.superior.leftSubordinate;
        if(this.rightSubordinate!=null){this.rightSubordinate.superior = this;}
        this.superior.leftSubordinate = this;
        this.updateHeight();
    }
    // join is a void function that adds a new member to family and updates the output
    // After adding new member it updates all of its superiors heights and reorganize them by using updateHeight and reorganize.
    public void join(Member member, BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(this.name + " welcomed " + member.name+"\n");
        if (this.gms > member.gms){
            if (this.leftSubordinate == null){
                this.leftSubordinate = member;
                member.superior=this;
                member.updateHeight();
                this.reorganize();
            }
            else {this.leftSubordinate.join(member,bufferedWriter);}
        }
        else {
            if (this.rightSubordinate == null){
                this.rightSubordinate = member;
                member.superior=this;
                member.updateHeight();
                this.reorganize();
            }
            else {this.rightSubordinate.join(member,bufferedWriter);}
        }
    }
    // It just finds the leftmost subordinate and only used in leave
    public Member smallestGmsSubordinate(){ //returns subordinate with smallest gms
        if (this.leftSubordinate == null){return this;}
        else {return this.leftSubordinate.smallestGmsSubordinate();}
    }
    public String leave(){
        Member replacement;
        String str;
        //both subordinates exist
        if (this.leftSubordinate != null & this.rightSubordinate != null){
            replacement = this.rightSubordinate.smallestGmsSubordinate();
            str = this.name + " left the family, replaced by " + replacement.name;
            this.name = replacement.name;
            this.gms = replacement.gms;
            if (replacement.superior != this){
                replacement.superior.leftSubordinate = replacement.rightSubordinate;
                if (replacement.rightSubordinate!=null){
                    replacement.rightSubordinate.superior = replacement.superior;//
                }
                replacement.superior.updateHeight();
                replacement.superior.reorganize();
            }
            // If right subordinate has no left subordinate
            if (replacement.superior == this){
                this.rightSubordinate = replacement.rightSubordinate;
                if (replacement.rightSubordinate!=null){replacement.rightSubordinate.superior = this;}//
                this.updateHeight();
                this.reorganize();
            }

        }
        //only right subordinate exist
        else if (this.rightSubordinate != null & this.leftSubordinate == null){
            replacement = this.rightSubordinate;
            str = this.name + " left the family, replaced by " + replacement.name;
            this.name = replacement.name;
            this.gms = replacement.gms;
            this.rightSubordinate = replacement.rightSubordinate;
            if (replacement.rightSubordinate!=null){replacement.rightSubordinate.superior = this;}//
            this.leftSubordinate = replacement.leftSubordinate;
            if (replacement.leftSubordinate!=null){replacement.leftSubordinate.superior = this;}//
            replacement.superior.updateHeight();
            replacement.superior.reorganize();
        }
        //only left subordinate exist
        else if (this.leftSubordinate != null) {
            replacement = this.leftSubordinate;
            str = this.name + " left the family, replaced by " + replacement.name;
            this.name = replacement.name;
            this.gms = replacement.gms;
            this.leftSubordinate = replacement.leftSubordinate;
            if (replacement.leftSubordinate!=null){replacement.leftSubordinate.superior = this;}//
            this.rightSubordinate = replacement.rightSubordinate;
            if (replacement.rightSubordinate!=null){replacement.rightSubordinate.superior = this;}//
            replacement.superior.updateHeight();
            replacement.superior.reorganize();
        }
        // There is no subordinate
        else {
            str = this.name + " left the family, replaced by nobody";
            if (this == boss){Member.boss = null;}
            else if (this.gms < this.superior.gms){
                this.superior.leftSubordinate = null;
                this.superior.updateHeight();
                this.superior.reorganize();
            }
            else {
                this.superior.rightSubordinate=null;
                this.superior.updateHeight();
                this.superior.reorganize();
            }
        }
        return str;
    }
    public Member target(double gms1, double gms2){
        double gms = this.gms;

        if (gms == gms1){return this;}
        if (gms == gms2){return this;}
        if (gms < gms1){
            if (gms < gms2){return this.rightSubordinate.target(gms1,gms2);}
            else {return this;}
        }

        if (gms < gms2) {return this;}
        else {return this.leftSubordinate.target(gms1,gms2);}

    }
    //Updates heights of a member and all of its superiors.
    //Whenever there is a change in tree this function will be called and heights will always be correct before next operations
    public void updateHeight(){
        Member member = this;
        while (member != null){
            int left=0;
            int right=0;
            if (member.leftSubordinate != null){left = member.leftSubordinate.height;}
            if (member.rightSubordinate != null){right = member.rightSubordinate.height;}
            member.height = Math.max(left,right) + 1;
            member = member.superior;
        }


    }
    public int balance(){
        int left=0;
        int right=0;
        if (this.leftSubordinate != null){left = this.leftSubordinate.height;}
        if (this.rightSubordinate != null){right = this.rightSubordinate.height;}
        return left-right;
    }
    //My division algorithm is actually basic. First choose every leaf then come to the boss
    //In order to do that I wrote a recursive function which returns whether this leaf is taken or not
    //For every taken member our static variable will be updated and for each division operation it will be 0 again.
    public boolean divide(){
        if (this.height == 1){//If this is a leaf
            divisonNumber += 1;
            return true;
        }
        else {
            if (this.leftSubordinate == null){
                boolean right = this.rightSubordinate.divide();
                if (!right){//if right is not taken and left is null
                    divisonNumber += 1;
                    return true;
                }
            }
            else if (this.rightSubordinate==null) {
                boolean left = this.leftSubordinate.divide();
                if (!left){//if right is null and left is not taken
                    divisonNumber += 1;
                    return true;
                }
            }
            else {//if both right and left exist
                boolean right = this.rightSubordinate.divide();
                boolean left = this.leftSubordinate.divide();
                if((!left) && (!right)){//if both subordinates are nontaken
                    divisonNumber += 1;
                    return true;
                }
            }
        }
        return false;
    }
    public int findRank(double gms){
        if (this.gms == gms){return 0;}
        if (this.gms < gms){return 1 + this.rightSubordinate.findRank(gms);}
        else {return 1 + this.leftSubordinate.findRank(gms);}
    }
    public ArrayList<Member> findMembersInRank(int x){
        if (x == 0){
            ArrayList<Member> result = new ArrayList<Member>();
            result.add(this);
            return result;
        }
        else {
            ArrayList<Member> result = new ArrayList<Member>();
            //if the distance is equal to the height then it is unnecassary to continue since rest will be null
            if (x >= this.height){return result;}
            if (this.leftSubordinate!=null){result.addAll(
                    this.leftSubordinate.findMembersInRank(x-1));
            }
            if (this.rightSubordinate != null){
                result.addAll(this.rightSubordinate.findMembersInRank(x-1));
            }

            return result;
        }
    }
}
