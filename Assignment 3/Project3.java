import java.io.*;
/**In my alghorithm epicblend has 2 maxheaps and 1 minheap and each playlist has 1 minheap and 1 maxheap for each category.
 Epicblend minheap is actually epicblend itself. Its element number is capped at max number of elements epicblend can store.
 Similarly, the number of elements in playlist minheap is capped at the number a playlist can contribute to the epicblend.
 Maxheaps are like discard decks. They store the elements that could not get into the minheaps. However, epicblend has 2 of them.
 Discard1 is for the songs that is in the minheap of its respective playlist.
 In other words they are suitable for epicblend, but they could not find a place in it.
 Discard2 is actually the total of all the maxheaps in playlists. They are not suitable for epicblend unless there is a remove operation.
 Actually it is not necessary and removing it will fasten up my code, but I don't know code is working fast enough.
 Violating the golden rule (If something is working DO NOT TOUCH IT) is not necessary :)
**/
public class Project3 {
    public static int playCatLimit; // indicates the max number of songs that a playlist contribute to
    public static MinHeap epicBlendHeartache;
    public static MaxHeap heartacheDiscard1 = new MaxHeap(100, 0);
    public static MaxHeap heartacheDiscard2 = new MaxHeap(100, 0);
    public static MinHeap epicBlendRoadtrip;
    public static MaxHeap roadtripDiscard1 = new MaxHeap(100, 1);
    public static MaxHeap roadtripDiscard2 = new MaxHeap(100, 1);
    public static MinHeap epicBlendBlissfull;
    public static MaxHeap blissfullDiscard1 = new MaxHeap(100, 2);
    public static MaxHeap blissfullDiscard2 = new MaxHeap(100, 2);
    public static void main(String[] args) {
        String songsFileName =  args[0];
        String inputFileName = args[1];
        String outputFileName = args[2];
        try {
            BufferedReader songsInp = new BufferedReader(new FileReader(songsFileName));
            BufferedReader input = new BufferedReader(new FileReader(inputFileName));
            FileWriter output = new FileWriter(outputFileName);
            String line;
            // take songs
            // index = id
            int songNumber = Integer.parseInt(songsInp.readLine().trim());
            Song[] songs = new Song[songNumber + 1];
            // Create an array called songs to store the songs
            while ((line = songsInp.readLine()) != null) {
                line = line.trim();
                if (line.equals("")) {
                    continue;
                }
                Song song = new Song();
                String[] lineArray = line.split(" ");
                song.id = Integer.parseInt(lineArray[0]);
                song.name = lineArray[1];
                song.playcount = Integer.parseInt(lineArray[2]);
                song.heartache = Integer.parseInt(lineArray[3]);
                song.roadtrip = Integer.parseInt(lineArray[4]);
                song.blissful = Integer.parseInt(lineArray[5]);
                songs[song.id] = song;
            }
            // Now we need to create epicblend
            String[] firstLine = input.readLine().split(" ");
            playCatLimit = Integer.parseInt(firstLine[0]);
            epicBlendHeartache = new MinHeap(Integer.parseInt(firstLine[1]), 0);
            epicBlendRoadtrip = new MinHeap(Integer.parseInt(firstLine[2]), 1);
            epicBlendBlissfull = new MinHeap(Integer.parseInt(firstLine[3]), 2);
            int playlistNumber = Integer.parseInt(input.readLine());
            Playlist[] playlists = new Playlist[playlistNumber + 1];

            // Create playlists
            for (int id = 1; id <= playlistNumber; id++) {
                int songNum = Integer.parseInt(input.readLine().split(" ")[1]);
                Playlist playlist = new Playlist(songNum);
                playlists[id] = playlist;
                String[] songArray = input.readLine().split(" ");
                for (int index = 0; index < songNum; index++) {
                    int songId = Integer.parseInt(songArray[index]);
                    Song song = songs[songId];
                    song.playlist = playlist;
                    playlist.addSong(song);
                }
            }

            int eventnumber = Integer.parseInt(input.readLine());
            // handling events
            for (int a = 0; a < eventnumber; a++) {
                String[] inputArray = input.readLine().split(" ");
                if (inputArray[0].equals("ASK")) {
                    double asktime = System.currentTimeMillis();
                    askTree(output);
                    System.out.println((System.currentTimeMillis() - asktime) / 1000);
                    continue;
                }

                int songId = Integer.parseInt(inputArray[1]);
                int playlistId = Integer.parseInt(inputArray[2]);
                Song song = songs[songId];
                Playlist playlist = playlists[playlistId];
                if (inputArray[0].equals("ADD")) {
                    song.playlist = playlist;
                    String outputStr = playlist.addSong(song);
                    output.write(outputStr + "\n");
                    continue;
                }
                if (inputArray[0].equals("REM")) {
                    String outputStr = playlist.remove(song);
                    song.playlist = null;
                    output.write(outputStr + "\n");
                }
            }


            songsInp.close();
            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void askTree(FileWriter output) throws IOException {
        // My ask function is basic, create a bst tree and execute it
        Node mother;
        if (epicBlendHeartache.heap[1] != null){
         mother = new Node(epicBlendHeartache.heap[1]);
        }
        else if (epicBlendRoadtrip.heap[1] != null){
            mother = new Node(epicBlendRoadtrip.heap[1]);
        }
        else if ( epicBlendBlissfull.heap[1] != null){
            mother = new Node(epicBlendBlissfull.heap[1]);
        }
        else{
            output.write("\n");
            return;
        }
        for (Song element: epicBlendHeartache.heap){
            mother.add(element);
        }
        for (Song element: epicBlendRoadtrip.heap){
            mother.add(element);
        }
        for (Song element: epicBlendBlissfull.heap){
            mother.add(element);
        }
        output.write(mother.write().trim()+"\n");
    }
}