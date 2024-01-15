import java.util.HashMap;

public class MaxHeap {
    public Song[] heap;
    public int size;
    public int property;
    public HashMap<Song,Integer> hashMap;
    MaxHeap(int size, int property){
        this.size = 0;
        this.heap = new Song[size+1];
        this.property = property;
        this.hashMap = new HashMap<Song,Integer>();
    }
    public Song remove(int a){
        Song song = heap[a];
        if (song == null){
            return null;
        }
        heap[a] = heap[size];
        hashMap.put(heap[a],a);
        heap[size] = null;
        size--;
        hashMap.remove(song);
        if (a <= size){ //If we did not delete the last element it is necessary to percolate
            if (a != 1){
                if (heap[a].compareTo(heap[a/2],this.property) > 0){
                    percolateUp(a);
                }
            }
            percolateDown(a);
        }
        return song;
    }
    public Song remove(Song song){
        if (hashMap.get(song) != null){
            int index = hashMap.get(song);
            return this.remove(index);
        }
        return null;
    }
    public void resize(){ // Since maxheaps does not have a limit it is necessary to resize
        Song[] newHeap = new Song[size*2+2];
        for (int a=1; a < size; a++){
            newHeap[a] = this.heap[a];
        }
        this.heap = newHeap;
    }
    public void add(Song song){ // This add method does not do anything rather than adding a new element like insert of minheap
        if (song == null){
            return;
        }
        if (this.hashMap.containsKey(song)){ // If song already exist
            return;
        }
        size += 1;
        if (this.isFull()) {
            this.resize();
        }
        int hole = size;
        heap[hole] = song;
        hashMap.put(heap[hole],hole);
        percolateUp(hole);
    }

    public boolean isFull(){
        return this.size == this.heap.length-1;
    }

    public void percolateUp(int hole) {
        Song song = heap[hole];
        heap[0] = song;
        while (song.compareTo(heap[hole / 2], this.property) > 0) {
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
            if (child != size && heap[ hole * 2 + 1].compareTo(heap[child], this.property) > 0) {
                child = hole * 2 + 1;
            }
            if (heap[child].compareTo(songToMove, this.property) > 0) {
                heap[hole] = heap[child];
                hashMap.put(heap[hole],hole);
            } else {
                break;
            }
            hole = child;
        }
        heap[hole] = songToMove;
        hashMap.put(heap[hole],hole);
    }

}
