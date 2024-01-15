public class Playlist {
    public MinHeap heartacheMin;
    public MinHeap roadtripMin;
    public MinHeap blissfulMin;
    public MaxHeap heartacheMax;
    public MaxHeap roadtripMax;
    public MaxHeap blissfulMax;
    Playlist(int a){
        this.heartacheMin = new MinHeap(Project3.playCatLimit,0);
        this.roadtripMin = new MinHeap(Project3.playCatLimit,1);
        this.blissfulMin= new MinHeap(Project3.playCatLimit,2);
        this.heartacheMax = new MaxHeap(100,0);
        this.roadtripMax = new MaxHeap(100,1);
        this.blissfulMax= new MaxHeap(100,2);
    }
    public String addSong(Song song){
        int firstInit = 0;
        int secondInit = 0;
        int thirdInit = 0;
        int firstFinal = 0;
        int secondFinal = 0;
        int thirdFinal = 0;
        Song song1 = this.heartacheMin.insert(song);
        Song song2 = this.roadtripMin.insert(song);
        Song song3 = this.blissfulMin.insert(song);
        if (song1 != null){ // If there has been a change
            this.heartacheMax.add(song1); //add old element to the discard list
            Project3.heartacheDiscard2.add(song1);
            Project3.heartacheDiscard1.remove(song1);
            if (song1 != song){ // means song did not bounce back from minheap
                firstInit = song.id;
                song1 = Project3.epicBlendHeartache.remove(song1);
                Song oldSong = Project3.epicBlendHeartache.insert(song);
                if (song1 != null){ // means song1 is the song removed from epicblend
                    firstFinal = song1.id;
                    if (firstInit == firstFinal){
                        firstInit = firstFinal = 0;
                    }
                    if (song1.isSuitable(0)){Project3.heartacheDiscard1.add(song1);}
                    else {Project3.heartacheDiscard2.add(song1);}
                }
                else { // means oldSong is the song removed from epicblend
                    firstFinal = oldSong.id;
                    if (firstInit == firstFinal){
                        firstInit = firstFinal = 0;
                    }
                    if (oldSong.isSuitable(0)){Project3.heartacheDiscard1.add(oldSong);}
                    else {Project3.heartacheDiscard2.add(oldSong);}
                }
            }

        }

        else if ( !Project3.epicBlendHeartache.isFull() ){ // If there is no change and epicblend has space to spare
            Project3.epicBlendHeartache.insert(song);
            firstInit = song.id;
        }

        else { // if epicblend does not have this space then remove its min and add to discardeck 1
            Song oldSong = Project3.epicBlendHeartache.insert(song);
            if (oldSong != song){
                firstInit = song.id;
                firstFinal = oldSong.id;
            }
            Project3.heartacheDiscard1.add(oldSong);
        }



        if (song2 != null){// If there has been a change
            this.roadtripMax.add(song2);//add old element to the discard list
            Project3.roadtripDiscard2.add(song2);
            Project3.roadtripDiscard1.remove(song2);
            if (song2 != song){ // If the song did not bounce back
                secondInit = song.id;
                song2 = Project3.epicBlendRoadtrip.remove(song2);
                Song oldSong = Project3.epicBlendRoadtrip.insert(song);
                if (song2 != null){
                    secondFinal = song2.id;
                    if (secondInit == secondFinal){
                        secondInit = secondFinal = 0;
                    }
                    if (song2.isSuitable(1)){Project3.roadtripDiscard1.add(song2);}
                    else {Project3.roadtripDiscard2.add(song2);}
                }
                else {
                    secondFinal = oldSong.id;
                    if (secondInit == secondFinal){
                        secondInit = secondFinal = 0;
                    }
                    if (oldSong.isSuitable(1)){Project3.roadtripDiscard1.add(oldSong);}
                    else {Project3.roadtripDiscard2.add(oldSong);}
                }
            }
        }

        else if ( !Project3.epicBlendRoadtrip.isFull() ){
            Project3.epicBlendRoadtrip.insert(song);
            secondInit = song.id;
        }

        else {
            Song oldSong = Project3.epicBlendRoadtrip.insert(song);
            if (oldSong != song){
                secondInit = song.id;
                secondFinal = oldSong.id;
            }
            Project3.roadtripDiscard1.add(oldSong);
        }



        if (song3 != null){
            this.blissfulMax.add(song3);
            Project3.blissfullDiscard2.add(song3);
            Project3.blissfullDiscard1.remove(song3);
            if (song3 != song){
                thirdInit =song.id;
                song3 = Project3.epicBlendBlissfull.remove(song3);
                Song oldSong = Project3.epicBlendBlissfull.insert(song);
                if (song3 != null){
                    thirdFinal = song3.id;
                    if (thirdInit == thirdFinal){
                        thirdInit = thirdFinal = 0;
                    }
                    if (song3.isSuitable(2)){Project3.blissfullDiscard1.add(song3);}
                    else {Project3.blissfullDiscard2.add(song3);}
                }
                else {
                    thirdFinal = oldSong.id;
                    if (thirdInit == thirdFinal){
                        thirdInit = thirdFinal = 0;
                    }
                    if (oldSong.isSuitable(2)){Project3.blissfullDiscard1.add(oldSong);}
                    else {Project3.blissfullDiscard2.add(oldSong);}
                }
            }

        }

        else if ( !Project3.epicBlendBlissfull.isFull() ){
            Project3.epicBlendBlissfull.insert(song);
            thirdInit = song.id;
        }

        else {
            Song newSong = Project3.epicBlendBlissfull.insert(song);
            if (newSong != song){
                thirdInit = song.id;
                thirdFinal = newSong.id;
            }
            Project3.blissfullDiscard1.add(newSong);
        }
        return firstInit + " " + secondInit + " " + thirdInit + "\n" + firstFinal + " " + secondFinal + " " + thirdFinal;
    }

    public String remove(Song song){
        int firstInit = 0;
        int secondInit = 0;
        int thirdInit = 0;
        int firstFinal = 0;
        int secondFinal = 0;
        int thirdFinal = 0;
        Song song1 = this.heartacheMin.remove(song);
        Song song2 = this.roadtripMin.remove(song);
        Song song3 = this.blissfulMin.remove(song);
        if (song1 == null){ // If song is not inside no need to do anything update epicblend. Just remove it from discard decks
            this.heartacheMax.remove(song);
            Project3.heartacheDiscard1.remove(song);
            Project3.heartacheDiscard2.remove(song);
        }
        else { // If song is inside then the problem begins
            // First we need to update the minHeap
            Song newSong = this.heartacheMax.remove(1);
            if (newSong != null){
                Project3.heartacheDiscard1.add(Project3.heartacheDiscard2.remove(newSong)); // add the song from non-suitable discard deck to suitable one
                this.heartacheMin.insert(newSong);
            }
            // Then updating epicblend begins
            song1 = Project3.epicBlendHeartache.remove(song);
            if (song1 == null){ // If song is not in the epicblend then we are good.
                Project3.heartacheDiscard1.remove(song);
                Project3.heartacheDiscard2.remove(song);
            }
            else { // If it is inside
                Song newEpicSong = Project3.heartacheDiscard1.remove(1);
                if (newEpicSong != null){
                    Project3.epicBlendHeartache.insert(newEpicSong);
                    firstInit = newEpicSong.id;
                }
                firstFinal = song1.id;
            }
        }


        if (song2 == null){// If song is not inside no need to do anything update epicblend. Just remove it from discard decks
            this.roadtripMax.remove(song);
            Project3.roadtripDiscard1.remove(song);
            Project3.roadtripDiscard2.remove(song);
        }
        else {// If song is inside then the problem begins
            // First update the minheap
            Song newSong = this.roadtripMax.remove(1);
            if (newSong != null){
                Project3.roadtripDiscard1.add(Project3.roadtripDiscard2.remove(newSong));
                this.roadtripMin.insert(newSong);
            }
            // Then updating epicblend begins
            song2 = Project3.epicBlendRoadtrip.remove(song);
            if (song2 == null){// If song is not in the epicblend then we are good.
                Project3.roadtripDiscard1.remove(song);
                Project3.roadtripDiscard2.remove(song);
            }
            else {// If it is inside
                Song newEpicSong = Project3.roadtripDiscard1.remove(1);
                if (newEpicSong != null){
                    Project3.epicBlendRoadtrip.insert(newEpicSong);
                    secondInit = newEpicSong.id;
                }
                secondFinal = song2.id;
            }
        }



        if (song3 == null){// If song is not inside no need to do anything update epicblend. Just remove it from discard decks
            this.blissfulMax.remove(song);
            Project3.blissfullDiscard1.remove(song);
            Project3.blissfullDiscard2.remove(song);
        }
        else {// If song is inside then the problem begins
            // First update the minheap
            Song newSong = this.blissfulMax.remove(1);
            if (newSong != null){
                this.blissfulMin.insert(newSong);
                Project3.blissfullDiscard1.add(Project3.blissfullDiscard2.remove(newSong));
            }
            // Then updating epicblend begins
            song3 = Project3.epicBlendBlissfull.remove(song);
            if (song3 == null){// If song is not in the epicblend then we are good.
                Project3.blissfullDiscard1.remove(song);
                Project3.blissfullDiscard2.remove(song);
            }
            else {
                Song newEpicSong = Project3.blissfullDiscard1.remove(1);
                if (newEpicSong != null){
                    Project3.epicBlendBlissfull.insert(newEpicSong);
                    thirdInit = newEpicSong.id;
                }
                thirdFinal = song3.id;
            }
        }
        return firstInit + " " + secondInit + " " + thirdInit + "\n" + firstFinal + " " + secondFinal + " " + thirdFinal;
    }
}
