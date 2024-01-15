public class Hashtable <E>{
    public int tableSize;
    public Node[] hashTable;
    public int size; // the number of elements in the hash table

    Hashtable (int a){
        this.tableSize = a;
        this.hashTable = new Node[tableSize];
    }

    // add method that used in rehashing by passing the value
    public void add (Node newNode){
        int hashCode = newNode.key.hashCode();
        int position = Math.abs(hashCode % this.tableSize);
        if (this.hashTable[position] == null){
            this.hashTable[position] = newNode;
            newNode.prev = null;
            newNode.next = null;
        }
        else {this.hashTable[position].addNode(newNode);}
    }

    // Finds and returns the chain for a given key
    public Node find(String key){
        int hashCode = key.hashCode();
        int position = Math.abs(hashCode % this.tableSize);
        if (this.hashTable[position] == null){
            return null;
        }
        else {return this.hashTable[position].returnNode(key);}
    }

    // Checks the load factor and initializes rehash process if it is necessary
    public void checkLoadFactor(){
        double loadFactor = size * 1.0 / tableSize;
        if (loadFactor > 0.5){
            this.rehash();
        }
    }
    public void rehash(){
        Hashtable<E> newTable = new Hashtable<>(2 * this.tableSize + 1);
        for (int a = 0; a < this.tableSize; a++ ){
            Node curNode = this.hashTable[a];
            if (curNode != null){
                Node nextNode = curNode.next;
                newTable.add(curNode);
                while (nextNode != null){
                    curNode = nextNode;
                    nextNode = curNode.next;
                    newTable.add(curNode);
                }
            }
        }
        this.tableSize = newTable.tableSize;
        this.hashTable = newTable.hashTable;
    }
    public void delete(Node node){ // deletes a node
        if (node.prev != null){
            node.prev.next = node.next;
            if (node.next != null){
                node.next.prev = node.prev;
            }
        }
        else {
            int hashCode = node.key.hashCode();
            int position = Math.abs(hashCode % this.tableSize);
            this.hashTable[position] = node.next;
            if (node.next != null ){ node.next.prev = null; }
        }
        this.size -= 1;

    }
}
