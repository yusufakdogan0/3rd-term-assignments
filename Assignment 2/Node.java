public class Node {
    public Node next;
    public Node prev;
    public Object value;
    public String key; // It is the name of the object and its key for hashtable
    Node(String key, Object value){
        this.value = value;
        this.key = key;
    }
    public Node addNext(String key, Object value){ // Add function of branches.
        if (this.next == null){
            this.next = new Node(key, value);
            this.next.prev = this;
            return this.next;
        }
        else if (this.next.key.equals(key)){
            return this.next;
        }
        else {return this.next.addNext(key,value);}
    }

    // Add functions for employees.
    // Since employees are used in instance variables like manager or promotionqueue we need a pass by reference here

    public void addNode(Node newNode){
        if (this.next == null){
            this.next = newNode;
            newNode.prev = this;
            newNode.next = null;
        }
        else {this.next.addNode(newNode);}
    }

    // Finds and returns a node for a given key

    public Node returnNode(String key){
        if (this.key.equals(key)){return this;}
        if (this.next == null){return null;}
        return this.next.returnNode(key);
    }


}
