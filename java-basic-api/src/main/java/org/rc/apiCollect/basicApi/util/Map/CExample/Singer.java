package org.rc.apiCollect.basicApi.util.Map.CExample;

public class Singer implements Comparable {
    private String name;
    private Song song;
    public Singer() {
        super();
    }
    public Singer(String name) {
        super();
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Song getSong() {
        return song;
    }
    public void setSong(Song song) {
        this.song = song;
    }
    @Override
    public String toString() {
        return name;
    }
    @Override
    public int compareTo(Object o) {
        if(o == this){
            return 0;
        }
        if(o instanceof Singer singer){
            return name.compareTo(singer.getName());
        }
        return 0;
    }
}
