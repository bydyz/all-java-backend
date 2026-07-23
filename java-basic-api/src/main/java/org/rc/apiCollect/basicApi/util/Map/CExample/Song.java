package org.rc.apiCollect.basicApi.util.Map.CExample;

public class Song implements Comparable {
    private String songName;//歌名
    public Song() {
        super();
    }
    public Song(String songName) {
        super();
        this.songName = songName;
    }
    public String getSongName() {
        return songName;
    }
    public void setSongName(String songName) {
        this.songName = songName;
    }
    @Override
    public String toString() {
        return "《" + songName + "》";
    }
    @Override
    public int compareTo(Object o) {
        if(o == this){
            return 0;
        }
        if(o instanceof Song song){
            return songName.compareTo(song.getSongName());
        }
        return 0;
    }
}
