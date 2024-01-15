public class Song {
    int id;
    public String name;
    public int playcount;
    public int heartache;
    public int roadtrip;
    public int blissful;
    public Playlist playlist;
    Song(){}
    // Compares two songs
    public int compareTo(Song song, int property){
        if (property == 0){ // Compare method for heartache
            if (this.heartache == song.heartache){ return -this.name.compareTo(song.name);}
            return this.heartache - song.heartache;
        }
        if (property == 1){ // compare method for roadtrip
            if (this.roadtrip == song.roadtrip){ return -this.name.compareTo(song.name);}
            return this.roadtrip - song.roadtrip;
        }
        if (property == 2){ // compare method for blissful
            if (this.blissful == song.blissful){ return -this.name.compareTo(song.name);}
            return this.blissful - song.blissful;
        }
        if (property == 3){ // compare method for ask operation
            if (this.playcount == song.playcount){ return -this.name.compareTo(song.name);}
            return this.playcount - song.playcount;
        }
        return 0;
    }
    public boolean isSuitable(int property){ // returns whether the song is suitable for epicblend or not
        if (property == 0){
            return this.playlist.heartacheMin.hashMap.containsKey(this);
        }
        if (property == 1){
            return this.playlist.roadtripMin.hashMap.containsKey(this);
        }
        if (property == 2){
            return this.playlist.blissfulMin.hashMap.containsKey(this);
        }
        return false;
    }
}
