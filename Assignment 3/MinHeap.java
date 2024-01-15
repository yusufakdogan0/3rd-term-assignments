import java.util.HashMap;

public class MinHeap {
    public int capacity;
    public Song[] heap;
    public int size;
    public int property;
    public HashMap<Song,Integer> hashMap;
    MinHeap(int capacity, int property){
        this.size = 0;
        this.capacity = capacity;
        this.heap = new Song[capacity + 1];
        this.property = property;
        this.hashMap = new HashMap<Song,Integer>();
    }
    // Classic remove method
    public Song remove(int a){
        Song song = heap[a];
        heap[a] = heap[size];
        hashMap.put(heap[a],a);
        heap[size] = null;
        size--;
        if (a <= size){ //If we did not delete the last element it is necessary to percolate
            if (a != 1){
                if (heap[a].compareTo(heap[a/2],this.property) < 0){
                    percolateUp(a);
                }
            }
            percolateDown(a);
        }

        hashMap.remove(song);
        return song;
    }
    // It just finds an element's position and removes it by calling the other remove
    public Song remove(Song song){
        if (hashMap.get(song) != null){
            int index = hashMap.get(song);
            return remove(index);
        }
        return null;
    }
    // Just return whether it is full or not
    public boolean isFull(){
        return this.size == this.capacity;
    }
    // In order to correctly implement the algorithm I need to make sure it does not get overloaded
    public Song insert(Song songToInsert){
        if (this.isFull()) { // If minheap is full, remove and return the smallest element
            if ( heap[1].compareTo(songToInsert,property) < 0 ){
                Song temp = heap[1];
                heap[1] = songToInsert;
                hashMap.remove(temp);
                hashMap.put(songToInsert,1);
                percolateDown(1);
                return temp;
            }
            else {
                return songToInsert;
            }
        }
        else { // If it is not full no need to do anything just insert it
            size += 1;
            int hole = size;
            heap[hole] = songToInsert;
            hashMap.put(songToInsert,hole);
            percolateUp(hole);
            return null;
        }
    }
    public void percolateUp(int hole) {
        Song song = heap[hole];
        heap[0] = song;

        while (song.compareTo(heap[hole / 2], this.property) < 0) {
            heap[hole] = heap[hole / 2];
            hashMap.put(heap[hole],hole);
            hole /= 2;
        }

        heap[hole] = song;
        hashMap.put(song,hole);
        heap[0] = null;
    }
    public void percolateDown(int hole) {
        int child;
        Song songToMove = heap[hole];

        while (hole <= size / 2) {
            child = hole * 2;

            if (child != size && heap[ hole * 2 + 1].compareTo(heap[child], this.property) < 0) {
                child = hole * 2 + 1;
            }

            if (heap[child].compareTo(songToMove, this.property) < 0) {
                heap[hole] = heap[child];
                hashMap.put(heap[hole],hole);
            } else {
                break;
            }
            hole = child;
        }
        heap[hole] = songToMove;
        hashMap.put(songToMove,hole);
    }
}
