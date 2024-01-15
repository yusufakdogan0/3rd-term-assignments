// Whole point of this class is ask method. Nothing else
public class Node {
    Node right;
    Node left;
    Node parent;
    Song value;
    Node(Song song){
        this.value = song;
    }
    public void add(Song song){
        if (song == null){
            return;
        }
        if (song.compareTo(this.value,3) == 0){ return;}
        if (song.compareTo(this.value,3) < 0){
            if (this.left == null){
                this.left = new Node(song);
            }
            else {
                this.left.add(song);
            }
        }
        else {
            if (this.right == null){
                this.right = new Node(song);
            }
            else {
                this.right.add(song);
            }

        }
    }
    public String write() { // returns the string of the ask method
        String output = "";
        if (this.right != null){
            output += this.right.write();
        }
        output += this.value.id +" ";
        if (this.left != null){
            output += this.left.write();
        }
        return output;
    }
}
